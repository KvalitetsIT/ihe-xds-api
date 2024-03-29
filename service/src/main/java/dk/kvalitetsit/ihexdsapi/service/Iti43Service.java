package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import org.openapitools.model.Iti43QueryParameter;
import org.openapitools.model.Iti43Response;

public interface Iti43Service {
    Iti43Response getDocument(Iti43QueryParameter queryParameter, DgwsClientInfo clientInfo) throws ItiException;
}
