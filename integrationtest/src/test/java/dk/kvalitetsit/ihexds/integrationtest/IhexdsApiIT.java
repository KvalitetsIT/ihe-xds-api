package dk.kvalitetsit.ihexds.integrationtest;

import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IhexdsApi;
import org.openapitools.client.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class IhexdsApiIT extends AbstractIntegrationTest{

    private IhexdsApi ihexdsApi;

    public IhexdsApiIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());
        ihexdsApi = new IhexdsApi(apiClient);
    }

    // post

    @Test
    public void testV1Iti18PostController() throws ApiException {


        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId("2512489996");
        iti18QueryParameter.setAvailabilityStatus("APPROVED");
        iti18QueryParameter.setDocumentType(Arrays.asList(new String[]{"STABLE", "ON-DEMAND"}));
        iti18QueryParameter.setEndToDate(null);
        iti18QueryParameter.setEndFromDate(null);
        //iti18QueryParameter.setStartFromDate(Long.parseLong("1662131460000"));
        iti18QueryParameter.setStartFromDate(null);
        iti18QueryParameter.setStartToDate(null);


        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);



        var result = ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request);

        assertEquals(200, result.getStatusCode());

        // add result?
    }

    @Test
    public void testV1Iti43PostController () throws ApiException {


        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("7a65a7d3-75f9-4ddf-b60a-f1e3078077c8");
        queryParameters.setRepositoryId("1.2.208.192.100.101");
        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti43Request iti43Request = new Iti43Request();

        iti43Request.setContext(context);
        iti43Request.setCredentialId(credentialID);
        iti43Request.setQueryParameters(queryParameters);

        var result = ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request);

        assertEquals(200, result.getStatusCode());

    }


    @Test
    public void testv1Iti41UploadPostController () throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("D:" + "9038f177-d345-4c42-b2b4-6e27314e714f");
        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


        var result = ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest);

        assertEquals(201, result.getStatusCode());

    }

    @Test
    public void testv1Iti41PreviewUploadPostController () throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41PreviewRequest request = new Iti41PreviewRequest();
        request.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));




        var result = ihexdsApi.v1Iti41PreviewUploadPostWithHttpInfo(request);

        assertEquals(200, result.getStatusCode());

    }
}
