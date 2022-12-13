package dk.kvalitetsit.ihexds.integrationtest;

import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IhexdsApi;
import org.openapitools.client.api.TypeCodeApi;
import org.openapitools.client.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

// TODO Add more errors
public class IhexdsApiIT extends AbstractIntegrationTest {

    private IhexdsApi ihexdsApi;

    public IhexdsApiIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());
        ihexdsApi = new IhexdsApi(apiClient);
    }

    // post
// -----------------Iti41 tests---------------------------------------------------- \\

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

    }

    @Test
    public void testV1Iti18PostControllerThrowErrorNoAuthorizationCode() throws ApiException {

        // Checks for "multiple authorizations found Error"

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

        context.setAuthorizationCode("");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());

        assertTrue(apiException.getResponseBody().contains("multiple authorizations found"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorWrongAuthorizationCode() throws ApiException {

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

        context.setAuthorizationCode("Wrong");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());

        assertTrue(apiException.getResponseBody().contains("authorization not found"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorPatientIDIsEmpty() throws ApiException {


        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId("");
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Patient-ID is empty"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorPatientIDIsNull() throws ApiException {


        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId(null);
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("must not be null"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorPatientIDIsWrong() throws ApiException {
        // Really hacky solution

        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId("Wrong");
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("ID does not exists"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorCredentialIDIsEmpty() throws ApiException {


        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId("2512489996");
        iti18QueryParameter.setAvailabilityStatus("APPROVED");
        iti18QueryParameter.setDocumentType(Arrays.asList(new String[]{"STABLE", "ON-DEMAND"}));
        iti18QueryParameter.setEndToDate(null);
        iti18QueryParameter.setEndFromDate(null);
        //iti18QueryParameter.setStartFromDate(Long.parseLong("1662131460000"));
        iti18QueryParameter.setStartFromDate(null);
        iti18QueryParameter.setStartToDate(null);


        String credentialID = "";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("CredentialInfo is null"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorCredentialIDIsWrong() throws ApiException {


        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId("2512489996");
        iti18QueryParameter.setAvailabilityStatus("APPROVED");
        iti18QueryParameter.setDocumentType(Arrays.asList(new String[]{"STABLE", "ON-DEMAND"}));
        iti18QueryParameter.setEndToDate(null);
        iti18QueryParameter.setEndFromDate(null);
        //iti18QueryParameter.setStartFromDate(Long.parseLong("1662131460000"));
        iti18QueryParameter.setStartFromDate(null);
        iti18QueryParameter.setStartToDate(null);


        String credentialID = "Wrong";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("CredentialInfo is null"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorNoAvailabilityStatus() throws ApiException {
// This one is going on behind the scenes because @Valid is added to the parameter in the controller.

        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId("2512489996");
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("must not be null"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorNoDocumentType() throws ApiException {
// This one is going on behind the scenes because @Valid is added to the parameter in the controller.

        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setPatientId("2512489996");
        iti18QueryParameter.setAvailabilityStatus("APPROVED");
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("List is null"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorNoContext() throws ApiException {


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


        Iti18Request iti18Request = new Iti18Request();


        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);


        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());

        assertTrue(apiException.getResponseBody().contains("context must not be null"));
    }

    @Test
    public void testV1Iti18PostControllerThrowErrorNoConsentOverride() throws ApiException {


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
        context.setRole("User");

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);


        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Consent override must be a valid boolean value"));


    }

    @Test
    public void testV1Iti18PostControllerThrowErrorNoRole() throws ApiException {


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

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);


        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("UserRole must be specified"));
    }

    @Test
    public void testV1Iti18PostControllerThrowsErrorNoQueryParameters() throws ApiException {


        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);


        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("queryParameters must not be null"));
    }

// -----------------Iti43 tests---------------------------------------------------- \\

    @Test
    public void testV1Iti43PostController() throws ApiException {

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
    public void testV1Iti43PostControllerThrowsErrorNoAuthorizationCode() throws ApiException {

        // Checks for "multiple authorizations found Error" TODO should make a test for all possible failures

        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("7a65a7d3-75f9-4ddf-b60a-f1e3078077c8");
        queryParameters.setRepositoryId("1.2.208.192.100.101");
        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti43Request iti43Request = new Iti43Request();

        iti43Request.setContext(context);
        iti43Request.setCredentialId(credentialID);
        iti43Request.setQueryParameters(queryParameters);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("multiple authorizations found"));

    }

    @Test
    public void testV1Iti43PostControllerThrowsErrorWrongAuthorizationCode() throws ApiException {


        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("7a65a7d3-75f9-4ddf-b60a-f1e3078077c8");
        queryParameters.setRepositoryId("1.2.208.192.100.101");
        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("Wrong");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti43Request iti43Request = new Iti43Request();

        iti43Request.setContext(context);
        iti43Request.setCredentialId(credentialID);
        iti43Request.setQueryParameters(queryParameters);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("authorization not found"));


    }

    @Test
    public void testV1Iti43PostControllerThrowsErrorDocumentIDIsNull() throws ApiException {

        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        //queryParameters.setDocumentId("7a65a7d3-75f9-4ddf-b60a-f1e3078077c8");
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("queryParameters.documentId must not be null"));

    }

    @Test
    public void testV1Iti43PostControllerThrowsErrorNoDocumentID() throws ApiException {

        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("");
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Document unique ID is empty"));

    }

    @Test
    public void testV1Iti43PostControllerThrowsErrorWrongDocumentID() throws ApiException {

        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("Wrong");
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

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Failed to retrieve document"));

    }

    @Test
    public void testV1Iti43PostControllerThrowsErrorRepositoryIDIsNull() throws ApiException {

        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("7a65a7d3-75f9-4ddf-b60a-f1e3078077c8");
        queryParameters.setRepositoryId(null);
        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti43Request iti43Request = new Iti43Request();

        iti43Request.setContext(context);
        iti43Request.setCredentialId(credentialID);
        iti43Request.setQueryParameters(queryParameters);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("queryParameters.repositoryId must not be null"));

    }

    @Test
    public void testV1Iti43PostControllerThrowsErrorRepositoryIDIsEmpty() throws ApiException {

        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("7a65a7d3-75f9-4ddf-b60a-f1e3078077c8");
        queryParameters.setRepositoryId("");
        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti43Request iti43Request = new Iti43Request();

        iti43Request.setContext(context);
        iti43Request.setCredentialId(credentialID);
        iti43Request.setQueryParameters(queryParameters);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Repository ID is empty"));

    }

    @Test
    public void testV1Iti43PostControllerThrowsErrorRepositoryIDIsWrong() throws ApiException {

        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        queryParameters.setDocumentId("7a65a7d3-75f9-4ddf-b60a-f1e3078077c8");
        queryParameters.setRepositoryId("Wrong");
        String credentialID = "D:9038f177-d345-4c42-b2b4-6e27314e713e";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();

        context.setAuthorizationCode("NS363");
        context.setConsentOverride(false);
        context.setRole("User");

        Iti43Request iti43Request = new Iti43Request();

        iti43Request.setContext(context);
        iti43Request.setCredentialId(credentialID);
        iti43Request.setQueryParameters(queryParameters);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti43PostWithHttpInfo(iti43Request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Repository identified by Repository Unique Id"));

    }

// -----------------Iti41  upload---------------------------------------------------- \\

    @Test
    public void testv1Iti41UploadPostController() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("D:" + "9038f177-d345-4c42-b2b4-6e27314e714f");


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


        // Generates new UUID for test purpose
        String newUUID = UUID.randomUUID().toString();
        String newdata = iti41UploadRequest.getXmlInformation().replace("74b28be0-4948-11ed-b878-0242aceel00f2", newUUID);
        iti41UploadRequest.setXmlInformation(newdata);

// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName("");
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId(newUUID);

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        var result = ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest);

        assertEquals(200, result.getStatusCode());

    }

    @Test
    public void testv1Iti41UploadPostControllerThrowsErrorFailedToUploadDocument() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("D:" + "9038f177-d345-4c42-b2b4-6e27314e714f");


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName(null);
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId("1.2.208.184^74b28be0-4948-11ed-b878-0242aceel00f2");

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("XDSDuplicateUniqueIdInRegistry"));

    }

    @Test
    public void testv1Iti41UploadPostControllerThrowsErrorXMLIsNull() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("D:" + "9038f177-d345-4c42-b2b4-6e27314e714f");


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation(null);
// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName(null);
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId("1.2.208.184^74b28be0-4948-11ed-b878-0242aceel00f2");

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("XML file is empty"));

    }

    @Test
    public void testv1Iti41UploadPostControllerThrowsErrorXMLIsEmpty() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("D:" + "9038f177-d345-4c42-b2b4-6e27314e714f");


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation("");
// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName(null);
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId("1.2.208.184^74b28be0-4948-11ed-b878-0242aceel00f2");

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("XML file is empty"));

    }


    @Test
    public void testv1Iti41UploadPostControllerThrowsErrorXMLIsWrong() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("D:" + "9038f177-d345-4c42-b2b4-6e27314e714f");


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation("Wrong");
// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName(null);
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId("1.2.208.184^74b28be0-4948-11ed-b878-0242aceel00f2");

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Cant parse xml document Unexpected character"));

    }

    @Test
    public void testv1Iti41UploadPostControllerThrowsErrorCertificateIsEmpty() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("");


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


        // Generates new UUID for test purpose
        String newUUID = UUID.randomUUID().toString();
        String newdata = iti41UploadRequest.getXmlInformation().replace("74b28be0-4948-11ed-b878-0242aceel00f2", newUUID);
        iti41UploadRequest.setXmlInformation(newdata);

// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName("");
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId(newUUID);

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Credentials do not exist"));

    }

    @Test
    public void testv1Iti41UploadPostControllerThrowsErrorCertificateIsNull() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID(null);


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


        // Generates new UUID for test purpose
        String newUUID = UUID.randomUUID().toString();
        String newdata = iti41UploadRequest.getXmlInformation().replace("74b28be0-4948-11ed-b878-0242aceel00f2", newUUID);
        iti41UploadRequest.setXmlInformation(newdata);

// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName("");
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId(newUUID);

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Credentials do not exist"));

    }
    @Test
    public void testv1Iti41UploadPostControllerThrowsErrorCertificateIsWrong() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41UploadRequest iti41UploadRequest = new Iti41UploadRequest();

        iti41UploadRequest.setCertificateID("Wrong");


        iti41UploadRequest.setRepository(iti41Repository);

        iti41UploadRequest.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


        // Generates new UUID for test purpose
        String newUUID = UUID.randomUUID().toString();
        String newdata = iti41UploadRequest.getXmlInformation().replace("74b28be0-4948-11ed-b878-0242aceel00f2", newUUID);
        iti41UploadRequest.setXmlInformation(newdata);

// Set response data
        ResponseMetaData responseMetaData = new ResponseMetaData();

        Code authorIns = new Code();
        authorIns.setName("Aftale Ansvarlige Organisation");
        authorIns.setCode("325431000016004");
        authorIns.setScheme("1.2.208.176.1.1");
        responseMetaData.setAuthorInstitution(authorIns);

        GeneratedMetaDataAuthorPerson authorPerson = new GeneratedMetaDataAuthorPerson();
        authorPerson.setGivenName("Jens");
        authorPerson.setFamilyName("Jensen");
        authorPerson.setSecondAndFurtherGivenNames("Bo&Henrik");
        responseMetaData.setAuthorPerson(authorPerson);

        Code classCode = new Code();
        classCode.setName("Clinical report");
        classCode.setCode("001");
        classCode.setScheme("1.2.208.184.100.9");

        responseMetaData.setClassCode(classCode);
        Code confiCode = new Code();
        confiCode.setName("");
        confiCode.setCode("N");
        confiCode.setScheme("2.16.840.1.113883.5.25");

        responseMetaData.setConfidentialityCode(confiCode);

        responseMetaData.setCreationTime("20220909071100");

        List<Code> eventCodes = new ArrayList<>();

        Code eCode1 = new Code();
        eCode1.setName("Graviditet, fødsel og barsel");
        eCode1.setCode("ALAL51");
        eCode1.setScheme("1.2.208.176.2.4");

        eventCodes.add(eCode1);

        responseMetaData.setEventCode(eventCodes);

        Code formatCode = new Code();
        formatCode.setName("APD DK schema");
        formatCode.setCode("urn:ad:dk:medcom:apd-v2.0.1:full");
        formatCode.setScheme("1.2.208.184.100.10");

        responseMetaData.setFormatCode(formatCode);

        responseMetaData.setLanguageCode("da-DK");

        responseMetaData.setLegalAuthenticator(null);

        Code patientId = new Code();
        patientId.setName("");
        patientId.setCode("2906910651");
        patientId.setScheme("1.2.208.176.1.2");
        responseMetaData.setPatientId(patientId);

        responseMetaData.setServiceStartTime("20221001080000");
        responseMetaData.setServiceStopTime("20221001083000");

        Code sourcePatientId = new Code();
        sourcePatientId.setName("");
        sourcePatientId.setCode("2906910651");
        sourcePatientId.setScheme("1.2.208.176.1.2");

        responseMetaData.setSourcePatientId(sourcePatientId);

        GeneratedMetaDataSourcePatientInfo sourcePatientInfo = new GeneratedMetaDataSourcePatientInfo();
        sourcePatientInfo.setFamilyName("Andersen");
        sourcePatientInfo.setGivenName("AndersMEGETMEGETMEGETMEGETMEGETMEGETMEGETLANGTNAVN");
        sourcePatientInfo.setSecondAndFurtherGivenNames("Aftaler");
        sourcePatientInfo.setGender(GeneratedMetaDataSourcePatientInfo.GenderEnum.M);
        sourcePatientInfo.setBirthTime("19910629");

        responseMetaData.setSourcePatientInfo(sourcePatientInfo);

        responseMetaData.setTitle("Aftale for 2906910651");


        Code typeCode = new Code();
        typeCode.setName("Dato og tidspunkt for møde mellem patient og sundhedsperson");
        typeCode.setCode("39289-4");
        typeCode.setScheme("2.16.840.1.113883.6.1");
        responseMetaData.setTypeCode(typeCode);

        responseMetaData.setUniqueId(newUUID);

        Code objectType = new Code();
        objectType.setName("Stable");
        objectType.setCode("STABLE");
        objectType.setScheme(" ");
        responseMetaData.objectType(objectType);

        Code availabilityStatus = new Code();
        availabilityStatus.setName("Approved");
        availabilityStatus.setCode("APPROVED");
        availabilityStatus.setScheme(" ");

        responseMetaData.setAvailabilityStatus(availabilityStatus);


        Code health = new Code();
        health.setName("N/A");
        health.setCode("N/A");
        health.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setHealthcareFacilityTypeCode(health);

        Code practiceCode = new Code();
        practiceCode.setName("N/A");
        practiceCode.setCode("N/A");
        practiceCode.setScheme("2.16.840.1.113883.6.96");
        responseMetaData.setPracticeSetting(practiceCode);

        responseMetaData.setSubmissionTime(OffsetDateTime.parse("2022-12-06T13:02:02.94Z"));
        iti41UploadRequest.setResponseMetaData(responseMetaData);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41UploadPostWithHttpInfo(iti41UploadRequest));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("Credentials do not exist"));

    }

// ---------------------------- Upload Choose xml and Repository-----------------------\\
    @Test
    public void testv1Iti41PreviewUploadPostController() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41PreviewRequest request = new Iti41PreviewRequest();
        request.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


        var result = ihexdsApi.v1Iti41PreviewUploadPostWithHttpInfo(request);

        assertEquals(200, result.getStatusCode());

    }
// TODO Once repo matetrs
   /* @Test
    public void testv1Iti41PreviewUploadPostControllerThrowsErrorRepositoryIsNull() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41PreviewRequest request = new Iti41PreviewRequest();
        request.setXmlInformation(Files.readString(Paths.get(getClass().getClassLoader().getResource("xml/DK-APD_Example_1_2_apd_maximum.xml").toURI())));


        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41PreviewUploadPostWithHttpInfo(request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        System.out.println(apiException.getResponseBody());
        //assertTrue(apiException.getResponseBody().contains("Credentials do not exist"));

    }
    */
    @Test

    public void testv1Iti41PreviewUploadPostControllerThrowsErrorXMLIsNull() throws ApiException, URISyntaxException, IOException {

        Iti41Repository iti41Repository = new Iti41Repository();
        iti41Repository.setDisplayName("DROS");
        iti41Repository.setPath("DROS");

        Iti41PreviewRequest request = new Iti41PreviewRequest();
        request.setXmlInformation(null);

        ApiException apiException = assertThrows(ApiException.class, () -> ihexdsApi.v1Iti41PreviewUploadPostWithHttpInfo(request));
        assertEquals(HttpStatus.SC_BAD_REQUEST, apiException.getCode());
        assertTrue(apiException.getResponseBody().contains("XML file is empty"));

    }

    @Test
    public void testv1Iti41RepositoriesGet() throws ApiException {

        var result = ihexdsApi.v1Iti41RepositoriesGetWithHttpInfo();
        assertEquals(200, result.getStatusCode());
        assertEquals(1, result.getData().size());

    }
}
