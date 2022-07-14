package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.controller.IheXdsController;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.service.impl.IheXdsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openapitools.model.HealthcareProfessionalContext;
import org.openapitools.model.Iti18HealthCareProfessionalRequest;
import org.openapitools.model.Iti18QueryParameter;
import org.openapitools.model.Iti18Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.Valid;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {dk.kvalitetsit.ihexdsapi.configuration.TestConfiguration.class})
public class Iti18ServiceTest {

    @Autowired
    DgwsService dgwsService;

    @Autowired
    Iti18Service iti18Service;
    IheXdsController subject;

    @Before
    public void setup() {

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
