package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import org.openapitools.model.Iti18QueryParameter;
import org.openapitools.model.Iti18Response;

import java.util.List;

public interface Iti18Service {

	List<Iti18Response> queryForDocument(Iti18QueryParameter iti18Request, DgwsClientInfo dgwsClientInfo) throws DgwsSecurityException;

}
