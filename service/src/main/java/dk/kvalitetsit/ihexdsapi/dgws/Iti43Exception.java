package dk.kvalitetsit.ihexdsapi.dgws;

public class Iti43Exception extends AbstractServiceException{

    public Iti43Exception(Exception cause, int errorCode, String message) {
        super(cause, errorCode, message);
    }

}
