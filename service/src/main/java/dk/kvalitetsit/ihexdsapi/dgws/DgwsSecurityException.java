package dk.kvalitetsit.ihexdsapi.dgws;

public class DgwsSecurityException extends Exception {

	public DgwsSecurityException(Exception cause) {
		super(cause);
	}

    public DgwsSecurityException(String message) {
		super(message);
    }
}
