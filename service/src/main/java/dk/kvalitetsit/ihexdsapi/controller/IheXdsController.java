package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import dk.kvalitetsit.ihexdsapi.dgws.*;
import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.service.*;
import org.jdom2.JDOMException;
import org.openapitools.api.*;
import org.openapitools.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
//@CrossOrigin(origins = "http://localhost:*")
public class IheXdsController implements IhexdsApi, RequestResultApi, ResponseResultApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(IheXdsController.class);

    private Iti18Service iti18Service;

    private Iti43Service iti43Service;

    private DgwsService dgwsService;

    private CacheRequestResponseHandle cacheRequestResponseHandle;


    private IDContextService iDContextService;


    private Iti41Service iti41Service;


    private Iti41RepositoriesService iti41RepositoriesService;


    public IheXdsController(DgwsService dgwsService, Iti18Service iti18Service,
                            CacheRequestResponseHandle cacheRequestResponseHandle,
                            IDContextService iDContextService, Iti43Service iti43Service, Iti41Service iti41Service, Iti41RepositoriesService iti41RepositoriesService, UploadService uploadService) {
        this.dgwsService = dgwsService;
        this.iti18Service = iti18Service;
        this.cacheRequestResponseHandle = cacheRequestResponseHandle;
        this.iDContextService = iDContextService;
        this.iti43Service = iti43Service;
        this.iti41Service = iti41Service;
        this.iti41RepositoriesService = iti41RepositoriesService;
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
    public ResponseEntity<Iti41PreviewResponse> v1Iti41PreviewUploadPost(Iti41PreviewRequest iti41PreviewRequest) {

        try {
            Iti41PreviewResponse response = new Iti41PreviewResponse();
            response.setXmlInformation(iti41PreviewRequest.getXmlInformation());
            response.setGeneratedMetadata(iti41Service.getGeneratedMetaData(iti41PreviewRequest.getXmlInformation()));
            response.setRepository(iti41PreviewRequest.getRepository());

            ResponseEntity<Iti41PreviewResponse> responseEntity = new ResponseEntity(response, HttpStatus.OK);
            // TODO Handle execptions correctly
            return responseEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ResponseEntity<List<Iti41Repository>> v1Iti41RepositoriesGet() {
        try {
            Collection<Iti41Repository> repositories = iti41RepositoriesService.getRepositories();
            ResponseEntity<List<Iti41Repository>> responseEntity = new ResponseEntity(repositories, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            // Get proper error
            //throw new RuntimeException(e);
            return null;
        }

    }


    @Override
    public ResponseEntity<Iti41UploadResponse> v1Iti41UploadPost(Iti41UploadRequest iti41UploadRequest) {
		/*HealthcareProfessionalContext context = new HealthcareProfessionalContext();

		context.setAuthorizationCode("NS363");
		context.setConsentOverride(false);
		context.setRole("User");*/
        try {
            DgwsClientInfo clientInfo = dgwsService.getSystemClientInfo(iti41UploadRequest.getCertificateID());
            //moces
		/*	DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo("", iti41UploadRequest.getCertificateID(), context);
			System.out.println(clientInfo);*/
            Iti41UploadResponse response = iti41Service.doUpload(iti41UploadRequest.getXmlInformation(), clientInfo);

        } catch (DgwsSecurityException e) {
            System.out.println(e);
        }

        System.out.println();


        return null;
    }

    @Override
    public ResponseEntity<Iti43Response> v1Iti43Post(Iti43Request iti43Request) {
        try {
            DgwsClientInfo clientInfo = dgwsService.getHealthCareProfessionalClientInfo(iti43Request.getQueryParameters().getPatientId(), iti43Request.getCredentialId(), iti43Request.getContext());
            Iti43Response iti43Response = iti43Service.getDocument(iti43Request.getQueryParameters(), clientInfo);
            return new ResponseEntity<>(iti43Response, HttpStatus.OK);
        } catch (DgwsSecurityException e) {

            throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
            //throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
        } catch (Iti43Exception e) {
            List<String> errors = new ArrayList<>();
            for (RegistryError err : e.getOtherErrors()) {
                errors.add("" + err.getCodeContext() + ", " + err.getErrorCode() + ", " + err.getSeverity() + ", " + err.getCustomErrorCode());
            }

            throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage(), errors);
        }
    }


    @Override
    public ResponseEntity<DownloadLog> v1RequestRequestIdGet(String requestId) {
        String result = null;

        try {
            result = cacheRequestResponseHandle.getRequestAndResponse(requestId);
            DownloadLog response = new DownloadLog();
            response.setPayload(result);
            return new ResponseEntity<DownloadLog>(response, HttpStatus.OK);

        } catch (Exception e) {
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

        } catch (Exception e) {
            //throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
            throw e;
        }
    }


}
