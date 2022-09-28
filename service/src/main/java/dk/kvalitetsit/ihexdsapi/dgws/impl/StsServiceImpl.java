package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.io.IOException;
import java.util.Properties;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.model.*;
import org.openapitools.model.HealthcareProfessionalContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.StsService;
import dk.sosi.seal.SOSIFactory;
import dk.sosi.seal.model.constants.IDValues;
import dk.sosi.seal.model.constants.SubjectIdentifierTypeValues;
import dk.sosi.seal.pki.SOSITestFederation;
import dk.sosi.seal.pki.SignatureProviderFactory;
import dk.sosi.seal.vault.CredentialVault;
import dk.sosi.seal.xml.XmlUtil;

public class StsServiceImpl implements StsService {

	private String stsUrl;

	private RestTemplate restTemplate = new RestTemplate();

	private String itSystem;

	public StsServiceImpl(String stsUrl) {
		this.stsUrl = stsUrl;
		this.itSystem = "IheXdsApi";
	}

	@Override
	public DgwsClientInfo getDgwsClientInfoForSystem(CredentialInfo credentialInfo, String patientId, HealthcareProfessionalContext context) throws DgwsSecurityException {
		Properties properties = new Properties(System.getProperties());
		properties.setProperty(SOSIFactory.PROPERTYNAME_SOSI_VALIDATE, Boolean.toString(true));
		SOSIFactory sosiFactory = new SOSIFactory(new SOSITestFederation(properties), credentialInfo.getCredentialVault(), properties);

		String cvr = credentialInfo.getCredentialVault().getSystemCredentialPair().getCertificate().getSubjectX500Principal().getName();
		cvr = cvr.substring(cvr.lastIndexOf('/') + 6,cvr.lastIndexOf(','));

		String authorizationCode = context.getAuthorizationCode();
		if (authorizationCode.equals("null")) {
			authorizationCode = null;
		}

		UserIDCard userIDCard = getUserIdCard(credentialInfo.getCredentialVault(), sosiFactory, cvr, context.getRole(), authorizationCode);


		Request request = sosiFactory.createNewRequest(false, null);
		request.setIDCard(userIDCard);
		return new DgwsClientInfo(request.serialize2DOMDocument(), userIDCard.getUserInfo().getCPR(), patientId, userIDCard.getUserInfo().getAuthorizationCode(), cvr);
	}

	private UserIDCard getUserIdCard(CredentialVault credentialVault, SOSIFactory sosiFactory, String cvr, String role, String authCode) throws DgwsSecurityException {

		String rawString = credentialVault.getSystemCredentialPair().getCertificate().getSubjectX500Principal().getName();

		String name = rawString.substring(3, rawString.indexOf(' '));
		String surName =rawString.substring(rawString.indexOf(' ')+1, rawString.indexOf('+'));
		String orgName = rawString.substring(rawString.indexOf(',') + 3, rawString.indexOf('/') - 1 );


		UserInfo userInfo = new UserInfo(null, name, surName, null, null, role, authCode);
		CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, orgName);
		UserIDCard selfSigned = sosiFactory.createNewUserIDCard(itSystem, userInfo, careProvider, AuthenticationLevel.MOCES_TRUSTED_USER, null, null, null, null);

		SecurityTokenRequest securityTokenRequest = sosiFactory.createNewSecurityTokenRequest();
		securityTokenRequest.setIDCard(selfSigned);
		Document doc = securityTokenRequest.serialize2DOMDocument();

		SignatureConfiguration signatureConfiguration = new SignatureConfiguration(new String[] { IDValues.IDCARD }, IDValues.IDCARD, IDValues.id);
		SignatureUtil.sign(SignatureProviderFactory.fromCredentialVault(credentialVault), doc, signatureConfiguration);
		String requestXml = XmlUtil.node2String(doc, false, true);

		String responseXml = null;
		try {
			responseXml = sendRequest(requestXml);
		} catch (IOException e) {
			throw new DgwsSecurityException(e, 1000, "Something went wrong");
		}

		if (responseXml.contains("Authentication failed: multiple authorizations found")) {
			String errorMsg = responseXml.substring(706, 791);

			String example = errorMsg.substring(errorMsg.indexOf('{') + 1, errorMsg.indexOf(','));

			//System.out.println("HELLO");
			/*System.out.println(errorMsg);
			System.out.println(responseXml.length());
			System.out.println(responseXml.substring(690));

			System.out.println(responseXml.indexOf('<', 791));


			System.out.println(true);*/

			throw new DgwsSecurityException(1000, errorMsg + ". Choose one\ne.g. " + example);
		}

		SecurityTokenResponse securityTokenResponse = sosiFactory.deserializeSecurityTokenResponse(responseXml);

		if (securityTokenResponse.isFault() || securityTokenResponse.getIDCard() == null) {
			throw  new DgwsSecurityException(1000, "No ID card :-(");
		}
		else {
			return  (UserIDCard) securityTokenResponse.getIDCard();


		}
	}

	private Document getSystemIdCardFromSTS(CredentialVault credentialVault, String cvr, String organisation) throws DgwsSecurityException {
		Properties properties = new Properties(System.getProperties());
		properties.setProperty(SOSIFactory.PROPERTYNAME_SOSI_VALIDATE, Boolean.toString(true));
		SOSIFactory sosiFactory = new SOSIFactory(new SOSITestFederation(properties), credentialVault, properties);




		CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, organisation);


		// User info / createNew USERIDCARD
		SystemIDCard selfSignedUserIdCard = sosiFactory.createNewSystemIDCard(itSystem, careProvider, AuthenticationLevel.VOCES_TRUSTED_SYSTEM, null, null, credentialVault.getSystemCredentialPair().getCertificate(), null);

		SecurityTokenRequest securityTokenRequest = sosiFactory.createNewSecurityTokenRequest();
		securityTokenRequest.setIDCard(selfSignedUserIdCard);
		Document doc = securityTokenRequest.serialize2DOMDocument();

		SignatureConfiguration signatureConfiguration = new SignatureConfiguration(new String[] { IDValues.IDCARD }, IDValues.IDCARD, IDValues.id);
		//SignatureUtil.sign(SignatureProviderFactory.fromCredentialVault(credentialVault), doc, signatureConfiguration);
		String requestXml = XmlUtil.node2String(doc, false, true);

		String responseXml = null;
		try {
			responseXml = sendRequest(requestXml);
		} catch (IOException e) {
			throw new DgwsSecurityException(e, 1000, "Something went wrong");
		}
		SecurityTokenResponse securityTokenResponse = sosiFactory.deserializeSecurityTokenResponse(responseXml);

		if (securityTokenResponse.isFault() || securityTokenResponse.getIDCard() == null) {
			throw  new DgwsSecurityException(1000, "No ID card :-(");
		}
		else {
			SystemIDCard stsSignedIdCard = (SystemIDCard) securityTokenResponse.getIDCard();
			Request request = sosiFactory.createNewRequest(false, null);
			request.setIDCard(stsSignedIdCard);
			return request.serialize2DOMDocument();
		}
	}
/*
	
	protected Document generateTokenRequestForUserIdCard(CredentialVault vault, SOSIFactory sosiFactory) {
		
		UserInfo userInfo = new UserInfo(getUserCpr(), getFirstName(), getLastName(), null, "Test", "læge", getAuhorizationCode());

		CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, getCvr(), getOrg());
		
		UserIDCard selfSignedUserIdCard = sosiFactory.createNewUserIDCard(getItSystem(), userInfo, careProvider, AuthenticationLevel.MOCES_TRUSTED_USER, null, null, vault.getSystemCredentialPair().getCertificate(), null);

		SecurityTokenRequest securityTokenRequest = sosiFactory.createNewSecurityTokenRequest();
		securityTokenRequest.setIDCard(selfSignedUserIdCard);
		Document doc = securityTokenRequest.serialize2DOMDocument();

		SignatureConfiguration signatureConfiguration = new SignatureConfiguration(new String[] { IDValues.IDCARD }, IDValues.IDCARD, IDValues.id);
		SignatureUtil.sign(SignatureProviderFactory.fromCredentialVault(vault), doc, signatureConfiguration);

		return doc;
	}*/





	private String sendRequest(String postBody) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml; charset=utf-8");
		headers.set("SOAPAction", "\"Issue\"");

		HttpEntity<String> entity = new HttpEntity<>(postBody, headers);

		ResponseEntity<String> result = restTemplate.exchange(stsUrl, HttpMethod.POST, entity, String.class);
		
		int statusCode = result.getStatusCode().value();
		if (statusCode != 200) {
			throw new IOException("HTTP POST failed (" + statusCode + "): " + result.getBody());
		}

		return result.getBody();
	}
}
