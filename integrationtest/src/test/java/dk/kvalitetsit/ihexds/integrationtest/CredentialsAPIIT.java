package dk.kvalitetsit.ihexds.integrationtest;

import static org.junit.Assert.assertNotNull;

import dk.kvalitetsit.ihexdsapi.controller.CredentialInfoController;
import dk.kvalitetsit.ihexdsapi.dgws.impl.CredentialServiceImpl;
import org.junit.Test;
import org.openapitools.client.api.CredentialsApi;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CreateCredentialResponse;
import org.openapitools.model.Iti18HealthCareProfessionalRequest;

public class CredentialsAPIIT extends AbstractIntegrationTest {

   private final CredentialsApi credentialsApi;

    public CredentialsAPIIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());

        credentialsApi = new CredentialsApi(apiClient);


    }

    @Test
    public void testCredentialinfoPutController() throws ApiException{


        CreateCredentialResponse createCredentialResponse = new CreateCredentialResponse();

        createCredentialResponse.setCvr("637283d");
        createCredentialResponse.setId("1234ABA");
        createCredentialResponse.setOrganisation("Statens Serum Institute");
        createCredentialResponse.setOwner("Me");
        createCredentialResponse.setPrivateKeyStr("Private key");
        createCredentialResponse.setPublicCertStr("Certificate");


        //var result = credentialsApi.v1CredentialinfoPut(createCredentialResponse);


        //System.out.println(subject.credentialService);
    }
    @Test
    public void testCredentialinfoPutControllerFails() throws ApiException{

        var subject = new CredentialInfoController();

        CreateCredentialResponse createCredentialResponse = new CreateCredentialResponse();

        createCredentialResponse.setCvr("637283d");
        createCredentialResponse.setId("1234ABA");
        createCredentialResponse.setOrganisation("Statens Serum Institute");
        createCredentialResponse.setOwner("Me");
        createCredentialResponse.setPrivateKeyStr("Private key");
        createCredentialResponse.setPublicCertStr("Private key");




        var result = subject.v1CredentialinfoPut(createCredentialResponse);


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
