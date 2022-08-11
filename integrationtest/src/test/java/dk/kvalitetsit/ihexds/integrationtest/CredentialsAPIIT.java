package dk.kvalitetsit.ihexds.integrationtest;


import org.apache.http.HttpStatus;
import org.junit.Test;
import org.openapitools.client.api.CredentialsApi;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CreateCredentialRequest;
import org.openapitools.model.Iti18HealthCareProfessionalRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CredentialsAPIIT extends AbstractIntegrationTest {

   private final CredentialsApi credentialsApi;



    public CredentialsAPIIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());

        credentialsApi = new CredentialsApi(apiClient);



    }
@Test
    public void testCredentialinfoGetController() throws ApiException, IOException, URISyntaxException {

        // Checking for defualt/standard owner null
        var result = credentialsApi.v1CredentialinfoGet(null);

    assertEquals(1, result.size());



    }

    @Test
    public void testCredentialinfoPutController() throws ApiException, URISyntaxException, IOException {

        CreateCredentialRequest createCredentialRequest = new CreateCredentialRequest();

        createCredentialRequest.setCvr("637283d");
        createCredentialRequest.setId("1234ABA");
        createCredentialRequest.setOrganisation("Statens Serum Institute");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest.setPrivateKeyStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI())));

        createCredentialRequest.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));


        var result = credentialsApi.v1CredentialinfoPutWithHttpInfo(createCredentialRequest);

        assertEquals(201, result.getStatusCode());



    }
@Test
    public void testCredentialinfoPutControllerThrowsBadRequestExeceptionInvalidCert() throws ApiException, URISyntaxException, IOException {


        CreateCredentialRequest createCredentialRequest = new CreateCredentialRequest();

        createCredentialRequest.setCvr("637283d");
        createCredentialRequest.setId("1234ABC");
        createCredentialRequest.setOrganisation("Statens Serum Institute");
        createCredentialRequest.setOwner("Me");
    createCredentialRequest.setPrivateKeyStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI())));


    createCredentialRequest.setPublicCertStr("Not a certificate");


        ApiException apiException = assertThrows(ApiException.class, () -> credentialsApi.v1CredentialinfoPutWithHttpInfo(createCredentialRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Invalid certificate"));




    }

    @Test
    public void testCredentialinfoPutControllerThrowsBadRequestExeceptionInvalidKey() throws ApiException, URISyntaxException, IOException {


        CreateCredentialRequest createCredentialRequest = new CreateCredentialRequest();

        createCredentialRequest.setCvr("637283d");
        createCredentialRequest.setId("1234ABd");
        createCredentialRequest.setOrganisation("Statens Serum Institute");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest.setPrivateKeyStr("Not a key");
        createCredentialRequest.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));


        ApiException apiException = assertThrows(ApiException.class, () -> credentialsApi.v1CredentialinfoPutWithHttpInfo(createCredentialRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Invalid private key"));
    }

    @Test
    public void testCredentialinfoPutControllerExistingID() throws ApiException, URISyntaxException, IOException {
        String id = "1234AAADD";
        // First time added
        CreateCredentialRequest createCredentialRequest = new CreateCredentialRequest();
        createCredentialRequest.setCvr("gfsr3");
        createCredentialRequest.setId(id);
        createCredentialRequest.setOrganisation("Statens Serum Institute");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest.setPrivateKeyStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI())));
        createCredentialRequest.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));

        // Adding 2nd time
        CreateCredentialRequest createCredentialRequest2nd = new CreateCredentialRequest();
        createCredentialRequest2nd.setCvr("637283d");
        createCredentialRequest2nd.setId(id);
        createCredentialRequest2nd.setOrganisation("Statens Serum Institute");
        createCredentialRequest2nd.setOwner("Me");
        createCredentialRequest2nd.setPrivateKeyStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI())));
        createCredentialRequest2nd.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));

        credentialsApi.v1CredentialinfoPut(createCredentialRequest);

        ApiException apiException = assertThrows(ApiException.class, () -> credentialsApi.v1CredentialinfoPutWithHttpInfo(createCredentialRequest2nd));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("A credential vault with id " +id + " is already registered"));








    }

    @Test
    public void testCallService() throws ApiException {
        var iti18HealthCareProfessionalRequest = new Iti18HealthCareProfessionalRequest();
//        iti18HealthCareProfessionalRequest.set

//        input.setName("John Doe");

       // var result = iheXdsApi.v1Iti18Get(input);

        //assertNotNull(result);
//        assertEquals(input.getName(), result.getName());
  //      assertNull(result.getiCanBeNull());
    //    assertNotNull(result.getNow());
    }
}
