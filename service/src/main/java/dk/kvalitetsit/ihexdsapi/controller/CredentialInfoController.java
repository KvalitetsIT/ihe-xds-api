package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import org.openapitools.api.CredentialInfoApi;
import org.openapitools.model.CredentialInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RestController
// CORS - Consider if this is needed in your application. Only here to make Swagger UI work.
@CrossOrigin(origins = "http://localhost")
public class CredentialInfoController implements CredentialInfoApi {

    @Autowired
    private CredentialService credentialService;

    @Override
    public ResponseEntity<List<CredentialInfoResponse>> v1CredentialinfoGet() {

        Collection<String> ids = credentialService.getIds();
        List<CredentialInfoResponse> responses = new LinkedList<>();
        for (String id : ids) {
            CredentialInfoResponse credentialInfoResponse = new CredentialInfoResponse();
            credentialInfoResponse.setId(id);
            responses.add(credentialInfoResponse);
        }
        ResponseEntity<List<CredentialInfoResponse>> responseEntity = new ResponseEntity(responses, HttpStatus.OK);
        return responseEntity;
    }
}
