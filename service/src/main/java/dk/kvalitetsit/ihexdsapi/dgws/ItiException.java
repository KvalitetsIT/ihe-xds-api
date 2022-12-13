package dk.kvalitetsit.ihexdsapi.dgws;

import org.openapitools.model.RegistryError;

import java.util.List;

public class ItiException extends AbstractServiceException{

    public ItiException(Exception cause, int errorCode, String message) {
        super(cause, errorCode, message);
    }
    public ItiException(Exception cause, int errorCode, String message, List<RegistryError>  otherErrors) {
        super(cause, errorCode, message, otherErrors);
    }

    public ItiException(int errorCode, String message, List<RegistryError> otherErrors) {
        super(errorCode, message, otherErrors);
    }

}
