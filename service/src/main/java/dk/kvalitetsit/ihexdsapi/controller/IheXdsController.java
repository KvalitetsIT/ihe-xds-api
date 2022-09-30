package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.UtilityService;
import org.openapitools.api.*;
import org.openapitools.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
//@CrossOrigin(origins = "http://localhost:*")
public class IheXdsController  implements IhexdsApi,  RequestResultApi, ResponseResultApi  {

	private static final Logger LOGGER = LoggerFactory.getLogger(IheXdsController.class);

	private Iti18Service iti18Service;

	private DgwsService dgwsService;

	private CacheRequestResponseHandle cacheRequestResponseHandle;

	@Autowired
	private UtilityService utilityService;

    public IheXdsController(DgwsService dgwsService, Iti18Service iti18Service, CacheRequestResponseHandle cacheRequestResponseHandle,
							UtilityService utilityService) {
        this.dgwsService = dgwsService;
		this.iti18Service = iti18Service;
		this.cacheRequestResponseHandle = cacheRequestResponseHandle;
		this.utilityService = utilityService;
    }

	@Override
	public ResponseEntity<Iti18Response> v1Iti18Post(@Valid Iti18Request iti18Request) {
		try {
			DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo(iti18Request.getQueryParameters().getPatientId(), iti18Request.getCredentialId(), iti18Request.getContext());
			Iti18Response iti18Response = iti18Service.queryForDocument(iti18Request.getQueryParameters(), clientInfo);
			iti18Response.setResponseId(utilityService.getId("tempRes"));
			iti18Response.setRequestId(utilityService.getId("tempReq"));
/*
			// Generate 3 responses
			Iti18Response res;
			List<Iti18Response> iti18Responses = new LinkedList<>();

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

				iti18Responses.add(res);
			}
*/
			return new ResponseEntity<Iti18Response>(iti18Response, HttpStatus.OK);
		} catch (DgwsSecurityException e) {
			throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
		}
	}


	@Override
	public ResponseEntity<DownloadLog> v1RequestRequestIdGet(String requestId)  {
		String result = null;

		try {
			result = cacheRequestResponseHandle.getRequestAndResponse(requestId);
			DownloadLog response = new DownloadLog();
			response.setPayload(result);
			return new ResponseEntity<DownloadLog>(response, HttpStatus.OK);

		}
		catch (Exception e) {
			throw e;
			//throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
		}

	}

	@Override
	public ResponseEntity<DownloadLog> v1ResponseResponseIdGet(String responseId) {
		String result = null;

		try {
			result = cacheRequestResponseHandle.getRequestAndResponse(responseId);
			DownloadLog response = new DownloadLog();
			response.setPayload(result);

			// Måske sende 204 code hvis result er tomt.
			return new ResponseEntity<DownloadLog>(response, HttpStatus.OK);

		}
		catch (Exception e) {
			//throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
throw e;		}
	}


}
