package dk.kvalitetsit.ihexdsapi.service;

public class CodesExecption extends Exception{
    private int errorCode;
    private String message;
    public CodesExecption(Exception cause, int errorCode, String message) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }public CodesExecption(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public CodesExecption(String message) {
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

