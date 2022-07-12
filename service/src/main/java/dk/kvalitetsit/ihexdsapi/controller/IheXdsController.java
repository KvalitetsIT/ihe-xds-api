package dk.kvalitetsit.ihexdsapi.controller;

import org.openapitools.api.IhexdsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
@CrossOrigin(origins = "http://localhost")
public class IheXdsController implements IhexdsApi {
    private static final Logger logger = LoggerFactory.getLogger(IheXdsController.class);
    private final IheXdsService helloService;

    public IheXdsController(IheXdsService helloService) {
        this.helloService = helloService;
    }

}
