package dk.kvalitetsit.ihexdsapi.dgws;

public abstract class AbstractServiceException extends Exception{

    private int errorCode;
    private String message;
    public AbstractServiceException(Exception cause, int errorCode, String message) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }public AbstractServiceException( int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
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
}

