package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CreateCredentialRequest;
import org.openapitools.model.CredentialInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
@CrossOrigin(origins = "http://localhost")
public class CredentialInfoController implements CredentialsApi {

    @Autowired
    private CredentialService credentialService;

@Override
    public ResponseEntity<List<CredentialInfoResponse>> v1CredentialinfoGet(String owner) {

        Collection<String> ids = credentialService.getIds(owner);
        List<CredentialInfoResponse> responses = new LinkedList<>();
        for (String id : ids) {
            CredentialInfoResponse credentialInfoResponse = new CredentialInfoResponse();
            credentialInfoResponse.setId(id);
            responses.add(credentialInfoResponse);
        }
        ResponseEntity<List<CredentialInfoResponse>> responseEntity = new ResponseEntity(responses, HttpStatus.OK);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Void> v1CredentialinfoPut(CreateCredentialRequest createCredentialResponse) {


        try {
            CredentialInfo  credential = credentialService.createAndAddCredentialInfo(
                    createCredentialResponse.getOwner(), createCredentialResponse.getId(),
                    createCredentialResponse.getCvr(), createCredentialResponse.getOrganisation(),
                    createCredentialResponse.getPublicCertStr(), createCredentialResponse.getPrivateKeyStr());
            return  ResponseEntity.created(null).body(null);

        } catch (DgwsSecurityException e) {
            throw BadRequestException.createException(BadRequestException.ERROR_CODE.fromInt(e.getErrorCode()), e.getMessage());
        }
    }


}
