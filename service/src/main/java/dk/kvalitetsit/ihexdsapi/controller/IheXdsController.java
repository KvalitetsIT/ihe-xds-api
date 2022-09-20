package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import org.openapitools.api.*;
import org.openapitools.model.Code;
import org.openapitools.model.Document;
import org.openapitools.model.Iti18Request;
import org.openapitools.model.Iti18Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
//@CrossOrigin(origins = "http://localhost:*")
public class IheXdsController  implements IhexdsApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(IheXdsController.class);

	private Iti18Service iti18Service;

	private DgwsService dgwsService;

    public IheXdsController(DgwsService dgwsService, Iti18Service iti18Service) {
        this.dgwsService = dgwsService;
		this.iti18Service = iti18Service;
    }

	@Override
	public ResponseEntity<List<Iti18Response>> v1Iti18Post(@Valid Iti18Request iti18Request) {
		System.out.println(iti18Request.getContext());

			//dgwsService.getHealthCareProfessionalClientInfo()

		try {
			DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo(iti18Request.getQueryParameters().getPatientId(), iti18Request.getCredentialId(), iti18Request.getContext());
			List<Iti18Response> iti18Response = iti18Service.queryForDocument(iti18Request.getQueryParameters(), clientInfo);

		} catch (DgwsSecurityException e) {
			throw new RuntimeException(e);
		}
		//

			// Generate 3 responses
			Iti18Response res = null;
			List<Iti18Response> iti18Response = new LinkedList<>();

			for (int i = 1; i <= 3; i++) {
				Document document = new Document();
				document.setProp1("Data for set: " + i);
				res = new Iti18Response();

				res.setDocument(document);
				res.setDocumentType("Approved");
				res.setRepositoryID("Some ID for: " + i );
				res.setPatientId("patient: " + i);
				res.setDocumentId("Some ID for: " + i);
				res.setServiceStart(OffsetDateTime.parse("2007-12-03T10:15:30Z"));
				res.setServiceEnd(OffsetDateTime.parse("2007-12-07T10:15:30Z"));

				iti18Response.add(res);
			}

			return new ResponseEntity<List<Iti18Response>>(iti18Response, HttpStatus.OK);
		/*catch (DgwsSecurityException e) {
			System.out.println(iti18Request);

			throw new RuntimeException(e);
		}*/
	}



}
