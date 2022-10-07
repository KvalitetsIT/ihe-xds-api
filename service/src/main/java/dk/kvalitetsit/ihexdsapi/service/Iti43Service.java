package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import org.openapitools.model.Iti43QueryParameter;

public interface Iti43Service {
    String getDocument(Iti43QueryParameter queryParameter, DgwsClientInfo clientInfo);
}
