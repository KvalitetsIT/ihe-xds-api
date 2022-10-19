package dk.kvalitetsit.ihexdsapi.dgws;

import org.openapitools.model.RegistryError;

import java.util.List;

public class Iti43Exception extends AbstractServiceException{

    public Iti43Exception(Exception cause, int errorCode, String message) {
        super(cause, errorCode, message);
    }
    public Iti43Exception(Exception cause, int errorCode, String message, List<RegistryError>  otherErrors) {
        super(cause, errorCode, message, otherErrors);
    }

    public Iti43Exception( int errorCode, String message, List<RegistryError> otherErrors) {
        super(errorCode, message, otherErrors);
    }

}
