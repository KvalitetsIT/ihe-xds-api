package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.xds.Codes;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.joda.time.DateTime;
import org.openapitools.model.Iti18QueryParameter;
import org.openapitools.model.Iti18QueryResponse;
import org.openapitools.model.Iti18Response;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLQueryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;

import java.time.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Iti18ServiceImpl implements Iti18Service {

	private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();

	private Iti18PortType iti18PortType;

	private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();
	
	public Iti18ServiceImpl(Iti18PortType iti18PortType) {
		this.iti18PortType = iti18PortType;

		Client proxy = ClientProxy.getClient(iti18PortType);
		proxy.getOutInterceptors().add(dgwsSoapDecorator);
	}

	private List<Iti18QueryResponse> createResponse(String patientId, AdhocQueryResponse response) {



		QueryResponseTransformer queryResponseTransformer = new QueryResponseTransformer(getEbXmlFactory());

		EbXMLQueryResponse30 ebXmlresponse = new EbXMLQueryResponse30(response);
		QueryResponse queryResponse = queryResponseTransformer.fromEbXML(ebXmlresponse);

		List<Iti18QueryResponse> result = new LinkedList<>();
		for (DocumentEntry documentEntry : queryResponse.getDocumentEntries()) {


			String documentTypeString = documentEntry.getTypeCode().getDisplayName().toString();

			documentTypeString = documentTypeString.substring(documentTypeString.lastIndexOf('=') + 1, documentTypeString.length() - 1);

			Iti18QueryResponse iti18QueryResponse = new Iti18QueryResponse();
			iti18QueryResponse.setPatientId(patientId);
			iti18QueryResponse.setDocumentId(documentEntry.getUniqueId());
			iti18QueryResponse.setRepositoryID(documentEntry.getRepositoryUniqueId());
			iti18QueryResponse.setDocumentType(documentTypeString);



			if (documentEntry.getServiceStartTime() != null) {
				iti18QueryResponse.setServiceStart(formatTimeForResponse(documentEntry.getServiceStartTime().toString()));}
			if (documentEntry.getServiceStopTime() != null) {
				iti18QueryResponse.setServiceEnd(formatTimeForResponse(documentEntry.getServiceStopTime().toString())); }
			result.add(iti18QueryResponse);
		}
		return result;
	}

	private Long formatTimeForResponse(String time) {
		Instant i = Instant.parse(time);
		return  i.toEpochMilli();
	}

	private EbXMLFactory getEbXmlFactory() {
		return ebXMLFactory;
	}


	private AdhocQueryRequest createQuery(Iti18QueryParameter iti18Request) {

		FindDocumentsQuery fdq = new FindDocumentsQuery();



		// Patient ID
		AssigningAuthority authority = new AssigningAuthority(Codes.DK_CPR_CLASSIFICAION_OID);
		Identifiable patientIdentifiable = new Identifiable(iti18Request.getPatientId(), authority);
		fdq.setPatientId(patientIdentifiable);

		// Availability status
		if (iti18Request.getAvailabilityStatus() != null && iti18Request.getAvailabilityStatus().trim().length() > 0) {
			List<AvailabilityStatus> status = new ArrayList<>();
			status.add(AvailabilityStatus.valueOfOpcode(iti18Request.getAvailabilityStatus()));
			fdq.setStatus(status);
		}

		// Type code
		if (iti18Request.getTypeCode() != null && !iti18Request.getTypeCode().getCode().isEmpty()) {


			fdq.setTypeCodes(getCode(iti18Request.getTypeCode().getCode(), iti18Request.getTypeCode().getCodeScheme()));
		}

		// Format code
		if (iti18Request.getFormatCode() != null && !iti18Request.getFormatCode().getCode().isEmpty()) {

			fdq.setFormatCodes(getCode(iti18Request.getFormatCode().getCode(), iti18Request.getFormatCode().getCodeScheme()));
		}


		// Event code
		if (iti18Request.getEventCode() != null && !iti18Request.getEventCode().getCode().isEmpty() && iti18Request.getEventCode().getCodeScheme() != null && !iti18Request.getEventCode().getCodeScheme().isEmpty()) {

			fdq.setEventCodes(new QueryList<Code>());
			fdq.getEventCodes().getOuterList().add(getCode(iti18Request.getEventCode().getCode(), iti18Request.getEventCode().getCodeScheme()));
		}

		// HealthcareFacilityType code
		if (iti18Request.getHealthcareFacilityTypeCode() != null && !iti18Request.getHealthcareFacilityTypeCode().getCode().isEmpty()) {

			fdq.setHealthcareFacilityTypeCodes(getCode(iti18Request.getHealthcareFacilityTypeCode().getCode(),iti18Request.getHealthcareFacilityTypeCode().getCodeScheme()));
		}


		// Practicesetting code
		if (iti18Request.getPracticeSettingCode() != null && !iti18Request.getPracticeSettingCode().getCode().isEmpty()) {

			fdq.setPracticeSettingCodes(getCode(iti18Request.getPracticeSettingCode().getCode(), iti18Request.getPracticeSettingCode().getCodeScheme()));
		}

		// ServiceStart
		if (iti18Request.getStartFromDate()!= null && !iti18Request.getStartFromDate().toString().isEmpty()) {

			fdq.getServiceStartTime().setFrom(dateFormatterForRequest(iti18Request.getStartFromDate()));
		}

		if (iti18Request.getStartToDate()!= null && !iti18Request.getStartToDate().toString().isEmpty()) {
			fdq.getServiceStartTime().setTo(dateFormatterForRequest(iti18Request.getStartToDate()));

		}
		// ServiceStop

		if (iti18Request.getEndFromDate()!= null && !iti18Request.getEndFromDate().toString().isEmpty()) {
			fdq.getServiceStopTime().setFrom(dateFormatterForRequest(iti18Request.getEndFromDate()));

		}

		if (iti18Request.getEndToDate()!= null && !iti18Request.getEndToDate().toString().isEmpty()) {
			fdq.getServiceStopTime().setTo(dateFormatterForRequest(iti18Request.getEndToDate()));

		}

		// Document Type
		if (iti18Request.getDocumentType().contains("STABLE")) {
			if (fdq.getDocumentEntryTypes() == null) {
				fdq.setDocumentEntryTypes(new LinkedList<>());
			}
			fdq.getDocumentEntryTypes().add(DocumentEntryType.ON_DEMAND);
		}
		if (iti18Request.getDocumentType().contains("ON-DEMAND")) {
			if (fdq.getDocumentEntryTypes() == null) {
				fdq.setDocumentEntryTypes(new LinkedList<>());
			}
			fdq.getDocumentEntryTypes().add(DocumentEntryType.ON_DEMAND);
		}


		QueryRegistry queryRegistry = new QueryRegistry(fdq);
		queryRegistry.setReturnType(QueryReturnType.LEAF_CLASS);

		QueryRegistryTransformer queryRegistryTransformer = new QueryRegistryTransformer();
		EbXMLAdhocQueryRequest ebxmlAdhocQueryRequest = queryRegistryTransformer.toEbXML(queryRegistry);
		AdhocQueryRequest internal = (AdhocQueryRequest)ebxmlAdhocQueryRequest.getInternal();
		return internal;
	}

	private List<Code> getCode(String code, String scheme) {
		List<Code> result = new ArrayList<>();
		Code c = new Code();
		c.setCode(code);
		c.setSchemeName(scheme);

		result.add(c);

		return result;
	}

	private DateTime dateFormatterForRequest(Long date) {
		Instant  i = Instant.ofEpochMilli(date);
		DateTime dt = DateTime.parse(i.toString());

		return dt;

	}

	@Override
	public Iti18Response queryForDocument(Iti18QueryParameter iti18Request, DgwsClientInfo dgwsClientInfo) throws DgwsSecurityException {
		try {
			dgwsSoapDecorator.setDgwsClientInfo(dgwsClientInfo);

			var query = createQuery(iti18Request);
			var response = iti18PortType.documentRegistryRegistryStoredQuery(query);

			List<Iti18QueryResponse> queryResponses = createResponse(iti18Request.getPatientId(), response);

			//Generate Response and request ID



			Iti18Response iti18Response = new Iti18Response();

			iti18Response.setQueryResponse(queryResponses);




			return iti18Response;

		} finally {
			dgwsSoapDecorator.clearSDgwsClientInfo();;
		}
	}
}
