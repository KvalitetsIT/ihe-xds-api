package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.service.ConfigsService;
import org.openapitools.api.ConfigApi;
import org.openapitools.model.ConfigResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class ConfigurationController implements ConfigApi {

    private ConfigsService configsService;

    public ConfigurationController(ConfigsService configsService) {
        this.configsService = configsService;
    }
    @Override
    public ResponseEntity<List<ConfigResponse>> v1ConfigGet() {
        try {
            Collection<ConfigResponse> responses = configsService.getListOfConfigResponses();
            ResponseEntity<List<ConfigResponse>> responseEntity = new ResponseEntity(responses, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }
}
