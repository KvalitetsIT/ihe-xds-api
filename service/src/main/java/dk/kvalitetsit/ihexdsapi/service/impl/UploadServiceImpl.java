package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import dk.kvalitetsit.ihexdsapi.service.CodesService;
import dk.kvalitetsit.ihexdsapi.service.UploadService;
import dk.s4.hl7.cda.convert.CDAMetadataXmlCodec;
import dk.s4.hl7.cda.model.CodedValue;
import dk.s4.hl7.cda.model.cdametadata.CDAMetadata;
import org.openapitools.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class UploadServiceImpl implements UploadService {

    private CodesService codesService;


    public UploadServiceImpl(CodesServiceImpl codesService) {
        this.codesService = codesService;
    }

    private final String DEFAULTCLASSCODE = "001";
    private final String CLASSCODESCHEME = "1.2.208.184.100.9";


    @Override
    public ResponseMetaData getMetaData(String xml) {
        return null;
    }

    @Override
    public Iti41UploadResponse uploadRequest(String xmlPayload, DgwsClientInfo dgwsClientInfo) {
        return null;
    }

    @Override
    public GeneratedMetaData getGeneratedMetaData(String xml) throws ItiException {
        GeneratedMetaData generatedMetaData = new GeneratedMetaData();

        if (xml == null || xml.isEmpty()) {
            throw new ItiException(1000, "XML file is empty", null);
        }

        CDAMetadataXmlCodec codec = new CDAMetadataXmlCodec();
        CDAMetadata cdaMetadataDecoded = codec.decode(xml);

        // author.authorInstitution - organization

        if (cdaMetadataDecoded.getAuthor() != null && cdaMetadataDecoded.getAuthor().getOrganizationIdentity() != null && cdaMetadataDecoded.getAuthor().getOrganizationIdentity().getId() != null) {
            String code = cdaMetadataDecoded.getAuthor().getOrganizationIdentity().getId().getExtension();
            String codeSystem = cdaMetadataDecoded.getAuthor().getOrganizationIdentity().getId().getRoot();
            String displayName = cdaMetadataDecoded.getAuthor().getOrganizationIdentity().getOrgName();

            generatedMetaData.setAuthorInstitution(makeCodeObject(displayName, code, codeSystem));
        }


        //auther.authorperson


        if (cdaMetadataDecoded.getAuthor() != null && cdaMetadataDecoded.getAuthorperson() != null) {

            GeneratedMetaDataAuthorPerson person = new GeneratedMetaDataAuthorPerson();
            if (cdaMetadataDecoded.getAuthorperson().getFamilyName() != null) {
                person.setFamilyName(cdaMetadataDecoded.getAuthorperson().getFamilyName());
            }
            if (cdaMetadataDecoded.getAuthorperson().getGivenNames() != null && cdaMetadataDecoded.getAuthorperson().getGivenNames().length > 0) {
                person.setGivenName(cdaMetadataDecoded.getAuthorperson().getGivenNames()[0]);
                if (cdaMetadataDecoded.getAuthorperson().getGivenNames().length > 1) {
                    person.setSecondAndFurtherGivenNames(cdaMetadataDecoded.getAuthorperson().getGivenNames()[1]);
                    for (int i = 2; i < cdaMetadataDecoded.getAuthorperson().getGivenNames().length; i++) {
                        person.setSecondAndFurtherGivenNames(person.getSecondAndFurtherGivenNames() + "&" + cdaMetadataDecoded.getAuthorperson().getGivenNames()[i]);
                    }
                }
            }
            generatedMetaData.setAuthorPerson(person);

        }

        //classCode
        Code code = null;

        if (cdaMetadataDecoded.getCodeCodedValue() != null && cdaMetadataDecoded.getCodeCodedValue().getCode() != null) { //even though in form (as output only), the value must come from document
            String classCode = codesService.getClassCodeFromTypeCode(cdaMetadataDecoded.getCodeCodedValue().getCode());
            if (classCode != null) {
                String classCodeName = codesService.getClassCodeNameFromCode(classCode);
                if (classCodeName != null) {
                    code = makeCodeObject(classCodeName, classCode, CLASSCODESCHEME);
                    generatedMetaData.setClassCode(code);
                }
            }

            if (code == null) {
                String defaultClassCode = this.DEFAULTCLASSCODE;
                String classCodeName = codesService.getClassCodeNameFromCode(defaultClassCode);
                if (classCodeName != null) {
                    code = makeCodeObject(classCodeName, defaultClassCode, CLASSCODESCHEME);
                    generatedMetaData.setClassCode(code);
                }
            }

        }



            //confidentialityCode
            if (cdaMetadataDecoded.getConfidentialityCodeCodedValue() != null && cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCode() != null && cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCodeSystem() != null) {
                generatedMetaData.setConfidentialityCode(makeCodeObject(cdaMetadataDecoded.getConfidentialityCodeCodedValue().getDisplayName(), cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCode(), cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCodeSystem()));
            }


        //contentTypeCode - not used

        //creationTime

            if (cdaMetadataDecoded.getCreationTime() != null) {
                generatedMetaData.setCreationTime(turnEpochToUTCDateTime(cdaMetadataDecoded.getCreationTime().toInstant().toEpochMilli()));
            }


        //eventCodedList

            for (CodedValue event : cdaMetadataDecoded.getEventCodeList()) {
                if (generatedMetaData.getEventCode() == null) {
                    generatedMetaData.setEventCode(new LinkedList<Code>());
                }
                Code eventCode = makeCodeObject(event.getDisplayName(), event.getCode(), event.getCodeSystem());
                generatedMetaData.getEventCode().add(eventCode);
            }



        //formatcode

            if (cdaMetadataDecoded.getFormatCode() != null) { //overrule from possible input, since that from document has higher priority than from input
                Code formatCode = makeCodeObject(cdaMetadataDecoded.getFormatCode().getDisplayName(), cdaMetadataDecoded.getFormatCode().getCode(), cdaMetadataDecoded.getFormatCode().getCodeSystem());
                generatedMetaData.setFormatCode(formatCode);
            }


        //LanguageCode

            if (cdaMetadataDecoded.getLanguageCode() != null) {
                generatedMetaData.setLanguageCode(cdaMetadataDecoded.getLanguageCode());
            }



        //legalAuthenticator

            if (cdaMetadataDecoded.getLegalAuthenticator() != null && cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity() != null) {

                GeneratedMetaDataLegalAuthenticator auntenticator = new GeneratedMetaDataLegalAuthenticator();

                if (cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getFamilyName() != null) {
                    auntenticator.setFamilyName(cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getFamilyName());
                }
                if (cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames() != null && cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames().length > 0) {
                    auntenticator.setGivenName(cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames()[0]);
                    if (cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames().length > 1) {
                        auntenticator.setSecondAndFurtherGivenNames(cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames()[1]);
                        for (int i = 2; i < cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames().length; i++) {
                            auntenticator.setSecondAndFurtherGivenNames(auntenticator.getSecondAndFurtherGivenNames() + "&" + cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames()[i]);
                        }
                    }
                }
                generatedMetaData.setLegalAuthenticator(auntenticator);
            }


        //patientId

            if (cdaMetadataDecoded.getPatient() != null && cdaMetadataDecoded.getPatientId() != null && cdaMetadataDecoded.getPatientId().getCode() != null && cdaMetadataDecoded.getPatientId().getCodeSystem() != null) {
                Code patientIdCode = makeCodeObject("", cdaMetadataDecoded.getPatientId().getCode(), cdaMetadataDecoded.getPatientId().getCodeSystem());
                generatedMetaData.setPatientId(patientIdCode);
            }


        //serviceStartTime

            if (cdaMetadataDecoded.getServiceStartTime() != null) {
                generatedMetaData.setServiceStartTime(turnEpochToUTCDateTime(cdaMetadataDecoded.getServiceStartTime().toInstant().toEpochMilli()));
            }


        //serviceStopTime

            if (cdaMetadataDecoded.getServiceStopTime() != null) {
                generatedMetaData.setServiceStopTime(turnEpochToUTCDateTime(cdaMetadataDecoded.getServiceStopTime().toInstant().toEpochMilli()));
            }


        //sourcePatientId

            if (cdaMetadataDecoded.getPatient() != null && cdaMetadataDecoded.getSourcePatientId() != null && cdaMetadataDecoded.getSourcePatientId().getCode() != null && cdaMetadataDecoded.getSourcePatientId().getCodeSystem() != null) {
                Code sourcePatientIdCode = makeCodeObject("", cdaMetadataDecoded.getSourcePatientId().getCode(), cdaMetadataDecoded.getSourcePatientId().getCodeSystem());
                generatedMetaData.setSourcePatientId(sourcePatientIdCode);
            }

        //sourcePatientInfo

            if (cdaMetadataDecoded.getPatient() != null) {
                GeneratedMetaDataSourcePatientInfo sourcePatientInfoPerson = new GeneratedMetaDataSourcePatientInfo();
                if (cdaMetadataDecoded.getPatient().getFamilyName() != null) {
                    sourcePatientInfoPerson.setFamilyName(cdaMetadataDecoded.getPatient().getFamilyName());
                }
                if (cdaMetadataDecoded.getPatient().getGivenNames() != null && cdaMetadataDecoded.getPatient().getGivenNames().length > 0) {
                    sourcePatientInfoPerson.setGivenName(cdaMetadataDecoded.getPatient().getGivenNames()[0]);
                    if (cdaMetadataDecoded.getPatient().getGivenNames().length > 1) {
                        sourcePatientInfoPerson.setSecondAndFurtherGivenNames(cdaMetadataDecoded.getPatient().getGivenNames()[1]);
                        for (int i = 2; i < cdaMetadataDecoded.getPatient().getGivenNames().length; i++) {
                            sourcePatientInfoPerson.setSecondAndFurtherGivenNames(sourcePatientInfoPerson.getSecondAndFurtherGivenNames() + "&" + cdaMetadataDecoded.getPatient().getGivenNames()[i]);
                        }
                    }
                }
                if (cdaMetadataDecoded.getPatient().getBirthTime() != null) {
                    sourcePatientInfoPerson.setBirthTime(turnEpochToDate(cdaMetadataDecoded.getPatient().getBirthTime().toInstant().toEpochMilli()));

                }
                if (cdaMetadataDecoded.getPatient().getGender() != null) {
                    sourcePatientInfoPerson.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.valueOf(cdaMetadataDecoded.getPatient().getGender().name().substring(0, 1)));

                }
                generatedMetaData.setSourcePatientInfo(sourcePatientInfoPerson);

            }

        //title


            if (cdaMetadataDecoded.getTitle() != null) {
                generatedMetaData.setTitle(cdaMetadataDecoded.getTitle());
            }

        //typeCode

            if ((cdaMetadataDecoded.getCodeCodedValue() != null) && (cdaMetadataDecoded.getCodeCodedValue().getCode() != null) && (cdaMetadataDecoded.getCodeCodedValue().getCodeSystem() != null) && (cdaMetadataDecoded.getCodeCodedValue().getDisplayName() != null)) {
                Code typeCode = makeCodeObject(cdaMetadataDecoded.getCodeCodedValue().getDisplayName(), cdaMetadataDecoded.getCodeCodedValue().getCode(), cdaMetadataDecoded.getCodeCodedValue().getCodeSystem());
                generatedMetaData.setTypeCode(typeCode);
            }



        //uniqeId

            if (cdaMetadataDecoded.getId() != null && cdaMetadataDecoded.getId().getExtension() != null && cdaMetadataDecoded.getId().getRoot() != null) {
                generatedMetaData.setUniqueId(cdaMetadataDecoded.getId().getRoot() + "^" + cdaMetadataDecoded.getId().getExtension());
            } else if (cdaMetadataDecoded.getId() != null && cdaMetadataDecoded.getId().getRoot() != null) {
                generatedMetaData.setUniqueId(cdaMetadataDecoded.getId().getRoot());
            }



        // Is Vaild CDA document
        generatedMetaData.setIsLegalDocument(

                isValidDocument(generatedMetaData));


        return generatedMetaData;
    }

    private org.openapitools.model.Code makeCodeObject(String name, String code, String codeScheme) {
        org.openapitools.model.Code c = new org.openapitools.model.Code();
        c.setName(name);
        c.setScheme(codeScheme);
        c.setCode(code);
        return c;
    }

    private String turnEpochToUTCDateTime(long time) {


        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    private String turnEpochToDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    private boolean isValidDocument(GeneratedMetaData data) {
        if (data.getAuthorInstitution() != null && data.getConfidentialityCode() != null
                && data.getCreationTime() != null && data.getLanguageCode() != null
                && data.getPatientId() != null && data.getSourcePatientId() != null && data.getSourcePatientInfo() != null
                && data.getTitle() != null && data.getTypeCode() != null && data.getUniqueId() != null) {
            return true;
        }

        return false;

    }
/*
    private String turnStringToUTCDate(String time) throws ParseException {

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssZ");
        Date dt = sf.parse(time);
        Instant newDt =  dt.toInstant();
        long newDate = (newDt.toEpochMilli());

        Date date = new Date(newDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);

        return formattedDate;
    }*/

}
