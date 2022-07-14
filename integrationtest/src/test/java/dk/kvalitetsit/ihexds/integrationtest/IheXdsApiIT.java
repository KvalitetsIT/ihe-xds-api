package dk.kvalitetsit.ihexds.integrationtest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.model.Iti18HealthCareProfessionalRequest;

public class IheXdsApiIT extends AbstractIntegrationTest {

   // private final IhexdsApi iheXdsApi;

    public IheXdsApiIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());

     //   iheXdsApi = new IheXdsController(apiClient);
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
