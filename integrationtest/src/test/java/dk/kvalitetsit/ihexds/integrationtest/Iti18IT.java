package dk.kvalitetsit.ihexds.integrationtest;

import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.IhexdsApi;

public class Iti18IT extends AbstractIntegrationTest{

    private IhexdsApi ihexdsApi;

    public Iti18IT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());
        ihexdsApi = new IhexdsApi(apiClient);
    }

    //@Test


}
