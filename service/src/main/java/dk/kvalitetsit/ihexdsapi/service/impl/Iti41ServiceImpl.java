package dk.kvalitetsit.ihexdsapi.service.impl;

import com.sun.istack.ByteArrayDataSource;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.service.Iti41Service;
import dk.kvalitetsit.ihexdsapi.service.UploadService;
import dk.kvalitetsit.ihexdsapi.service.decorators.CxfContentIDDecorator;
import dk.kvalitetsit.ihexdsapi.service.model.ProvideAndRegisterDocumentSetRequest;
import dk.kvalitetsit.ihexdsapi.upload_interceptor.KITAttachmentOutInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.jdom2.JDOMException;
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
import java.util.Calendar;
import java.util.List;

public class Iti41ServiceImpl implements Iti41Service {

    private Iti41PortType iti41PortType;
    private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();

    private CxfContentIDDecorator contentIDDecorator = new CxfContentIDDecorator();
    private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();

    private UploadService uploadService;

    private String mimeType = "text/xml";

    private final String XDSSubmissionSet_contentTypeCode       = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";


    public Iti41ServiceImpl(Iti41PortType iti41PortType, UploadService uploadService) {
        this.iti41PortType = iti41PortType;
        this.uploadService = uploadService;

        Client proxy = ClientProxy.getClient(iti41PortType);
        proxy.getOutInterceptors().add(dgwsSoapDecorator);
        //proxy.getOutInterceptors().add(contentIDDecorator);
        proxy.getOutInterceptors().add(new KITAttachmentOutInterceptor());
    }

    @Override
    public ResponseMetaData setMetaData(String xml) {
        ResponseMetaData data = new ResponseMetaData();

        data.setUniqueId("63ee-eeee-eeee-eee");

        GeneratedMetaDataSourcePatientInfo patientInfo = new GeneratedMetaDataSourcePatientInfo();

        patientInfo.setGivenName("Emil");
        patientInfo.setFamilyName("larsen");
        data.setSourcePatientInfo(patientInfo);

        data.setCreationTime("20221116100313");

        data.setLanguageCode("da-DK");

        data.setServiceStartTime("20221116100313");

        data.setServiceStopTime("20221116100313");


        data.setPatientId(new org.openapitools.model.Code());
        data.getPatientId().setCode("2906910651");
        data.getPatientId().setScheme("1.2.208.176.1.2");


        return data;
    }

    @Override
    public Iti41UploadResponse doUpload(String xmlPayload, DgwsClientInfo dgwsClientInfo) {
        dgwsSoapDecorator.setDgwsClientInfo(dgwsClientInfo, false);

        Iti41UploadResponse response = new Iti41UploadResponse();

        ResponseMetaData metaData = setMetaData(xmlPayload);
        //System.out.println(xmlPayload);
        //System.out.println();
        ProvideAndRegisterDocumentSetRequest request =  buildProvideAndRegisterDocumentSetRequest(xmlPayload, metaData, null);

        // Different iti41TypePorts need to be made in configs to change repo
        RegistryResponseType registryResponse = iti41PortType.documentRepositoryProvideAndRegisterDocumentSetB(request.getProvideAndRegisterDocumentSetRequestType());

        System.out.println(registryResponse);


        dgwsSoapDecorator.clearSDgwsClientInfo();
        return response;
    }

    @Override
    public GeneratedMetaData getGeneratedMetaData(String xml) throws IOException, JDOMException {
     return uploadService.getGeneratedMetaData(xml);
    }



    /**
     * This the where the metadata and document gets combined And forms the requestset
     */
    private ProvideAndRegisterDocumentSetRequest buildProvideAndRegisterDocumentSetRequest(String documentPayload, ResponseMetaData documentMetadata, String entryUuidToReplace) {

       // DateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

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

        //i submessionTime
        // CreationTime
        if (documentMetadata.getCreationTime() != null) {
            submissionSet.setSubmissionTime(documentMetadata.getCreationTime());
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

        if (documentMetadata.getSourcePatientInfo().getGender()!= null) {
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
            documentEntry.setClassCode(makeCodes(documentMetadata.getClassCode().getName(),
                    documentMetadata.getClassCode().getCode(), documentMetadata.getClassCode().getScheme()));
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
                eventCodesEntry.add(makeCodes(eventCode.getName(), eventCode.getCode(), eventCode.getScheme()));
            }
        }
        //i formatCode
        if (documentMetadata.getFormatCode() != null) {
            documentEntry.setFormatCode(makeCodes(documentMetadata.getFormatCode().getName(),
                    documentMetadata.getFormatCode().getCode(), documentMetadata.getFormatCode().getScheme()));
        }
        //i healthcareFacilityTypeCode
        if (documentMetadata.getHealthcareFacilityTypeCode() != null) {
            documentEntry.setHealthcareFacilityTypeCode(makeCodes(documentMetadata.getHealthcareFacilityTypeCode().getName(),
                    documentMetadata.getHealthcareFacilityTypeCode().getCode(),
                    documentMetadata.getHealthcareFacilityTypeCode().getScheme()));
        }
        //LanguageCode
        if (documentMetadata.getLanguageCode() != null) {
            documentEntry.setLanguageCode(documentMetadata.getLanguageCode());
        }

        documentEntry.setMimeType(mimeType); //mimetype is included because some repositories required this to work. Example is KIH

        //i objectType
        if (documentMetadata.getObjectType() != null) {
            documentEntry.setType(DocumentEntryType.valueOf(documentMetadata.getObjectType()));

        }

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
            documentEntry.setPracticeSettingCode(makeCodes(documentMetadata.getPracticeSetting().getName(),
                    documentMetadata.getPracticeSetting().getCode(),documentMetadata.getPracticeSetting().getScheme()    ));
        }

        //uniqeId
        String extenalDocumentId = null;
        if (documentMetadata.getUniqueId() != null) {
            extenalDocumentId = documentMetadata.getUniqueId();
        }
        documentEntry.setUniqueId(extenalDocumentId);
        documentEntry.setLogicalUuid(documentUuid);


        //legalAuthenticator
        if (documentMetadata.getLegalAuthenticator() != null && (documentMetadata.getLegalAuthenticator().getFamilyName() != null || documentMetadata.getLegalAuthenticator().getGivenName() != null)) {
            Name<?> legalAuthenticatorName = new XcnName();
            if (documentMetadata.getLegalAuthenticator().getFamilyName() != null) {
                legalAuthenticatorName.setFamilyName(documentMetadata.getLegalAuthenticator().getFamilyName());
            }
            if (documentMetadata.getLegalAuthenticator().getGivenName() != null) {
                legalAuthenticatorName.setGivenName(documentMetadata.getLegalAuthenticator().getGivenName());
            }
            if (documentMetadata.getLegalAuthenticator().getSecondAndFurtherGivenNames() != null) {
                legalAuthenticatorName.setSecondAndFurtherGivenNames(documentMetadata.getLegalAuthenticator().getSecondAndFurtherGivenNames());
            }
            Person legalAuthenticatorPerson = new Person();
            legalAuthenticatorPerson.setName(legalAuthenticatorName);
            documentEntry.setLegalAuthenticator(legalAuthenticatorPerson);
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
        return Math.abs(uuid.getLeastSignificantBits()) + "." + Math.abs(uuid.getMostSignificantBits())+"."+ Calendar.getInstance().getTimeInMillis();
    }

    private org.openapitools.model.Code makeCodeObject(String name, String code, String codeScheme) {
        org.openapitools.model.Code c = new org.openapitools.model.Code();
        c.setName(name);
        c.setScheme(codeScheme);
        c.setCode(code);
        return c;
    }

    private org.openehealth.ipf.commons.ihe.xds.core.metadata.Code makeCodes(String name, String code, String codeScheme) {
        org.openehealth.ipf.commons.ihe.xds.core.metadata.Code c = new Code();
        c.setCode(code);
        c.setSchemeName(codeScheme);
        c.setSchemeName(name);

        return c;
    }
}


