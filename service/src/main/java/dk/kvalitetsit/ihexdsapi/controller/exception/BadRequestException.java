package dk.kvalitetsit.ihexdsapi.controller.exception;

import org.openapitools.model.BasicError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public enum ERROR_CODE {
    INVALID_CERT(1),
    INVALID_KEY(2),
    GENERIC(1000);

    private final int errorCode;

    ERROR_CODE(int errorCode) {
        this.errorCode = errorCode;
    }
    }
    private final BasicError error;
    private BadRequestException(BasicError error) {
        this.error = error;
    }

    public BasicError getError() {
        return error;
    }

    public static BadRequestException createException(ERROR_CODE errorCode, String errorMessage) {
        var error = new BasicError();
        error.setError(errorMessage);
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setStatusText(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setTimestamp(OffsetDateTime.now());
        error.setErrorCode(errorCode.errorCode);

        return new BadRequestException(error);
    }


}
