package dk.kvalitetsit.ihexdsapi.controller;


import dk.kvalitetsit.ihexdsapi.service.ConfigsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openapitools.model.ConfigResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ConfigurationControllerTest {

    private ConfigsService configsService;
    private ConfigurationController subject;

    @Before
    public void setup() {
        configsService = Mockito.mock(ConfigsService.class);
        subject = new ConfigurationController(configsService);
    }

    @Test
    public void Testv1ConfigGet()  {

        // When
        ResponseEntity<List<ConfigResponse>> responseEntity = subject.v1ConfigGet();

        // Then
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());

    }
}
