package dk.kvalitetsit.ihexdsapi.controller;

import javax.validation.Valid;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import org.openapitools.api.IhexdsApi;
import org.openapitools.model.Iti18Request;
import org.openapitools.model.Iti18Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import dk.kvalitetsit.ihexdsapi.service.Iti18Service;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
@CrossOrigin(origins = "http://localhost")
public class IheXdsController implements IhexdsApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(IheXdsController.class);

	private Iti18Service iti18Service;
	
    public IheXdsController(Iti18Service iti18Service) {
        this.iti18Service = iti18Service;
    }

	@Override
	public ResponseEntity<Iti18Response> v1Iti18Get(@Valid Iti18Request iti18Request) {
		LOGGER.info("iti18 mere meningsfyldt TODO");
		try {
			Iti18Response iti18Response = iti18Service.queryForDocument(iti18Request);
			return new ResponseEntity<Iti18Response>(iti18Response, HttpStatus.OK);
		} catch (DgwsSecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
