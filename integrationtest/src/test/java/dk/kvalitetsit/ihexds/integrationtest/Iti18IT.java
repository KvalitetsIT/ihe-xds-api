package dk.kvalitetsit.ihexds.integrationtest;

import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IhexdsApi;
import org.openapitools.client.model.CodeQuery;
import org.openapitools.client.model.HealthcareProfessionalContext;
import org.openapitools.client.model.Iti18QueryParameter;
import org.openapitools.client.model.Iti18Request;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Iti18IT extends AbstractIntegrationTest{

    private IhexdsApi ihexdsApi;

    public Iti18IT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());
        ihexdsApi = new IhexdsApi(apiClient);
    }

    // post
    @Test
    public void testV1Iti18PostController() throws ApiException {

        Iti18QueryParameter iti18QueryParameter = new Iti18QueryParameter();
        iti18QueryParameter.setAvailabilityStatus("APPROVED");
        iti18QueryParameter.setDocumentType(Arrays.asList(new String[]{"STABLE"}));
        iti18QueryParameter.setEndToDate(OffsetDateTime.parse("2022-09-23T15:15:00Z"));
        iti18QueryParameter.setEndFromDate(OffsetDateTime.parse("2022-09-23T15:20:00Z"));
        iti18QueryParameter.setStartToDate(OffsetDateTime.parse("2022-09-28T15:20:00Z"));
        iti18QueryParameter.setStartFromDate(OffsetDateTime.parse("2022-09-28T15:15:00Z"));

        CodeQuery testCode = new CodeQuery();
        testCode.setCode("test");
        testCode.setCodeScheme("test");

        iti18QueryParameter.setEventCode(testCode);
        iti18QueryParameter.setFormatCode(testCode);
        iti18QueryParameter.setHealthcareFacilityTypeCode(testCode);
        iti18QueryParameter.setPracticeSettingCode(testCode);
        iti18QueryParameter.setTypeCode(testCode);

        iti18QueryParameter.setPatientId("Test ID");



        String credentialID = "testID";
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();
        context.setActingUserId("TestID");
        context.setResponsibleUserId("TestID");
        context.setAuthorizationCode("TestID");
        context.setConsentOverride(false);
        context.setOrganisationCode("TestID");


        Iti18Request iti18Request = new Iti18Request();

        iti18Request.setContext(context);
        iti18Request.setCredentialId(credentialID);
        iti18Request.setQueryParameters(iti18QueryParameter);



        var result = ihexdsApi.v1Iti18PostWithHttpInfo(iti18Request);

        assertEquals(200, result.getStatusCode());

        // add result?
    }

}
