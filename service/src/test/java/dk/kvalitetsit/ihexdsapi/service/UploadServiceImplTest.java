package dk.kvalitetsit.ihexdsapi.service;


import dk.kvalitetsit.ihexdsapi.dgws.impl.AbstractTest;

import dk.kvalitetsit.ihexdsapi.service.impl.Iti41ServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UploadServiceImplTest extends AbstractTest {

    private Iti41Service uploadService;

    private Iti41PortType iti41PortType;

    @Before
    public void setup() {
        iti41PortType = Mockito.mock(Iti41PortType.class);
        uploadService = new Iti41ServiceImpl(iti41PortType);
    }

/*
    @Test
    public void testGetMetaDataJustAuthorInstitution(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.getAuthorInstitution().getName(), "Aftale Ansvarlige Organisation");
        assertEquals(result.getAuthorInstitution().getCode(), "325431000016004");
        assertEquals(result.getAuthorInstitution().getScheme(), "1.2.208.176.1.1");

    }@Test
    public void testGetMetaDataJustAuthorPerson(){
        // Given
        String inputXml =  getFileString("/certificates/person.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.autorPerson.givenName, "Jens");
        assertEquals(result.autorPerson.familyname, "Jensen");
        assertEquals(result.autorPerson.secondAndFurtherGivenNames, "Henrik&Bo");

    }@Test
    public void testGetMetaDataJustAuthorConfidentialityCode(){
        // Given
        String inputXml =  getFileString("/certificates/confidentialityCode.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.confidentialityCode.name, null);
        assertEquals(result.confidentialityCode.code, "N");
        assertEquals(result.confidentialityCode.codeScheme, "2.16.840.1.113883.5.25");

    }@Test
    public void testGetMetaDataJustCreationTime(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        //TODO Need to figure out
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.creationTime, "20220909071100");


    }@Test
    public void testGetMetaDataJustEventCodeList(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.eventCode[0].name, "Graviditet, fødsel og barsel ");
        assertEquals(result.eventCode[0].code, "ALAL51");
        assertEquals(result.eventCode[0].codeScheme, "1.2.208.176.2.4 ");

    }@Test
    public void testGetMetaDataJustLanguageCode(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.languageCode, "da-DK");

    }@Test
    public void testGetMetaDataJustLegalAuthenticator(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.legalAuthenticator.familyName, null);
        assertEquals(result.legalAuthenticator.givenName, null);
        assertEquals(result.legalAuthenticator.secondAndFurtherGivenNames, null);

    }@Test
    public void testGetMetaDataJustPatientId(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.patientId.name, null);
        assertEquals(result.patientId.code, "2906910651");
        assertEquals(result.patientId.codeScheme, "1.2.208.176.1.2");

    }@Test
    public void testGetMetaDataJustServiceStartTime(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.serviceStartTime, "20221001080000");


    }@Test
    public void testGetMetaDataJustServiceStopTime(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.serviceStopTime, "20221001083000");

    }@Test
    public void testGetMetaDataJustSourcePatientId(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.sourcePatientId.name, null);
        assertEquals(result.sourcePatientId.code, "2906910651");
        assertEquals(result.sourcePatientId.codeScheme, "1.2.208.176.1.2");

    }@Test
    public void testGetMetaDataJustSourcePatientInfo(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.sourcePatientInfo.givenName, "Andersen");
        assertEquals(result.sourcePatientInfo.familyname, "AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        assertEquals(result.sourcePatientInfo.secondAndFurtherGivenNames, "Aftaler");
        assertEquals(result.sourcePatientInfo.gender, "M");
        assertEquals(result.sourcePatientInfo.dateOfBirth, "19910629");

    }@Test
    public void testGetMetaDataJustTitle(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.title, "Aftale for 2906910651");


    }@Test
    public void testGetMetaDataJustTypeCode(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.type.name, "Dato og tidspunkt for møde mellem patient og sundhedsperson");
        assertEquals(result.type.code, "39289-4");
        assertEquals(result.type.codeScheme, "2.16.840.1.113883.6.1");

    }@Test
    public void testGetMetaDataJustUniqueId(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), false);
        assertEquals(result.getUniqueId(), "1.2.208.184^74b28be0-4948-11ed-b878-0242acees00f2");


    }@Test
    public void testGetMetaDataValidDocument(){
        // Given
        String inputXml =  getFileString("/certificates/auth.xml");

        // When
        var result =  uploadService.getMetaData(inputXml);

        // Then
        assertEquals(result.getIsLegalDocument(), true);
        assertEquals(result.getAuthorInstitution().getName(), "Aftale Ansvarlige Organisation");
        assertEquals(result.getAuthorInstitution().getCode(), "325431000016004");
        assertEquals(result.getAuthorInstitution().getScheme(), "1.2.208.176.1.1");

        assertEquals(result.getAuthorPerson().givenName, "Jens");
        assertEquals(result.authorPerson.familyname, "Jensen");
        assertEquals(result.authorPerson.secondAndFurtherGivenNames, "Henrik&Bo");

        assertEquals(result.confidentialityCode.name, null);
        assertEquals(result.confidentialityCode.code, "N");
        assertEquals(result.confidentialityCode.codeScheme, "2.16.840.1.113883.5.25");

        assertEquals(result.creationTime, "20220909071100");

        assertEquals(result.eventCode[0].name, "Graviditet, fødsel og barsel ");
        assertEquals(result.eventCode[0].code, "ALAL51");
        assertEquals(result.eventCode[0].codeScheme, "1.2.208.176.2.4 ");

        assertEquals(result.languageCode, "da-DK");

        assertEquals(result.patientId.name, null);
        assertEquals(result.patientId.code, "2906910651");
        assertEquals(result.patientId.codeScheme, "1.2.208.176.1.2");

        assertEquals(result.getServiceStartTime(), "20221001080000");

        assertEquals(result.getServiceStopTime(), "20221001083000");

        assertEquals(result.sourcePatientId.name, null);
        assertEquals(result.sourcePatientId.code, "2906910651");
        assertEquals(result.sourcePatientId.codeScheme, "1.2.208.176.1.2");

        assertEquals(result.sourcePatientInfo.givenName, "Andersen");
        assertEquals(result.sourcePatientInfo.familyname, "AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        assertEquals(result.sourcePatientInfo.secondAndFurtherGivenNames, "Aftaler");
        assertEquals(result.sourcePatientInfo.gender, "M");
        assertEquals(result.sourcePatientInfo.dateOfBirth, "19910629");

        assertEquals(result.title, "Aftale for 2906910651");

        assertEquals(result.type.name, "Dato og tidspunkt for møde mellem patient og sundhedsperson");
        assertEquals(result.type.code, "39289-4");
        assertEquals(result.type.codeScheme, "2.16.840.1.113883.6.1");

        assertEquals(result.getUniqueId(), "1.2.208.184^74b28be0-4948-11ed-b878-0242acees00f2");


    }
*/

}
