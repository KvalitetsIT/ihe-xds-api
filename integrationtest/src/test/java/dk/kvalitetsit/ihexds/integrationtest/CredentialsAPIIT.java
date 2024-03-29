package dk.kvalitetsit.ihexds.integrationtest;


import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.openapitools.client.api.CredentialsApi;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CreateCredentialRequest;
import org.openapitools.client.model.Iti18Request;
import org.openapitools.model.CredentialInfoResponse;


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
        var result = credentialsApi.v1CredentialinfoGet(null, null);

        assertEquals(3, result.size());


    }

    @Test
    public void testCredentialinfoGetControllerFiltered() throws ApiException, IOException, URISyntaxException {

        // Checking for defualt/standard owner null
        String filter = CredentialInfoResponse.CredentialTypeEnum.HEALTHCAREPROFESSIONAL.toString();

        var result = credentialsApi.v1CredentialinfoGet(null, filter);

        assertEquals(2, result.size());

         filter = CredentialInfoResponse.CredentialTypeEnum.SYSTEM.toString();

         result = credentialsApi.v1CredentialinfoGet(null, filter);

        assertEquals(1, result.size());


    }
    @Test
    public void testCredentialinfoGetControllerThrowsBadRequest() throws ApiException, IOException, URISyntaxException {



        ApiException apiException = assertThrows(ApiException.class, () -> credentialsApi.v1CredentialinfoGet(null, "filter"));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Bad type query"));



    }

    @Test
    public void testCredentialinfoPutController() throws ApiException, URISyntaxException, IOException {

        CreateCredentialRequest createCredentialRequest = new CreateCredentialRequest();


        createCredentialRequest.setDisplayName("My cerificate");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest.setPrivateKeyStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI())));

        createCredentialRequest.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));


        var result = credentialsApi.v1CredentialinfoPutWithHttpInfo(createCredentialRequest);

        assertEquals(201, result.getStatusCode());


    }

    @Test
    public void testCredentialinfoPutControllerThrowsBadRequestExeceptionInvalidCert() throws ApiException, URISyntaxException, IOException {


        CreateCredentialRequest createCredentialRequest = new CreateCredentialRequest();


        createCredentialRequest.setDisplayName("My cerificate");
        createCredentialRequest.setOwner("Me");

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


        createCredentialRequest.setDisplayName("My cerificate");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest.setPrivateKeyStr("Not a key");
        createCredentialRequest.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));


        ApiException apiException = assertThrows(ApiException.class, () -> credentialsApi.v1CredentialinfoPutWithHttpInfo(createCredentialRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Invalid private key"));
    }

    /*


    @Test
    public void testCredentialinfoPutControllerExistingID() throws ApiException, URISyntaxException, IOException {
        String id = "1234AAADD";
        // First time added
        CreateCredentialRequest createCredentialRequest = new CreateCredentialRequest();

        createCredentialRequest.setDisplayName("My cerificate");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest.setPrivateKeyStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI())));
        createCredentialRequest.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));

        // Adding 2nd time
        CreateCredentialRequest createCredentialRequest2nd = new CreateCredentialRequest();

        createCredentialRequest.setDisplayName("My cerificate");
        createCredentialRequest.setOwner("Me");
        createCredentialRequest2nd.setOwner("Me");
        createCredentialRequest2nd.setPrivateKeyStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI())));
        createCredentialRequest2nd.setPublicCertStr(Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI())));

        credentialsApi.v1CredentialinfoPut(createCredentialRequest);

        ApiException apiException = assertThrows(ApiException.class, () -> credentialsApi.v1CredentialinfoPutWithHttpInfo(createCredentialRequest2nd));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        System.out.println(apiException.getResponseBody());
        //assertTrue(apiException.getResponseBody().contains("A credential vault with id " + id + " is already registered"));
    }*/

    @Test
    public void testCallService() throws ApiException {
        var iti18Request = new Iti18Request();
        /*
        iti18HealthCareProfessionalRequest.set

       input.setName("John Doe");

       // var result = iheXdsApi.v1Iti18Get(input);

        //assertNotNull(result);
       assertEquals(input.getName(), result.getName());
      assertNull(result.getiCanBeNull());
       assertNotNull(result.getNow());*/
    }
}
