package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import org.openapitools.model.Iti18QueryParameter;
import org.openapitools.model.Iti18RequestUnique;
import org.openapitools.model.Iti18Response;
import org.openapitools.model.Iti18ResponseUnique;

import java.util.List;

public interface Iti18Service {

	Iti18Response queryForDocument(Iti18QueryParameter iti18Request, DgwsClientInfo dgwsClientInfo) throws DgwsSecurityException, ItiException;
	Iti18ResponseUnique queryForDocument(Iti18RequestUnique iti18RequestUnique, DgwsClientInfo dgwsClientInfo) throws DgwsSecurityException;

}
