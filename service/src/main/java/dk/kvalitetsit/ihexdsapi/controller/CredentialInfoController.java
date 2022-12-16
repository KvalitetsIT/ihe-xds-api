package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.utility.ValutGenerator;
import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CreateCredentialRequest;
import org.openapitools.model.CredentialInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
//@CrossOrigin(origins = "http://localhost:3000", methods =  {RequestMethod.GET,  RequestMethod.PUT})
public class CredentialInfoController implements CredentialsApi {

    @Autowired
    private CredentialService credentialService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialInfoController.class);


    @Override
    public ResponseEntity<List<CredentialInfoResponse>> v1CredentialinfoGet(String owner, String type) {
        LOGGER.info("Owner: " + owner + " Type: " + type);

        CredentialInfoResponse.CredentialTypeEnum typeEnum;
        if (type == null || type.isEmpty()) {
            typeEnum = null;
        } else if (type.equalsIgnoreCase("HEALTHCAREPROFESSIONAL")) {
            typeEnum = CredentialInfoResponse.CredentialTypeEnum.HEALTHCAREPROFESSIONAL;
        } else if (type.equals("SYSTEM")) {
            typeEnum = CredentialInfoResponse.CredentialTypeEnum.SYSTEM;
        } else {
            throw BadRequestException.createException(BadRequestException.ERROR_CODE.GENERIC, "Bad type query");
        }
        try {
        List<CredentialInfoResponse> responses = credentialService.populateResponses(owner, typeEnum);

        ResponseEntity<List<CredentialInfoResponse>> responseEntity = new ResponseEntity(responses, HttpStatus.OK);
        return responseEntity; }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(1000), e.getMessage());

        }
    }

    @Override
    public ResponseEntity<Void> v1CredentialinfoPut(CreateCredentialRequest createCredentialResponse) {


        try {
            credentialService.createAndAddCredentialInfo(
                    createCredentialResponse.getOwner(), createCredentialResponse.getDisplayName(),
                    createCredentialResponse.getPublicCertStr(), createCredentialResponse.getPrivateKeyStr());

            return ResponseEntity.created(null).body(null);

        } catch (DgwsSecurityException e) {
            throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
        }

    }
}
