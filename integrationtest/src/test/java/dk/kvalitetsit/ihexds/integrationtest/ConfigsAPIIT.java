package dk.kvalitetsit.ihexds.integrationtest;

import org.junit.Test;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ConfigApi;
import org.openapitools.client.ApiClient;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class ConfigsAPIIT extends AbstractIntegrationTest{

    private final ConfigApi configApi;

    public ConfigsAPIIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());

        configApi = new ConfigApi(apiClient);

    }
    @Test
    public void testConfigGetontroller() throws ApiException, IOException, URISyntaxException {

        // Checking for defualt/standard owner null
        var result = configApi.v1ConfigGet();

        assertEquals(2, result.size());


    }

}
