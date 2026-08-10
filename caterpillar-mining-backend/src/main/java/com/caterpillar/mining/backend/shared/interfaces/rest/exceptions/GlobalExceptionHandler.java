package com.caterpillar.mining.backend.shared.interfaces.rest.exceptions;

import com.caterpillar.mining.backend.shared.domain.exceptions.DuplicateResourceException;
import com.caterpillar.mining.backend.shared.domain.exceptions.ResourceNotFoundException;
import com.caterpillar.mining.backend.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global exception handler shared by all bounded contexts.
 * <p>
 * Translates domain and framework exceptions into consistent {@link ErrorResource} responses
 * with an appropriate HTTP status code. It intentionally depends only on generic exception
 * categories (such as {@link DuplicateResourceException}), never on bounded-context-specific
 * exception types, so that {@code shared} does not take a dependency on any other bounded context.
 * </p>
 *
 * @author Diego Vilca
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles violations of uniqueness business rules.
     *
     * @param exception the thrown {@link DuplicateResourceException}
     * @return a {@code 409 Conflict} response with the error message
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResource> handleDuplicateResource(DuplicateResourceException exception) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    /**
     * Handles requests targeting a resource that does not exist.
     *
     * @param exception the thrown {@link ResourceNotFoundException}
     * @return a {@code 404 Not Found} response with the error message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResource> handleResourceNotFound(ResourceNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Handles validation failures thrown directly by domain value objects, commands, or resources
     * (e.g. blank required fields, out-of-range values, invalid enum values).
     *
     * @param exception the thrown {@link IllegalArgumentException}
     * @return a {@code 400 Bad Request} response with the error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResource> handleIllegalArgument(IllegalArgumentException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles validation failures that occur while Jackson deserializes the request body, which
     * Spring wraps in {@link HttpMessageNotReadableException}. The original, meaningful message
     * (thrown from within a record's compact constructor) is recovered via the deepest cause in
     * the exception chain, instead of surfacing Jackson's generic parse-error wrapper text.
     *
     * @param exception the thrown {@link HttpMessageNotReadableException}
     * @return a {@code 400 Bad Request} response with the recovered error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResource> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        var rootCause = exception.getMostSpecificCause();
        var message = rootCause != null ? rootCause.getMessage() : "Malformed request body.";
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Handles any unexpected exception that was not otherwise mapped, returning a generic message
     * so internal details are not leaked to API consumers.
     *
     * @param exception the thrown exception
     * @return a {@code 500 Internal Server Error} response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResource> handleUnexpected(Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    private ResponseEntity<ErrorResource> buildResponse(HttpStatus status, String message) {
        var errorResource = new ErrorResource(message, status.value(), Instant.now().toString());
        return ResponseEntity.status(status).body(errorResource);
    }
}
