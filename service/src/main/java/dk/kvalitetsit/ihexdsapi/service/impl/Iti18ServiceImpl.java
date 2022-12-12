package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import dk.kvalitetsit.ihexdsapi.interceptors.SoapFaultCatcherInterceptor;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.upload_interceptor.KITAttachmentOutInterceptor;
import dk.kvalitetsit.ihexdsapi.xds.Codes;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.joda.time.DateTime;
import org.openapitools.model.*;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLQueryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.*;
import java.util.*;

public class Iti18ServiceImpl implements Iti18Service {

    private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();

    private Iti18PortType iti18PortType;


    private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();

    private SoapFaultCatcherInterceptor soapFaultCatcherInterceptor = new SoapFaultCatcherInterceptor();

    public Iti18ServiceImpl(Iti18PortType iti18PortType) {
        this.iti18PortType = iti18PortType;

        Client proxy = ClientProxy.getClient(iti18PortType);
        proxy.getOutInterceptors().add(dgwsSoapDecorator);
        proxy.getInInterceptors().add(soapFaultCatcherInterceptor);
    }

    private Iti18Response populateIti18Response(String patientId, AdhocQueryResponse response, Iti18Response iti18Response) {


        QueryResponseTransformer queryResponseTransformer = new QueryResponseTransformer(getEbXmlFactory());

        EbXMLQueryResponse30 ebXmlresponse = new EbXMLQueryResponse30(response);
        QueryResponse queryResponse = queryResponseTransformer.fromEbXML(ebXmlresponse);


        // Query parameters
        List<Iti18QueryResponse> queryResponses = new LinkedList<>();
        for (DocumentEntry documentEntry : queryResponse.getDocumentEntries()) {


            String documentTypeString = documentEntry.getTypeCode().getDisplayName().toString();

            documentTypeString = documentTypeString.substring(documentTypeString.lastIndexOf('=') + 1, documentTypeString.length() - 1);

            Iti18QueryResponse iti18QueryResponse = new Iti18QueryResponse();
            iti18QueryResponse.setPatientId(patientId);
            iti18QueryResponse.setDocumentId(documentEntry.getUniqueId());
            iti18QueryResponse.setRepositoryID(documentEntry.getRepositoryUniqueId());
            iti18QueryResponse.setDocumentType(documentTypeString);


            if (documentEntry.getServiceStartTime() != null) {
                iti18QueryResponse.setServiceStart(formatTimeForResponse(documentEntry.getServiceStartTime().toString()));
            }
            if (documentEntry.getServiceStopTime() != null) {
                iti18QueryResponse.setServiceEnd(formatTimeForResponse(documentEntry.getServiceStopTime().toString()));
            }
            queryResponses.add(iti18QueryResponse);
        }

        // set errors
        List<RegistryError> errors = new LinkedList<>();
        for (ErrorInfo errorInfo : queryResponse.getErrors()) {
            RegistryError registryError = new RegistryError();

            registryError.setCodeContext(errorInfo.getCodeContext());
            registryError.setErrorCode(errorInfo.getErrorCode().getOpcode() + " , " + errorInfo.getErrorCode().name());

            if (errorInfo.getSeverity().name().contains("ERROR")) {
                registryError.setSeverity(RegistryError.SeverityEnum.ERROR);
            } else {
                registryError.setSeverity(RegistryError.SeverityEnum.WARNING);

            }

            errors.add(registryError);
        }


        iti18Response.setQueryResponse(queryResponses);
        iti18Response.setErrors(errors);

        return iti18Response;
    }

    private Long formatTimeForResponse(String time) {
        Instant i = Instant.parse(time);
        return i.toEpochMilli();
    }

    private EbXMLFactory getEbXmlFactory() {
        return ebXMLFactory;
    }


    private AdhocQueryRequest createQuery(Iti18QueryParameter iti18Request) throws ItiException {

        FindDocumentsQuery fdq = new FindDocumentsQuery();


        // Patient ID
        if (iti18Request.getPatientId() != null && !iti18Request.getPatientId().isEmpty()) {
            AssigningAuthority authority = new AssigningAuthority(Codes.DK_CPR_CLASSIFICAION_OID);
            Identifiable patientIdentifiable = new Identifiable(iti18Request.getPatientId(), authority);
            fdq.setPatientId(patientIdentifiable);
        } else {
            throw new ItiException(1000, "Patient-ID is empty", null);
        }

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

            fdq.setHealthcareFacilityTypeCodes(getCode(iti18Request.getHealthcareFacilityTypeCode().getCode(), iti18Request.getHealthcareFacilityTypeCode().getCodeScheme()));
        }


        // Practicesetting code
        if (iti18Request.getPracticeSettingCode() != null && !iti18Request.getPracticeSettingCode().getCode().isEmpty()) {

            fdq.setPracticeSettingCodes(getCode(iti18Request.getPracticeSettingCode().getCode(), iti18Request.getPracticeSettingCode().getCodeScheme()));
        }

        // ServiceStart
        if (iti18Request.getStartFromDate() != null && !iti18Request.getStartFromDate().toString().isEmpty()) {

            fdq.getServiceStartTime().setFrom(dateFormatterForRequest(iti18Request.getStartFromDate()));
        }

        if (iti18Request.getStartToDate() != null && !iti18Request.getStartToDate().toString().isEmpty()) {
            fdq.getServiceStartTime().setTo(dateFormatterForRequest(iti18Request.getStartToDate()));

        }
        // ServiceStop

        if (iti18Request.getEndFromDate() != null && !iti18Request.getEndFromDate().toString().isEmpty()) {
            fdq.getServiceStopTime().setFrom(dateFormatterForRequest(iti18Request.getEndFromDate()));

        }

        if (iti18Request.getEndToDate() != null && !iti18Request.getEndToDate().toString().isEmpty()) {
            fdq.getServiceStopTime().setTo(dateFormatterForRequest(iti18Request.getEndToDate()));

        }

        // Document Type
        if (iti18Request.getDocumentType() == null) {

            throw new ItiException((Exception) new NullPointerException().getCause(), 1000, "List is null");
        }
        if (iti18Request.getDocumentType().contains("STABLE")) {
            if (fdq.getDocumentEntryTypes() == null) {
                fdq.setDocumentEntryTypes(new LinkedList<>());
            }
            fdq.getDocumentEntryTypes().add(DocumentEntryType.STABLE);
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

        AdhocQueryRequest internal = (AdhocQueryRequest) ebxmlAdhocQueryRequest.getInternal();

        return internal;
    }


    private AdhocQueryRequest buildAdhocQueryRequest(String documentId) {
        List<String> uniqueIds = new LinkedList<String>();
        uniqueIds.add(documentId);

        GetDocumentsQuery gdq = new GetDocumentsQuery();
        gdq.setUniqueIds(uniqueIds);

        return createAdhocQueryRequest(gdq, QueryReturnType.LEAF_CLASS);
    }

    private AdhocQueryRequest createAdhocQueryRequest(Query query, QueryReturnType qrt) {
        QueryRegistry queryRegistry = new QueryRegistry(query);
        if (qrt != null) {
            queryRegistry.setReturnType(qrt);
        }
        QueryRegistryTransformer queryRegistryTransformer = new QueryRegistryTransformer();
        EbXMLAdhocQueryRequest ebxmlAdhocQueryRequest = queryRegistryTransformer.toEbXML(queryRegistry);
        AdhocQueryRequest internal = (AdhocQueryRequest) ebxmlAdhocQueryRequest.getInternal();

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
        Instant i = Instant.ofEpochMilli(date);
        DateTime dt = DateTime.parse(i.toString());

        return dt;

    }

    @Override
    public Iti18Response queryForDocument(Iti18QueryParameter iti18Request, DgwsClientInfo dgwsClientInfo) throws DgwsSecurityException, ItiException {
        try {

            dgwsSoapDecorator.setDgwsClientInfo(dgwsClientInfo, true);

            var query = createQuery(iti18Request);
            var response = iti18PortType.documentRegistryRegistryStoredQuery(query);


            Iti18Response iti18Response = populateIti18Response(iti18Request.getPatientId(), response, new Iti18Response());
            return iti18Response;
        }
            finally
         {
            dgwsSoapDecorator.clearSDgwsClientInfo();

        }
    }

    @Override
    public Iti18ResponseUnique queryForDocument(Iti18RequestUnique iti18RequestUnique, DgwsClientInfo dgwsClientInfo) throws DgwsSecurityException {
        try {
            dgwsSoapDecorator.setDgwsClientInfo(dgwsClientInfo, true);


            // TODO Change to actual document ID
            var query = buildAdhocQueryRequest(
                    iti18RequestUnique.getQueryParameters().getDocumentId());
           /*var query = buildAdhocQueryRequest(
                    "1.2.208.184^1232b492-72d0-4789-9bea-ff829f7e0114");*/
            var response = iti18PortType.documentRegistryRegistryStoredQuery(query);


            Iti18ResponseUnique metaDataResponse = populateUniqueIti18Response(iti18RequestUnique.getQueryParameters()
                    .getPatientId(), response, new Iti18ResponseUnique());


            return metaDataResponse;

        } finally {
            dgwsSoapDecorator.clearSDgwsClientInfo();
            ;
        }
    }

    private Iti18ResponseUnique populateUniqueIti18Response(String patientID, AdhocQueryResponse response, Iti18ResponseUnique metaDataResponse) {
        QueryResponseTransformer queryResponseTransformer = new QueryResponseTransformer(getEbXmlFactory());

        EbXMLQueryResponse30 ebXmlresponse = new EbXMLQueryResponse30(response);
        QueryResponse queryResponse = queryResponseTransformer.fromEbXML(ebXmlresponse);

        // Set document data

        if (!queryResponse.getDocumentEntries().isEmpty()) {
            DocumentEntry entry = queryResponse.getDocumentEntries().get(0);

            String authorP = "" + entry.getAuthor().getAuthorPerson().getName().getGivenName() + " " + entry.getAuthor().getAuthorPerson().getName().getFamilyName() + ", " + entry.getAuthor().getAuthorPerson().getName().getPrefix();
            metaDataResponse.setAuthorPerson(authorP);


            String orgName = "";
            String orgCodeScheme = "";
            String orgCode = "";
            for (Organization o : entry.getAuthor().getAuthorInstitution()) {
                orgName = orgName + o.getOrganizationName() + ",";
                orgCode = orgCode + o.getIdNumber() + ",";
                // TODO Need to confirm
                orgCodeScheme = orgCodeScheme + o.getAssigningAuthority().toString() + ",";
            }
            metaDataResponse.setAuthorInstitution(makeCodeObject(orgName, orgCode, orgCodeScheme));
            metaDataResponse.setAvailabilityStatus(makeCodeObject(entry.getAvailabilityStatus().name(),
                    entry.getAvailabilityStatus().getOpcode(), entry.getAvailabilityStatus().getQueryOpcode()));
            metaDataResponse.setClassCode(makeCodeObject(entry.getClassCode().getDisplayName().getValue(), entry.getClassCode().getCode(), entry.getClassCode().getSchemeName()));

            if (entry.getComments() != null) {
                metaDataResponse.setComments(entry.getComments().getValue());
            }
            String confidentialityName = "";
            String confidentialityCodeScheme = "";
            String confidentialityCode = "";
            for (Code c : entry.getConfidentialityCodes()) {
                confidentialityName = confidentialityName + c.getDisplayName().getValue();
                confidentialityCodeScheme = confidentialityCodeScheme + c.getCode();
                confidentialityCode = confidentialityCode + c.getSchemeName();
            }
            metaDataResponse.setConfidentialityCode(makeCodeObject(confidentialityName, confidentialityCode,
                    confidentialityCodeScheme));
            if (entry.getCreationTime() != null) {
                metaDataResponse.setCreationTime(formatTimeForResponse(entry.getCreationTime().toString()));
            }
            if (entry.getDocumentAvailability() != null) {
                metaDataResponse.setAvailabilityStatus(makeCodeObject(entry.getDocumentAvailability().name(),
                        entry.getDocumentAvailability().getOpcode(), entry.getDocumentAvailability().getFullQualified()));
            }

            metaDataResponse.setEntryUuid(entry.getEntryUuid());

            LinkedList<org.openapitools.model.Code> eventCode = new LinkedList<>();

            System.out.println(entry.getEventCodeList());
            for (Code c : entry.getEventCodeList()) {
                eventCode.add(makeCodeObject(c.getDisplayName().getValue(), c.getCode(), c.getSchemeName()));
            }
            metaDataResponse.setEventCode(eventCode);

            if (entry.getExtraMetadata() != null) {
                String extraData = "";
                for (Map.Entry<String, java.util.List<String>> e : entry.getExtraMetadata().entrySet()) {
                    extraData = "Key: " + e.getKey() + " Value: " + Arrays.toString(e.getValue().toArray()) + "\n";
                }

                metaDataResponse.setExtraMetadata(extraData);
            }

            metaDataResponse.setFormatCode(makeCodeObject(entry.getFormatCode().getDisplayName().getValue(),
                    entry.getFormatCode().getCode(), entry.getFormatCode().getSchemeName()));

            metaDataResponse.setHash(entry.getHash());

            metaDataResponse.setHealthCareFacilityType(makeCodeObject(entry.getHealthcareFacilityTypeCode()
                            .getDisplayName().getValue(), entry.getHealthcareFacilityTypeCode().getCode(),
                    entry.getHealthcareFacilityTypeCode().getSchemeName()));

            metaDataResponse.setHomeComunity(entry.getHomeCommunityId());

            metaDataResponse.setLanguageCode(entry.getLanguageCode());


            if (entry.getLegalAuthenticator() != null) {
                metaDataResponse.setLegalAuthenticator(entry.getLegalAuthenticator().getName().getGivenName() + " "
                        + entry.getLegalAuthenticator().getName().getFamilyName());
            }


            metaDataResponse.setLogicalUuid(entry.getLogicalUuid());
            metaDataResponse.setMimeType(entry.getMimeType());

            //
            metaDataResponse.setObjectType(entry.getType().toString());

            metaDataResponse.setPracticeSettingCode(makeCodeObject(entry.getPracticeSettingCode().getDisplayName().getValue(),
                    entry.getPracticeSettingCode().getCode(), entry.getPracticeSettingCode().getSchemeName()));

            metaDataResponse.setRepositoryUniqueId(entry.getRepositoryUniqueId());

            if (entry.getServiceStartTime() != null) {
                metaDataResponse.setServiceStartTime(formatTimeForResponse(entry.getServiceStartTime().toString()));
            }
            if (entry.getServiceStopTime() != null) {
                metaDataResponse.setServiceStopTime(formatTimeForResponse(entry.getServiceStopTime().toString()));
            }

            metaDataResponse.setSize(entry.getSize().intValue());

            metaDataResponse.setSourcePatientId(makeCodeObject(null, entry.getPatientId().getId(), entry.getPatientId().getAssigningAuthority().getUniversalId()));


            Iti18ResponseUniqueSourcePatientInfo patientInfo = new Iti18ResponseUniqueSourcePatientInfo();
            patientInfo.setName(entry.getSourcePatientInfo().getName().getGivenName() + " " + entry.getSourcePatientInfo().getName().getFamilyName());
            patientInfo.setGender(Iti18ResponseUniqueSourcePatientInfo.GenderEnum.fromValue(entry.getSourcePatientInfo().getGender()));
            patientInfo.setBirthTime(formatTimeForResponse(entry.getSourcePatientInfo().getDateOfBirth().toString()));

            metaDataResponse.setSourcePatientInfo(patientInfo);
            /// Source patientinfo

            metaDataResponse.setTitle(entry.getTitle().getValue());

            //TODO Figure out the correct value
            metaDataResponse.setType(makeCodeObject(entry.getType().name(), entry.getType().name(), entry.getType().getUuid()));


            metaDataResponse.setTypeCode(makeCodeObject(entry.getTypeCode().getDisplayName().getValue(),
                    entry.getTypeCode().getCode(), entry.getTypeCode().getSchemeName()));
            metaDataResponse.setUniqueId(entry.getUniqueId());

            metaDataResponse.setUri(entry.getUri());

            metaDataResponse.setVersion(entry.getVersion().getVersionName());

        }


        // set errors
        List<RegistryError> errors = new LinkedList<>();
        for (ErrorInfo errorInfo : queryResponse.getErrors()) {
            RegistryError registryError = new RegistryError();

            registryError.setCodeContext(errorInfo.getCodeContext());
            registryError.setErrorCode(errorInfo.getErrorCode().getOpcode() + " , " + errorInfo.getErrorCode().name());
            registryError.setCustomErrorCode(errorInfo.getCustomErrorCode());
            if (errorInfo.getSeverity().name().contains("ERROR")) {
                registryError.setSeverity(RegistryError.SeverityEnum.ERROR);
            } else {
                registryError.setSeverity(RegistryError.SeverityEnum.WARNING);

            }

            errors.add(registryError);
        }

        metaDataResponse.errors(errors);

        return metaDataResponse;
    }

    //TODO refactor in one class
    private org.openapitools.model.Code makeCodeObject(String name, String code, String codeScheme) {
        org.openapitools.model.Code c = new org.openapitools.model.Code();
        c.setName(name);
        c.setScheme(codeScheme);
        c.setCode(code);
        return c;
    }
}
