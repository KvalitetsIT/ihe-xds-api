package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.controller.exception.BadRequestException;
import dk.kvalitetsit.ihexdsapi.controller.exception.ResourceNotFoundException;
import org.openapitools.model.BasicError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BasicError> handleBadRequest(BadRequestException e, HttpServletRequest request) {
        var error = e.getError();
        error.setPath(request.getRequestURI());
        System.out.println(error);

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BasicError> handleBadRequest(ResourceNotFoundException e, HttpServletRequest request) {
        var error = e.getError();
        error.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BasicError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        BasicError error = new BasicError();

        error.setPath(request.getRequestURI());
        error.setErrorCode(1000);
        error.setStatus(400);
        error.setStatusText("Bad Request");
        error.setTimestamp( OffsetDateTime.now());

        error.setError(e.getBindingResult().getFieldError().getField() +
                " " + e.getBindingResult().getFieldError().getDefaultMessage());

        return ResponseEntity
                .badRequest()
                .body(error);
    }

  @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BasicError> handleRuntimeErrors(RuntimeException e, HttpServletRequest request) {
        BasicError error = new BasicError();
        if (e.getMessage().contains("ID does not exists")) {

            error.setPath(request.getRequestURI());
            error.setErrorCode(1000);
            error.setStatus(400);
            error.setStatusText("Bad Request");
            error.setTimestamp(OffsetDateTime.now());

            error.setError("Patient-" + e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }

      error.setError(e.getMessage());
      error.setPath(request.getRequestURI());
      error.setErrorCode(1000);
      error.setStatus(400);
      error.setStatusText("Bad Request");
      error.setTimestamp(OffsetDateTime.now());


      return ResponseEntity
              .badRequest()
              .body(error);

    }

}
