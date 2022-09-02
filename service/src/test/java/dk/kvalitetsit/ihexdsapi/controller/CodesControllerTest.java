package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.service.CodesService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.impl.DgwsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openapitools.model.Code;
import org.openapitools.model.Iti18Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CodesControllerTest {

    private CodesService codesService;
    private CodesController subject;

    @Before
    public void setup() {
        codesService = Mockito.mock(CodesService.class);
        subject = new CodesController(codesService);
    }

    @Test
    public void Testv1CodesTypeCodeGet()  {

        // When
        ResponseEntity<List<Code>> responseEntity = subject.v1CodesTypeCodeGet();

        // Then
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());

    }

}
