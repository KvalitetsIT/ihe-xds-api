package dk.kvalitetsit.ihexdsapi.controller.exception;

import dk.kvalitetsit.ihexdsapi.utility.ValutGenerator;
import org.openapitools.model.BasicError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public enum ERROR_CODE {
    INVALID_CERT(1),
    INVALID_KEY(2),
    EXISTING_CREDENTIAL_ID(3),
    GENERIC(1000);

    private final int errorCode;

    private static final Logger LOGGER = LoggerFactory.getLogger(BadRequestException.class);



        ERROR_CODE(int errorCode) {
        this.errorCode = errorCode;
    }
        private final static Map<Integer, ERROR_CODE> intToMap = new HashMap<>();
        static {
            for (ERROR_CODE type : ERROR_CODE.values()) {
                intToMap.put(type.errorCode, type);
            }
        }

        public static ERROR_CODE fromInt(int i) {
            ERROR_CODE type = intToMap.get(Integer.valueOf(i));
            return type;
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
    } public static BadRequestException createException(ERROR_CODE errorCode, String errorMessage, List<String> errors) {
        var error = new BasicError();
        error.setError(errorMessage);
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setStatusText(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setTimestamp(OffsetDateTime.now());
        error.setErrorCode(errorCode.errorCode);
        error.setOtherError(errors);

        return new BadRequestException(error);
    }


}
