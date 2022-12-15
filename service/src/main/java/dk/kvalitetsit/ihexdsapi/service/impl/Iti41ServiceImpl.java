package dk.kvalitetsit.ihexdsapi.service.impl;

import com.sun.istack.ByteArrayDataSource;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import dk.kvalitetsit.ihexdsapi.service.Iti41Service;
import dk.kvalitetsit.ihexdsapi.service.UploadService;
import dk.kvalitetsit.ihexdsapi.service.model.ProvideAndRegisterDocumentSetRequest;
import dk.kvalitetsit.ihexdsapi.upload_interceptor.KITAttachmentOutInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.jdom2.JDOMException;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.model.*;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLProvideAndRegisterDocumentSetRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;

import javax.activation.DataHandler;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Iti41ServiceImpl implements Iti41Service {

    private Iti41PortType iti41PortType;
    private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();

    private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();

    private UploadService uploadService;

    private String mimeType = "text/xml";

    private final String XDSSubmissionSet_contentTypeCode = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";


    public Iti41ServiceImpl(Iti41PortType iti41PortType, UploadService uploadService) {
        this.iti41PortType = iti41PortType;
        this.uploadService = uploadService;

        Client proxy = ClientProxy.getClient(iti41PortType);
        proxy.getOutInterceptors().add(dgwsSoapDecorator);
        proxy.getOutInterceptors().add(new KITAttachmentOutInterceptor());
    }

    @Override
    public ResponseMetaData setMetaData(Iti41UploadRequest iti41UploadRequest) throws IOException, JDOMException, ItiException {
        ResponseMetaData data = new ResponseMetaData();

        GeneratedMetaData generatedMetaData = uploadService.getGeneratedMetaData(iti41UploadRequest.getXmlInformation());

        // Set meta data from input
        data.setHealthcareFacilityTypeCode(iti41UploadRequest.getResponseMetaData().getHealthcareFacilityTypeCode());
        data.setClassCode(generatedMetaData.getClassCode());
        data.setAvailabilityStatus(iti41UploadRequest.getResponseMetaData().getAvailabilityStatus());
        data.setPracticeSetting(iti41UploadRequest.getResponseMetaData().getPracticeSetting());
        data.setSubmissionTime(iti41UploadRequest.getResponseMetaData().getSubmissionTime());
        data.setObjectType(iti41UploadRequest.getResponseMetaData().getObjectType());
        data.setFormatCode(generatedMetaData.getFormatCode());


        // Set metadata from xml
        data.setUniqueId(generatedMetaData.getUniqueId());


        ResponseMetaDataSourcePatientInfo metaDataSourcePatientInfo = new ResponseMetaDataSourcePatientInfo();

        metaDataSourcePatientInfo.setFamilyName(generatedMetaData.getSourcePatientInfo().getFamilyName());
        metaDataSourcePatientInfo.setGender(ResponseMetaDataSourcePatientInfo.GenderEnum.fromValue(generatedMetaData.getSourcePatientInfo().getGender().toString())
        );
        metaDataSourcePatientInfo.setGivenName(generatedMetaData.getSourcePatientInfo().getGivenName());
        metaDataSourcePatientInfo.setSecondAndFurtherGivenNames(generatedMetaData.getSourcePatientInfo().getSecondAndFurtherGivenNames());
        metaDataSourcePatientInfo.setBirthTime(generatedMetaData.getSourcePatientInfo().getBirthTime());


        data.setSourcePatientInfo(metaDataSourcePatientInfo);
        data.setPatientId(generatedMetaData.getPatientId());
        data.setSourcePatientId(generatedMetaData.getSourcePatientId());
        data.setCreationTime(generatedMetaData.getCreationTime());
        data.setLanguageCode(generatedMetaData.getLanguageCode());
        data.setServiceStartTime(generatedMetaData.getServiceStartTime());
        data.setServiceStopTime(generatedMetaData.getServiceStopTime());
        data.setTitle(generatedMetaData.getTitle());
        data.setAuthorInstitution(generatedMetaData.getAuthorInstitution());
        data.setTypeCode(generatedMetaData.getTypeCode());

        ResponseMetaDataAuthorPerson responseMetaDataAuthorPerson = new ResponseMetaDataAuthorPerson();

        responseMetaDataAuthorPerson.setFamilyName(generatedMetaData.getAuthorPerson().getFamilyName());
        responseMetaDataAuthorPerson.setGivenName(generatedMetaData.getAuthorPerson().getGivenName());
        responseMetaDataAuthorPerson.setSecondAndFurtherGivenNames(generatedMetaData.getAuthorPerson().getSecondAndFurtherGivenNames());

        data.setAuthorPerson(responseMetaDataAuthorPerson);


        data.setEventCode(generatedMetaData.getEventCode());


        if (data.getLegalAuthenticator().isPresent()) {
            data.setLegalAuthenticator(data.getLegalAuthenticator());
        } else {
            data.setLegalAuthenticator(null);
        }


        data.setConfidentialityCode(generatedMetaData.getConfidentialityCode());


        return data;
    }

    @Override
    public Iti41UploadResponse doUpload(Iti41UploadRequest iti41UploadRequest, DgwsClientInfo dgwsClientInfo) throws ItiException {
        dgwsSoapDecorator.setDgwsClientInfo(dgwsClientInfo, false);

        Iti41UploadResponse response = new Iti41UploadResponse();

        ResponseMetaData metaData = null;
        try {
            metaData = setMetaData(iti41UploadRequest);
            ProvideAndRegisterDocumentSetRequest request = buildProvideAndRegisterDocumentSetRequest(iti41UploadRequest.getXmlInformation(), metaData, null);

            // Different iti41TypePorts need to be made in configs to change repo
            RegistryResponseType registryResponse = iti41PortType.documentRepositoryProvideAndRegisterDocumentSetB(request.getProvideAndRegisterDocumentSetRequestType());


            // Add Status of document ( failure or success)
            response.setResultMessage(registryResponse.getStatus().substring(51));

            if (!response.getResultMessage().contains("Failure")) {
                // return document's unique ID
                response.setUniqueId(metaData.getUniqueId());
            } else {
                // Add errors

                List<RegistryError> erList = new ArrayList<>();

                for (org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryError r : registryResponse.getRegistryErrorList().getRegistryError()) {
                    RegistryError er = new RegistryError();
                    er.setErrorCode(r.getErrorCode());
                    er.setCodeContext(r.getCodeContext());

                    if (r.getSeverity().substring(50).contains("ERROR")) {
                        er.setSeverity(RegistryError.SeverityEnum.ERROR);
                    } else {
                        er.setSeverity(RegistryError.SeverityEnum.WARNING);

                    }

                    erList.add(er);
                }
                response.setErrors(erList);
                throw new ItiException(1000, "Failed to retrieve document", erList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JDOMException e) {
            throw new RuntimeException(e);

        } finally {
            dgwsSoapDecorator.clearSDgwsClientInfo();
        }


        return response;

    }

    @Override
    public GeneratedMetaData getGeneratedMetaData(String xml) throws IOException, JDOMException, ItiException {
        return uploadService.getGeneratedMetaData(xml);
    }


    /**
     * This the where the metadata and document gets combined And forms the request-set
     */
    private ProvideAndRegisterDocumentSetRequest buildProvideAndRegisterDocumentSetRequest(String documentPayload, ResponseMetaData documentMetadata, String entryUuidToReplace) {


        // This might be changeable in the future
        AvailabilityStatus standardStatus = AvailabilityStatus.APPROVED;


        ProvideAndRegisterDocumentSet provideAndRegisterDocumentSet = new ProvideAndRegisterDocumentSet();

        Identifiable patientIdentifiable = null;
        if (documentMetadata.getPatientId() != null) {
            AssigningAuthority patientIdAssigningAuthority = new AssigningAuthority(documentMetadata.getPatientId().getScheme());
            patientIdentifiable = new Identifiable(documentMetadata.getPatientId().getCode(), patientIdAssigningAuthority);
        }
        Identifiable sourcePatientIdentifiable = null;
        if (documentMetadata.getSourcePatientId() != null) {
            AssigningAuthority patientIdAssigningAuthority = new AssigningAuthority(documentMetadata.getSourcePatientId().getScheme());
            sourcePatientIdentifiable = new Identifiable(documentMetadata.getPatientId().getCode(), patientIdAssigningAuthority);
        }

        //author.authorInstitution - organization
        Author author = new Author();
        if (documentMetadata.getAuthorInstitution() != null && documentMetadata.getAuthorInstitution().getCode() != null && documentMetadata.getAuthorInstitution().getScheme() != null) {
            AssigningAuthority organisationAssigningAuthority = new AssigningAuthority(documentMetadata.getAuthorInstitution().getScheme());
            Organization authorOrganisation = new Organization(documentMetadata.getAuthorInstitution().getName(), documentMetadata.getAuthorInstitution().getCode(), organisationAssigningAuthority);
            author.getAuthorInstitution().add(authorOrganisation);
        }

        //author.authorperson
        if (documentMetadata.getAuthorPerson() != null && (documentMetadata.getAuthorPerson().getFamilyName() != null || documentMetadata.getAuthorPerson().getGivenName() != null)) {
            Name<?> authorName = new XcnName();

            if (documentMetadata.getAuthorPerson().getFamilyName() != null) {
                authorName.setFamilyName(documentMetadata.getAuthorPerson().getFamilyName());
            }
            if (documentMetadata.getAuthorPerson().getGivenName() != null) {
                authorName.setGivenName(documentMetadata.getAuthorPerson().getGivenName());
            }
            if (documentMetadata.getAuthorPerson().getSecondAndFurtherGivenNames() != null) {
                authorName.setSecondAndFurtherGivenNames(documentMetadata.getAuthorPerson().getSecondAndFurtherGivenNames());
            }
            Person authorPerson = new Person();
            authorPerson.setName(authorName);
            author.setAuthorPerson(authorPerson);
        }

        String submissionSetUuid = generateUUID();
        String submissionSetId = generateUUIDAdjusted();
        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setUniqueId(submissionSetId);
        submissionSet.setSourceId(submissionSetId);
        submissionSet.setLogicalUuid(submissionSetUuid);
        submissionSet.setEntryUuid(submissionSetUuid);
        submissionSet.setPatientId(patientIdentifiable);
        submissionSet.setTitle(new LocalizedString(submissionSetUuid));
        submissionSet.setAuthor(author);

        submissionSet.setContentTypeCode(new Code("NscContentType", new LocalizedString("NscContentType"), this.XDSSubmissionSet_contentTypeCode));
        //submissionSet.setContentTypeCode(createCode(documentMetadata.getContentTypeCode()));

        //submissionTime
        if (documentMetadata.getSubmissionTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            submissionSet.setSubmissionTime(formatter.format(documentMetadata.getSubmissionTime()));
        }

        // TODO Add availabilityStatus to generated Is this necessary
        //i availabilityStatus
        submissionSet.setAvailabilityStatus(standardStatus);
        provideAndRegisterDocumentSet.setSubmissionSet(submissionSet);

        String documentUuid = generateUUID();

        DocumentEntry documentEntry = new DocumentEntry();
        // 4.1 Patient Identification
        //patientId
        documentEntry.setPatientId(patientIdentifiable);
        //sourcePatientId
        documentEntry.setSourcePatientId(sourcePatientIdentifiable);

        // 4.2 Name, Address and Telecommunications
        //sourcePatientInfo
        PatientInfo sourcePatientInfo = new PatientInfo();
        documentEntry.setSourcePatientInfo(sourcePatientInfo);

        // 4.2.1 Name
        if (documentMetadata.getSourcePatientInfo() != null && (documentMetadata.getSourcePatientInfo().getFamilyName() != null) || documentMetadata.getSourcePatientInfo().getGivenName() != null) {
            Name<?> name = new XpnName();
            if (documentMetadata.getSourcePatientInfo().getFamilyName() != null) {
                name.setFamilyName(documentMetadata.getSourcePatientInfo().getFamilyName());
            }
            if (documentMetadata.getSourcePatientInfo().getGivenName() != null) {
                name.setGivenName(documentMetadata.getSourcePatientInfo().getGivenName());
            }
            if (documentMetadata.getSourcePatientInfo().getSecondAndFurtherGivenNames() != null) {
                name.setSecondAndFurtherGivenNames(documentMetadata.getSourcePatientInfo().getSecondAndFurtherGivenNames());
            }
            sourcePatientInfo.setName(name);
        }

        if (documentMetadata.getSourcePatientInfo().getGender() != null) {
            sourcePatientInfo.setGender(documentMetadata.getSourcePatientInfo().getGender().getValue());
        }
        if (documentMetadata.getSourcePatientInfo().getBirthTime() != null) {

            sourcePatientInfo.setDateOfBirth(documentMetadata.getSourcePatientInfo().getBirthTime());

        }


        documentEntry.setEntryUuid(generateUUID());
        documentEntry.setAuthor(author);
        documentEntry.setAvailabilityStatus(standardStatus);

        //i classCode
        if (documentMetadata.getClassCode() != null) {
            // TODO MAngler localized string eller lignende
            LocalizedString classCodeName = documentMetadata.getClassCode().getName() != null ? new LocalizedString(documentMetadata.getClassCode().getName()) : new LocalizedString(documentMetadata.getClassCode().getCode());
            Code classCode = new Code(documentMetadata.getClassCode().getCode(), classCodeName, documentMetadata.getClassCode().getScheme());
            documentEntry.setClassCode(classCode);
        }

        //confidentialityCode
        if (documentMetadata.getConfidentialityCode() != null) {
            // Code name is most likely null
            LocalizedString confidentialityName = documentMetadata.getConfidentialityCode().getName() != null ? new LocalizedString(documentMetadata.getConfidentialityCode().getName()) : new LocalizedString(documentMetadata.getConfidentialityCode().getCode());
            Code confidentialityCode = new Code(documentMetadata.getConfidentialityCode().getCode(), confidentialityName, documentMetadata.getConfidentialityCode().getScheme());
            documentEntry.getConfidentialityCodes().add(confidentialityCode);
        }
        //creationTime
        if (documentMetadata.getCreationTime() != null) {
            documentEntry.setCreationTime(documentMetadata.getCreationTime());
        }

        //eventCodedList
        List<Code> eventCodesEntry = documentEntry.getEventCodeList();
        if (documentMetadata.getEventCode() != null) {
            for (org.openapitools.model.Code eventCode : documentMetadata.getEventCode()) {
                LocalizedString eventCodeName = eventCode.getName() != null ? new LocalizedString(eventCode.getName()) : new LocalizedString(eventCode.getCode());
                Code eventCodeTemp = new Code(eventCode.getCode(), eventCodeName, eventCode.getScheme());
                eventCodesEntry.add(eventCodeTemp);
            }
        }
        //i formatCode
        if (documentMetadata.getFormatCode() != null) {
            documentEntry.setFormatCode(makeCodes(documentMetadata.getFormatCode().getName(),
                    documentMetadata.getFormatCode().getCode(), documentMetadata.getFormatCode().getScheme()));

            LocalizedString formatCodeName = documentMetadata.getFormatCode().getName() != null ? new LocalizedString(documentMetadata.getFormatCode().getName()) : new LocalizedString(documentMetadata.getFormatCode().getCode());
            Code formatCode = new Code(documentMetadata.getFormatCode().getCode(), formatCodeName, documentMetadata.getFormatCode().getScheme());
            documentEntry.setFormatCode(formatCode);

        }
        //i healthcareFacilityTypeCode
        if (documentMetadata.getHealthcareFacilityTypeCode() != null) {
            LocalizedString healthcareName = documentMetadata.getHealthcareFacilityTypeCode().getName() != null ? new LocalizedString(documentMetadata.getHealthcareFacilityTypeCode().getName()) : new LocalizedString(documentMetadata.getHealthcareFacilityTypeCode().getCode());
            Code healthcareCode = new Code(documentMetadata.getHealthcareFacilityTypeCode().getCode(), healthcareName, documentMetadata.getHealthcareFacilityTypeCode().getScheme());
            documentEntry.setHealthcareFacilityTypeCode(healthcareCode);

        }
        //LanguageCode
        if (documentMetadata.getLanguageCode() != null) {
            documentEntry.setLanguageCode(documentMetadata.getLanguageCode());
        }
        // Mimetype
        documentEntry.setMimeType(mimeType); //mimetype is included because some repositories required this to work. Example is KIH
/*
        // objectType
        if (documentMetadata.getObjectType() != null) {
            documentEntry.setType(DocumentEntryType.valueOf(documentMetadata.getObjectType()));

            Code objectType = new Code(documentMetadata.getObjectType().getCode(), new LocalizedString(documentMetadata.getObjectType().getName()), documentMetadata.getObjectType().getScheme());
            documentEntry.setOb

        }*/

        //title
        if (documentMetadata.getTitle() != null) {
            documentEntry.setTitle(new LocalizedString(documentMetadata.getTitle()));
        }
        //typeCode
        if (documentMetadata.getTypeCode() != null) {
            Code typeCode = new Code(documentMetadata.getTypeCode().getCode(), new LocalizedString(documentMetadata.getTypeCode().getName()), documentMetadata.getTypeCode().getScheme());
            documentEntry.setTypeCode(typeCode);
        }
        //i practiceSettingCode
        if (documentMetadata.getPracticeSetting() != null) {

            LocalizedString practiceCodeName = documentMetadata.getPracticeSetting().getName() != null ? new LocalizedString(documentMetadata.getPracticeSetting().getName()) : new LocalizedString(documentMetadata.getPracticeSetting().getCode());
            Code practiceCode = new Code(documentMetadata.getPracticeSetting().getCode(), practiceCodeName, documentMetadata.getPracticeSetting().getScheme());
            documentEntry.setPracticeSettingCode(practiceCode);

        }

        //uniqeId
        String extenalDocumentId = null;
        if (documentMetadata.getUniqueId() != null) {
            extenalDocumentId = documentMetadata.getUniqueId();
        }
        documentEntry.setUniqueId(extenalDocumentId);
        documentEntry.setLogicalUuid(documentUuid);


        //legalAuthenticator
        if (documentMetadata.getLegalAuthenticator() != null) {
            if ((documentMetadata.getLegalAuthenticator().get().getFamilyName() != null || documentMetadata.getLegalAuthenticator().get().getGivenName() != null)) {
                Name<?> legalAuthenticatorName = new XcnName();
                if (documentMetadata.getLegalAuthenticator().get().getFamilyName() != null) {
                    legalAuthenticatorName.setFamilyName(documentMetadata.getLegalAuthenticator().get().getFamilyName());
                }
                if (documentMetadata.getLegalAuthenticator().get().getGivenName() != null) {
                    legalAuthenticatorName.setGivenName(documentMetadata.getLegalAuthenticator().get().getGivenName());
                }
                if (documentMetadata.getLegalAuthenticator().get().getSecondAndFurtherGivenNames() != null) {
                    legalAuthenticatorName.setSecondAndFurtherGivenNames(documentMetadata.getLegalAuthenticator().get().getSecondAndFurtherGivenNames());
                }
                Person legalAuthenticatorPerson = new Person();
                legalAuthenticatorPerson.setName(legalAuthenticatorName);
                documentEntry.setLegalAuthenticator(legalAuthenticatorPerson);
            }
        }


        //serviceStartTime
        if (documentMetadata.getServiceStartTime() != null) {
            documentEntry.setServiceStartTime(documentMetadata.getServiceStartTime());
        }


        //serviceStopTime
        if (documentMetadata.getServiceStopTime() != null) {
            documentEntry.setServiceStopTime(documentMetadata.getServiceStopTime());
        }


        Document document = new Document(documentEntry, new DataHandler(new ByteArrayDataSource(documentPayload.getBytes(), mimeType)));
        provideAndRegisterDocumentSet.getDocuments().add(document);

        Association association = new Association();
        association.setAssociationType(AssociationType.HAS_MEMBER);
        association.setEntryUuid(generateUUID());
        association.setSourceUuid(submissionSet.getEntryUuid());
        association.setTargetUuid(documentEntry.getEntryUuid());
        association.setAvailabilityStatus(standardStatus);
        association.setOriginalStatus(standardStatus);
        association.setNewStatus(standardStatus);
        association.setLabel(AssociationLabel.ORIGINAL);
        provideAndRegisterDocumentSet.getAssociations().add(association);

        if (entryUuidToReplace != null) {
            Association replacementAssociation = new Association(AssociationType.REPLACE, generateUUID(), documentEntry.getEntryUuid(), entryUuidToReplace);
            provideAndRegisterDocumentSet.getAssociations().add(replacementAssociation);
        }

        ProvideAndRegisterDocumentSetTransformer registerDocumentSetTransformer = new ProvideAndRegisterDocumentSetTransformer(getEbXmlFactory());
        EbXMLProvideAndRegisterDocumentSetRequest30 ebxmlRequest = (EbXMLProvideAndRegisterDocumentSetRequest30) registerDocumentSetTransformer.toEbXML(provideAndRegisterDocumentSet);
        ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequestType = ebxmlRequest.getInternal();

        ProvideAndRegisterDocumentSetRequest provideAndRegisterDocumentSetRequest = new ProvideAndRegisterDocumentSetRequest(extenalDocumentId, provideAndRegisterDocumentSetRequestType);
        return provideAndRegisterDocumentSetRequest;


    }

    private EbXMLFactory getEbXmlFactory() {
        return ebXMLFactory;
    }

    private String generateUUID() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        return uuid.toString();
    }

    private String generateUUIDAdjusted() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        return Math.abs(uuid.getLeastSignificantBits()) + "." + Math.abs(uuid.getMostSignificantBits()) + "." + Calendar.getInstance().getTimeInMillis();
    }

    /*private org.openapitools.model.Code makeCodeObject(String name, String code, String codeScheme) {
        org.openapitools.model.Code c = new org.openapitools.model.Code();
        c.setName(name);
        c.setScheme(codeScheme);
        c.setCode(code);
        return c;
    }*/

    private org.openehealth.ipf.commons.ihe.xds.core.metadata.Code makeCodes(String name, String code, String codeScheme) {
        org.openehealth.ipf.commons.ihe.xds.core.metadata.Code c = new Code();
        c.setCode(code);
        c.setSchemeName(codeScheme);
        c.setSchemeName(name);

        return c;
    }
}


