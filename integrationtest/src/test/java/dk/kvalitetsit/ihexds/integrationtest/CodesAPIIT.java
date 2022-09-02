package dk.kvalitetsit.ihexds.integrationtest;

import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TypeCodeApi;

import static org.junit.Assert.assertEquals;

public class CodesAPIIT extends AbstractIntegrationTest {



    private ApiClient apiClient;

    public CodesAPIIT() {
        apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());
    }

    @Test
    public void Testv1CodesTypeCodeGet() throws ApiException {
        var typeCodeApi = new TypeCodeApi(apiClient);
        var result = typeCodeApi.v1CodesTypeCodeGetWithHttpInfo();
        assertEquals(200, result.getStatusCode());

    }
}
