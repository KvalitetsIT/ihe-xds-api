package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.dgws.Iti43Exception;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.IDContextService;
import dk.kvalitetsit.ihexdsapi.service.Iti43Service;
import org.openapitools.api.*;
import org.openapitools.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
//@CrossOrigin(origins = "http://localhost:*")
public class IheXdsController  implements IhexdsApi,  RequestResultApi, ResponseResultApi  {

	private static final Logger LOGGER = LoggerFactory.getLogger(IheXdsController.class);

	private Iti18Service iti18Service;

	private Iti43Service iti43Service;

	private DgwsService dgwsService;

	private CacheRequestResponseHandle cacheRequestResponseHandle;



	private IDContextService iDContextService;



    public IheXdsController(DgwsService dgwsService, Iti18Service iti18Service,
							CacheRequestResponseHandle cacheRequestResponseHandle,
							IDContextService iDContextService, Iti43Service iti43Service) {
        this.dgwsService = dgwsService;
		this.iti18Service = iti18Service;
		this.cacheRequestResponseHandle = cacheRequestResponseHandle;
		this.iDContextService = iDContextService;
		this.iti43Service = iti43Service;
    }

	@Override
	public ResponseEntity<Iti18Response> v1Iti18Post(@Valid Iti18Request iti18Request) {
		try {
			DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo(iti18Request.getQueryParameters().getPatientId(), iti18Request.getCredentialId(), iti18Request.getContext());
			Iti18Response iti18Response = iti18Service.queryForDocument(iti18Request.getQueryParameters(), clientInfo);
			iti18Response.setResponseId(iDContextService.getId("tempRes"));
			iti18Response.setRequestId(iDContextService.getId("tempReq"));


			return new ResponseEntity<>(iti18Response, HttpStatus.OK);
		} catch (DgwsSecurityException e) {
			throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
		}
	}
// TODO Better naming
	@Override
	public ResponseEntity<Iti18ResponseUnique> v1Iti18UniqueIDPost(Iti18RequestUnique iti18RequestUnique) {
		try {
			System.out.println(iti18RequestUnique);
			DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo(
					iti18RequestUnique.getQueryParameters().getPatientId(),
					iti18RequestUnique.getQueryParameters().getCredentialId(),
					iti18RequestUnique.getQueryParameters().getContext());
			Iti18ResponseUnique response = iti18Service.queryForDocument(iti18RequestUnique, clientInfo);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DgwsSecurityException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public ResponseEntity<Iti43Response> v1Iti43Post(Iti43Request iti43Request) {
		try {
			DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo(iti43Request.getQueryParameters().getPatientId(), iti43Request.getCredentialId(), iti43Request.getContext());
			String resp = iti43Service.getDocument(iti43Request.getQueryParameters(), clientInfo);
			Iti43Response iti43Response = new Iti43Response();
			iti43Response.setResponse(resp);
			return new ResponseEntity<>(iti43Response, HttpStatus.OK);
		}catch (DgwsSecurityException e) {
			throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
			//throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
		} catch (Iti43Exception e) {
			throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
		}
	}

	/* ResponseEntity<Iti41Response> v1Iti43Post(Iti41Request iti41Request) {
	DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo();

	}
	 */




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
