package com.caterpillar.mining.backend.shared.domain.exceptions;

/**
 * Generic exception thrown when an operation would create a resource that violates
 * a uniqueness business rule.
 * <p>
 * This exception is intentionally generic so that {@code shared} infrastructure (such as a
 * global exception handler) can map it to an HTTP response without depending on any
 * bounded-context-specific exception type. Bounded contexts should extend this class with
 * a specific exception carrying a descriptive message.
 * </p>
 *
 * @author Diego Vilca
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message the descriptive error message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
