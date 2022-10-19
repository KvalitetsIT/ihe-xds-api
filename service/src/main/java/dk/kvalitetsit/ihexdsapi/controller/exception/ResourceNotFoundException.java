package dk.kvalitetsit.ihexdsapi.controller.exception;

import org.openapitools.model.BasicError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;
import java.util.List;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public enum ERROR_CODE {
        GENERIC(1);

        private final int errorCode;

        ERROR_CODE(int errorCode) {
            this.errorCode = errorCode;
        }
    }

    private final BasicError error;

    private ResourceNotFoundException(BasicError error) {
        this.error = error;
    }

    public static ResourceNotFoundException createException(String message) {
        var error = new BasicError();
        error.setError(message);
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setStatusText(HttpStatus.NOT_FOUND.getReasonPhrase());
        error.setTimestamp(OffsetDateTime.now());
        error.setErrorCode(ERROR_CODE.GENERIC.errorCode);

        return new ResourceNotFoundException(error);
    }
    public static ResourceNotFoundException createException(String message, List<String> externalErrors) {
        var error = new BasicError();
        error.setError(message);
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setStatusText(HttpStatus.NOT_FOUND.getReasonPhrase());
        error.setTimestamp(OffsetDateTime.now());
        error.setErrorCode(ERROR_CODE.GENERIC.errorCode);
        error.setOtherError(externalErrors);

        return new ResourceNotFoundException(error);
    }

    public BasicError getError() {
        return error;
    }
}
