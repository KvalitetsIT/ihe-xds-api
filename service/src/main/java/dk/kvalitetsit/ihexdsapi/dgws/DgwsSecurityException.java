package dk.kvalitetsit.ihexdsapi.dgws;

public class DgwsSecurityException extends Exception {
	private int errorCode;
	private String message;
	public DgwsSecurityException(Exception cause, int errorCode, String message) {
		super(cause);
		this.errorCode = errorCode;
		this.message = message;
	}public DgwsSecurityException( int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

    public DgwsSecurityException(String message) {
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
