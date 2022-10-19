package dk.kvalitetsit.ihexdsapi.dgws;

import org.openapitools.model.RegistryError;

import java.util.List;

public abstract class AbstractServiceException extends Exception{

    private int errorCode;
    private String message;

    private List<RegistryError> otherErrors;
    public AbstractServiceException(Exception cause, int errorCode, String message) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }public AbstractServiceException(Exception cause, int errorCode, String message, List<RegistryError> otherErrors) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
        this.otherErrors = otherErrors;
    }public AbstractServiceException( int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
    public AbstractServiceException(int errorCode, String message, List<RegistryError> otherErrors) {
        this.errorCode = errorCode;
        this.message = message;
        this.otherErrors = otherErrors;
    }

    public AbstractServiceException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public List<RegistryError> getOtherErrors() {return  otherErrors;}
}

