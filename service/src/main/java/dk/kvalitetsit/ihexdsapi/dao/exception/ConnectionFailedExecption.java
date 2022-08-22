package dk.kvalitetsit.ihexdsapi.dao.exception;

import org.openapitools.model.BasicError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;


import org.openapitools.model.BasicError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class ConnectionFailedExecption extends RuntimeException {
        public enum ERROR_CODE {
            GENERIC(1);

            private final int errorCode;

            ERROR_CODE(int errorCode) {
                this.errorCode = errorCode;
            }
        }
        private int errorCode;
        private String message;
        private BasicError error;

        private ConnectionFailedExecption(BasicError error) {
            this.error = error;
        }

        public ConnectionFailedExecption( int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public static ConnectionFailedExecption createException(String message) {
            var error = new BasicError();
            error.setError(message);
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.setStatusText(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            error.setTimestamp(OffsetDateTime.now());
            error.setErrorCode(ConnectionFailedExecption.ERROR_CODE.GENERIC.errorCode);

            return new ConnectionFailedExecption(error);
        }

        public BasicError getError() {
            return error;
        }
    }




