package dk.kvalitetsit.ihexdsapi.service;


import dk.kvalitetsit.ihexdsapi.dgws.impl.AbstractTest;

import dk.kvalitetsit.ihexdsapi.service.impl.CodesServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.impl.UploadServiceImpl;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UploadServiceImplTest extends AbstractTest {

        UploadService uploadService;
        CodesService codesService;

    @Before
    public void setup() throws CodesExecption {

        String typeCodeCodes = "53576-5;74468-0;74465-6;11502-2;56446-8;";
        String typeCodeNames = "Personal health monitoring report Document;Questionnaire Form Definition Document;Questionnaire Response Document;LABORATORY REPORT.TOTAL;Appointment Summary Document;";
        String typeCodeScheme = "2.16.840.1.113883.6.1";
        String formatCodeCodes = "urn:ad:dk:medcom:phmr:full;urn:ad:dk:medcom:qfdd:full;urn:ad:dk:medcom:qrd:full;urn:ad:dk:medcom:appointmentsummary:full;urn:ad:dk:medcom:labreports:svareksponeringsservice;";
        String formatCodeNames = "DK PHMR schema;DK QFDD schema;DK QRD schema;DK Appointment Summary Document schema;Laboratoriesvar (samling af svar);";
        String formatCodeScheme = "1.2.208.184.100.10";
        String eventCodeCodes = "1.2.208.176.2.1;1.2.208.176.2.4;";
        String eventCodeNames = "NPU;Episode of care label;";
        String eventCodeScheme = "2.16.840.1.113883.6.1";
        String healthcareFacilityTypeCodeCodes = "N/A;264372000;20078004;554221000005108;554031000005103;546821000005103;547011000005103;546811000005109;550621000005101;22232009;550631000005103;550641000005106;550651000005108;394761003;550661000005105;42665001;554211000005102;550711000005101;550671000005100;554061000005105;264361005;554041000005106;554021000005101;550681000005102;550691000005104;550701000005104;554231000005106;554051000005108;";
        String healthcareFacilityTypeCodeNames = "N/A;apotek;behandlingscenter for stofmisbrugere;bosted;diætistklinik;ergoterapiklinik;fysioterapiklinik;genoptræningscenter (snomed: rehabiliteringscenter);hjemmesygepleje;hospital;jordemoderklinik;kiropraktor klinik;lægelaboratorium;lægepraksis (snomed:  almen lægepraksis);lægevagt;plejehjem;præhospitals enhed (snomed:  præhospitalsenhed);psykologisk rådgivningsklinik;speciallægepraksis;statsautoriseret fodterapeut (snomed:  fodterapeutklinik);sundhedscenter;sundhedsforvaltning;sundhedspleje;tandlægepraksis;tandpleje klinik;tandteknisk klinik;vaccinationsklinik;zoneterapiklinik;";
        String healthcareFacilityTypeCodeScheme = "2.16.840.1.113883.6.96";
        String practiceSettingCodeCodes = "N/A;408443003;394577000;394821009;394588006;394582007;394914008;394583002;394811001;394585009;408472002;394803006;394807007;419192003;394579002;408463005;394609007;551411000005104;394596001;394600006;394601005;394580004;421661004;408454008;394809005;394592004;418112009;394805004;394584008;394589003;394610002;394591006;394812008;394594003;394801008;394604002;394915009;394611003;394587001;394537008;394810000;554011000005107;394581000;394605001;394603008;408448007;394612005;";
        String practiceSettingCodeNames = "N/A;almen medicin;anæstesiologi;arbejdsmedicin;børne- og ungdomspsykiatri;dermato-venerologi;diagnostisk radiologi;endokrinologi;geriatri;gynækologi og obstetrik;hepatologi;hæmatologi;infektionsmedicin;intern medicin;kardiologi;karkirurgi;kirurgi;kirurgisk gastroenterologi;klinisk biokemi;klinisk farmakologi;klinisk fysiologi og nuklearmedicin (snomed:  klinisk fysiologi);klinisk genetik;klinisk immunologi;klinisk mikrobiologi;klinisk neurofysiologi;klinisk onkologi;lungesygdomme;medicinsk allergologi;medicinsk gastroenterologi;nefrologi;neurokirurgi;neurologi;odontologi (snomed: odontologiske specialer);oftalmologi;ortopædisk kirurgi;oto-rhino-laryngologi;patologisk anatomi og cytologi;plastikkirurgi;psykiatri;pædiatri;reumatologi;Retsmedicin;samfundsmedicin;tand-, mund- og kæbekirurgi;thoraxkirurgi;tropemedicin;urologi;";
        String practiceSettingCodeScheme = "2.16.840.1.113883.6.96";
        String classCodeCodes = "001";
        String classCodeNames = "Clinical report;";
        String classCodeScheme = "1.2.208.184.100.9";
        String objectTypeCodes = "STABLE";
        String objectTypeNames = "Stable";
        codesService = new CodesServiceImpl( typeCodeCodes,  typeCodeNames,  typeCodeScheme,
                formatCodeCodes,  formatCodeNames,  formatCodeScheme,
                eventCodeCodes,  eventCodeNames,  eventCodeScheme
                ,  healthcareFacilityTypeCodeCodes,  healthcareFacilityTypeCodeNames,  healthcareFacilityTypeCodeScheme,
                practiceSettingCodeCodes,  practiceSettingCodeNames,  practiceSettingCodeScheme
                ,  classCodeCodes,  classCodeNames,  classCodeScheme,
                objectTypeCodes,  objectTypeNames );



    uploadService = new UploadServiceImpl((CodesServiceImpl) codesService);
    }


    @Test
    public void testGetMetaDataJustAuthorInstitution() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/authorInstitution.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("Aftale Ansvarlige Organisation", result.getAuthorInstitution().getName() );
        assertEquals("325431000016004",result.getAuthorInstitution().getCode() );
        assertEquals("1.2.208.176.1.1",result.getAuthorInstitution().getScheme() );

    }

    @Test
    public void testGetMetaDataJustAuthorPerson() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/authorPerson.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument() );
        assertEquals("Jens", result.getAuthorPerson().getGivenName());
        assertEquals("Jensen", result.getAuthorPerson().getFamilyName() );
        assertEquals("Bo&Henrik", result.getAuthorPerson().getSecondAndFurtherGivenNames());

    }@Test
    public void testGetMetaDataJustAuthorConfidentialityCode() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/confidentialityCode.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals(null, result.getConfidentialityCode().getName());
        assertEquals("N", result.getConfidentialityCode().getCode());
        assertEquals("2.16.840.1.113883.5.25", result.getConfidentialityCode().getScheme() );

    }@Test
    public void testGetMetaDataJustCreationTime() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/creationTime.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        //TODO Need to figure out
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals("20220909071100", result.getCreationTime());



    }@Test
    public void testGetMetaDataJustEventCodeList() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/eventCode.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false,result.getIsLegalDocument() );
        assertEquals("Graviditet, fødsel og barsel",result.getEventCode().get(0).getName());
        assertEquals("ALAL51",result.getEventCode().get(0).getCode());
        assertEquals("1.2.208.176.2.4",result.getEventCode().get(0).getScheme() );

    }@Test
    public void testGetMetaDataJustLanguageCode() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/languageCode.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("da-DK", result.getLanguageCode());

    }@Test
    public void testGetMetaDataJustLegalAuthenticator() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/legalAuthenticator.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false,result.getIsLegalDocument());
        assertEquals(null, result.getLegalAuthenticator());
       /* assertEquals(null, result.getLegalAuthenticator().getFamilyName());
        assertEquals(null, result.getLegalAuthenticator().getGivenName());
        assertEquals(null, result.getLegalAuthenticator().getSecondAndFurtherGivenNames());*/

    }@Test
    public void testGetMetaDataJustPatientId() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/patientId.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("", result.getPatientId().getName());
        assertEquals("2906910651", result.getPatientId().getCode());
        assertEquals("1.2.208.176.1.2", result.getPatientId().getScheme() );

    }@Test
    public void testGetMetaDataJustServiceStartTime() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/serviceStartAndStop.xml");


        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("20221001080000", result.getServiceStartTime());


    }@Test
    public void testGetMetaDataJustServiceStopTime() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/serviceStartAndStop.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("20221001083000", result.getServiceStopTime());

    }@Test
    public void testGetMetaDataJustSourcePatientId() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/sourcePatientId.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("", result.getSourcePatientId().getName() );
        assertEquals("2906910651", result.getSourcePatientId().getCode());
        assertEquals("1.2.208.176.1.2", result.getSourcePatientId().getScheme());

    }@Test
    public void testGetMetaDataJustSourcePatientInfo() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/sourcePatientInfo.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("Andersen", result.getSourcePatientInfo().getFamilyName() );
        assertEquals("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN", result.getSourcePatientInfo().getGivenName());
        assertEquals("Aftaler", result.getSourcePatientInfo().getSecondAndFurtherGivenNames());
        assertEquals("M", result.getSourcePatientInfo().getGender().toString());
        assertEquals("19910629", result.getSourcePatientInfo().getBirthTime());

    }@Test
    public void testGetMetaDataJustTitle() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/title.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals( "Aftale for 2906910651", result.getTitle());


    }@Test
    public void testGetMetaDataJustTypeCode() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/typeCode.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("Dato og tidspunkt for møde mellem patient og sundhedsperson", result.getTypeCode().getName());
        assertEquals("39289-4", result.getTypeCode().getCode());
        assertEquals("2.16.840.1.113883.6.1", result.getTypeCode().getScheme());

    }@Test
    public void testGetMetaDataJustUniqueId() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/uniqueId.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(false, result.getIsLegalDocument());
        assertEquals("1.2.208.184^74b28be0-4948-11ed-b878-0242acees00f2", result.getUniqueId());


    }@Test
    public void testGetMetaDataValidDocument() throws IOException, JDOMException {
        // Given
        String inputXml =  getFileString("/xml/DK-APD_Example_1_2_apd_maximum.xml");

        // When
        var result =  uploadService.getGeneratedMetaData(inputXml);

        // Then
        assertEquals(true,result.getIsLegalDocument() );
        assertEquals("Aftale Ansvarlige Organisation", result.getAuthorInstitution().getName() );
        assertEquals("325431000016004",result.getAuthorInstitution().getCode() );
        assertEquals("1.2.208.176.1.1",result.getAuthorInstitution().getScheme() );

        assertEquals("Jens", result.getAuthorPerson().getGivenName());
        assertEquals("Jensen", result.getAuthorPerson().getFamilyName() );
        assertEquals("Bo&Henrik", result.getAuthorPerson().getSecondAndFurtherGivenNames());


        assertEquals(null, result.getConfidentialityCode().getName());
        assertEquals("N", result.getConfidentialityCode().getCode());
        assertEquals("2.16.840.1.113883.5.25", result.getConfidentialityCode().getScheme() );

        assertEquals("20220909071100", result.getCreationTime());

        assertEquals("Graviditet, fødsel og barsel",result.getEventCode().get(0).getName());
        assertEquals("ALAL51",result.getEventCode().get(0).getCode());
        assertEquals("1.2.208.176.2.4",result.getEventCode().get(0).getScheme() );

        assertEquals("da-DK", result.getLanguageCode());

        assertEquals(null, result.getLegalAuthenticator());
       /* assertEquals(null, result.getLegalAuthenticator().getFamilyName());
        assertEquals(null, result.getLegalAuthenticator().getGivenName());
        assertEquals(null, result.getLegalAuthenticator().getSecondAndFurtherGivenNames());*/

        assertEquals("", result.getPatientId().getName());
        assertEquals("2906910651", result.getPatientId().getCode());
        assertEquals("1.2.208.176.1.2", result.getPatientId().getScheme() );

        assertEquals("20221001080000", result.getServiceStartTime());

        assertEquals("20221001083000", result.getServiceStopTime());

        assertEquals("", result.getSourcePatientId().getName() );
        assertEquals("2906910651", result.getSourcePatientId().getCode());
        assertEquals("1.2.208.176.1.2", result.getSourcePatientId().getScheme());

        assertEquals("Andersen", result.getSourcePatientInfo().getFamilyName() );
        assertEquals("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN", result.getSourcePatientInfo().getGivenName());
        assertEquals("Aftaler", result.getSourcePatientInfo().getSecondAndFurtherGivenNames());
        assertEquals("M", result.getSourcePatientInfo().getGender().toString());
        assertEquals("19910629", result.getSourcePatientInfo().getBirthTime());

        assertEquals( "Aftale for 2906910651", result.getTitle());

        assertEquals("Dato og tidspunkt for møde mellem patient og sundhedsperson", result.getTypeCode().getName());
        assertEquals("39289-4", result.getTypeCode().getCode());
        assertEquals("2.16.840.1.113883.6.1", result.getTypeCode().getScheme());

        assertEquals("1.2.208.184^74b28be0-4948-11ed-b878-0242acees00f2", result.getUniqueId());


    }


}
