package dk.kvalitetsit.ihexdsapi.service;

import org.openapitools.model.Iti18Request;
import org.openapitools.model.Iti18Response;

public interface Iti18Service {

	Iti18Response queryForDocument(Iti18Request iti18Request);

}
