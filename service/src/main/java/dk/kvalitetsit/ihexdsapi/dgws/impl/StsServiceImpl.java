package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.io.IOException;
import java.util.Properties;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.model.*;
import org.openapitools.model.CredentialInfoResponse;
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

    // User  Moces
    @Override
    public DgwsClientInfo getDgwsClientInfoForSystem(CredentialInfo credentialInfo, String patientId, HealthcareProfessionalContext context) throws DgwsSecurityException {
        if (credentialInfo == null) {
            throw new DgwsSecurityException(1000, "CredentialInfo is null");
        }

        Properties properties = new Properties(System.getProperties());
        properties.setProperty(SOSIFactory.PROPERTYNAME_SOSI_VALIDATE, Boolean.toString(true));
        SOSIFactory sosiFactory = new SOSIFactory(new SOSITestFederation(properties), credentialInfo.getCredentialVault(), properties);

        String cvr = credentialInfo.getCredentialVault().getSystemCredentialPair().getCertificate().getSubjectX500Principal().getName();
        cvr = cvr.substring(cvr.lastIndexOf('/') + 6, cvr.lastIndexOf(','));

        String authorizationCode = context.getAuthorizationCode();


        UserIDCard userIDCard = getUserIdCard(credentialInfo, sosiFactory, cvr, context.getRole(), authorizationCode);


        Request request = sosiFactory.createNewRequest(false, null);
        request.setIDCard(userIDCard);
        if (context.getConsentOverride() == null)  {
            throw new DgwsSecurityException(1000, "Consent override must be a valid boolean value");
        }
        return new DgwsClientInfo(request.serialize2DOMDocument(), userIDCard.getUserInfo().getCPR(), patientId, userIDCard.getUserInfo().getAuthorizationCode(), cvr, context.getConsentOverride());
    }

    @Override
    public DgwsClientInfo getDgwsClientInfoForSystem(CredentialInfo credentialInfo) throws DgwsSecurityException {
        Properties properties = new Properties(System.getProperties());
        properties.setProperty(SOSIFactory.PROPERTYNAME_SOSI_VALIDATE, Boolean.toString(true));
        SOSIFactory sosiFactory = new SOSIFactory(new SOSITestFederation(properties), credentialInfo.getCredentialVault(), properties);


        SystemIDCard systemIDCard = getSystemIdCardFromSTS(credentialInfo, credentialInfo.getCvr(), credentialInfo.getOrganisationName(), sosiFactory);


        Request request = sosiFactory.createNewRequest(false, null);
        request.setIDCard(systemIDCard);
        return new DgwsClientInfo(request.serialize2DOMDocument());
    }

    private UserIDCard getUserIdCard(CredentialInfo credentialInfo, SOSIFactory sosiFactory, String cvr, String role, String authCode) throws DgwsSecurityException {
        CredentialVault credentialVault = credentialInfo.getCredentialVault();

        String rawString = credentialVault.getSystemCredentialPair().getCertificate().getSubjectX500Principal().getName();


        String name = rawString.substring(3, rawString.indexOf(' '));
        String surName = rawString.substring(rawString.indexOf(' ') + 1, rawString.indexOf('+'));
        String orgName = rawString.substring(rawString.indexOf(',') + 3, rawString.indexOf('/') - 1);

        if (role == null || role.isEmpty()) {
            throw new DgwsSecurityException(1000, "UserRole must be specified");
        }
        UserInfo userInfo = new UserInfo(null, name, surName, null, null, role, authCode);
        CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, orgName);
        UserIDCard selfSigned = sosiFactory.createNewUserIDCard(itSystem, userInfo, careProvider, AuthenticationLevel.MOCES_TRUSTED_USER, null, null, null, null);

        SecurityTokenRequest securityTokenRequest = sosiFactory.createNewSecurityTokenRequest();
        securityTokenRequest.setIDCard(selfSigned);
        Document doc = securityTokenRequest.serialize2DOMDocument();

        SignatureConfiguration signatureConfiguration = new SignatureConfiguration(new String[]{IDValues.IDCARD}, IDValues.IDCARD, IDValues.id);
        SignatureUtil.sign(SignatureProviderFactory.fromCredentialVault(credentialVault), doc, signatureConfiguration);
        String requestXml = XmlUtil.node2String(doc, false, true);

        String responseXml = null;
        try {
            responseXml = sendRequest(requestXml);

        } catch (IOException e) {
            throw new DgwsSecurityException(e, 1000, "Something went wrong");
        }


        SecurityTokenResponse securityTokenResponse = sosiFactory.deserializeSecurityTokenResponse(responseXml);

        if (securityTokenResponse.isFault() || securityTokenResponse.getIDCard() == null) {
            throw new DgwsSecurityException(1000, securityTokenResponse.getFaultString());
        } else {
            return (UserIDCard) securityTokenResponse.getIDCard();


        }
    }


    // Voces / Org certicate
    private SystemIDCard getSystemIdCardFromSTS(CredentialInfo credentialInfo, String cvr, String organisation, SOSIFactory sosiFactory) throws DgwsSecurityException {
        CredentialVault credentialVault = credentialInfo.getCredentialVault();


        CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, organisation);


        SystemIDCard selfSignedSystemIdCard = sosiFactory.createNewSystemIDCard(itSystem, careProvider, AuthenticationLevel.VOCES_TRUSTED_SYSTEM, null, null, credentialVault.getSystemCredentialPair().getCertificate(), null);

        SecurityTokenRequest securityTokenRequest = sosiFactory.createNewSecurityTokenRequest();
        securityTokenRequest.setIDCard(selfSignedSystemIdCard);
        Document doc = securityTokenRequest.serialize2DOMDocument();

        //SignatureConfiguration signatureConfiguration = new SignatureConfiguration(new String[]{IDValues.IDCARD}, IDValues.IDCARD, IDValues.id);
        //SignatureUtil.sign(SignatureProviderFactory.fromCredentialVault(credentialVault), doc, signatureConfiguration);
        String requestXml = XmlUtil.node2String(doc, false, true);
        System.out.println();

        String responseXml = null;
        try {
            responseXml = sendRequest(requestXml);
        } catch (IOException e) {
            throw new DgwsSecurityException(e, 1000, "Something went wrong");
        }
        SecurityTokenResponse securityTokenResponse = sosiFactory.deserializeSecurityTokenResponse(responseXml);

        if (securityTokenResponse.isFault() || securityTokenResponse.getIDCard() == null) {
            throw new DgwsSecurityException(1000, securityTokenResponse.getFaultString());
        }

        return (SystemIDCard) securityTokenResponse.getIDCard();
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
