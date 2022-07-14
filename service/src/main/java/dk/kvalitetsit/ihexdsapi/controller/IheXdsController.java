package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import org.openapitools.api.IhexdsApi;
import org.openapitools.model.Iti18HealthCareProfessionalRequest;
import org.openapitools.model.Iti18Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
@CrossOrigin(origins = "http://localhost")
public class IheXdsController implements IhexdsApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(IheXdsController.class);

	private Iti18Service iti18Service;

	private DgwsService dgwsService;
    public IheXdsController(DgwsService dgwsService, Iti18Service iti18Service) {
        this.dgwsService = dgwsService;
		this.iti18Service = iti18Service;
    }

	@Override
	public ResponseEntity<List<Iti18Response>> v1Iti18HealthcareProfessionalGet(@Valid Iti18HealthCareProfessionalRequest iti18HealthCareProfessionalRequest) {
		LOGGER.info("iti18 mere meningsfyldt TODO");
		try {

			DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo(iti18HealthCareProfessionalRequest.getQueryParameters().getPatientId(), iti18HealthCareProfessionalRequest.getCredentialId(), iti18HealthCareProfessionalRequest.getContext());
			List<Iti18Response> iti18Response = iti18Service.queryForDocument(iti18HealthCareProfessionalRequest.getQueryParameters(), clientInfo);
			return new ResponseEntity<List<Iti18Response>>(iti18Response, HttpStatus.OK);
		} catch (DgwsSecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
