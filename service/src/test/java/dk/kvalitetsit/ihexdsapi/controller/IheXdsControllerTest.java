package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.impl.DgwsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openapitools.model.HealthcareProfessionalContext;
import org.openapitools.model.Iti18HealthCareProfessionalRequest;
import org.openapitools.model.Iti18QueryParameter;
import org.openapitools.model.Iti18Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


public class IheXdsControllerTest {


    DgwsService dgwsService;
    Iti18Service iti18Service;
    IheXdsController subject;

    @Before
    public void setup() {
        // Configure mocks senere
        this.dgwsService = Mockito.mock(DgwsServiceImpl.class);
        this.iti18Service = Mockito.mock(Iti18Service.class);


        subject = new IheXdsController(dgwsService, iti18Service);
    }

   @Test
    public void testv1Iti18HealthcareProfessionalGet() {
        // Given
        Iti18HealthCareProfessionalRequest request = new Iti18HealthCareProfessionalRequest();
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();
        context.setActingUserId("0804569723");
        context.setAuthorizationCode("CBNH1");
        context.setOrganisationCode("293591000016003");
        context.setConsentOverride(false);
        request.setContext(context);
        Iti18QueryParameter queryParameters = new Iti18QueryParameter();
        queryParameters.setPatientId("2512489996");
        request.setQueryParameters(queryParameters);
        request.setCredentialId("DEFAULT");

        // When
        ResponseEntity<List<Iti18Response>> responseEntity = subject.v1Iti18HealthcareProfessionalGet(request);

        // Then
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }
}
