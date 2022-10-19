package dk.kvalitetsit.ihexdsapi.dgws;

public class DgwsSecurityException extends AbstractServiceException {

	public DgwsSecurityException(Exception cause, int errorCode, String message) {
		super(cause, errorCode, message);
	}

	public DgwsSecurityException(int errorCode, String message) {
		super(errorCode, message);
	}
}
