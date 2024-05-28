package dev.miguelhiguera.chantasy.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    private static final Map<Class<? extends Exception>, ExceptionResponse> EXCEPTION_MAPPINGS = new HashMap<>();

    static {
        EXCEPTION_MAPPINGS.put(BadCredentialsException.class, new ExceptionResponse(HttpStatusCode.valueOf(401), "The username or password is incorrect"));
        EXCEPTION_MAPPINGS.put(AccountStatusException.class, new ExceptionResponse(HttpStatusCode.valueOf(403), "The account is locked"));
        EXCEPTION_MAPPINGS.put(AccessDeniedException.class, new ExceptionResponse(HttpStatusCode.valueOf(403), "You are not authorized to access this resource"));
        EXCEPTION_MAPPINGS.put(SignatureException.class, new ExceptionResponse(HttpStatusCode.valueOf(401), "The JWT signature is invalid"));
        EXCEPTION_MAPPINGS.put(ExpiredJwtException.class, new ExceptionResponse(HttpStatusCode.valueOf(401), "The JWT token has expired"));
        EXCEPTION_MAPPINGS.put(EntityNotFoundException.class, new ExceptionResponse(HttpStatusCode.valueOf(404), "Resource not found"));
        EXCEPTION_MAPPINGS.put(EntityExistsException.class, new ExceptionResponse(HttpStatusCode.valueOf(409), "Resource already exists"));
        EXCEPTION_MAPPINGS.put(RuntimeException.class, new ExceptionResponse(HttpStatusCode.valueOf(400), "Bad request"));
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        exception.printStackTrace();

        ExceptionResponse response = EXCEPTION_MAPPINGS.getOrDefault(
                exception.getClass(),
                new ExceptionResponse(HttpStatusCode.valueOf(500), "Unknown internal server error.")
        );

        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(response.getStatus(), exception.getMessage());
        errorDetail.setProperty("description", response.getDescription());
        return errorDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Getter
    @AllArgsConstructor
    private static class ExceptionResponse {
        private final HttpStatusCode status;
        private final String description;
    }
}