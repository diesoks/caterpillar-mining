package com.caterpillar.mining.backend.shared.interfaces.rest.resources;

/**
 * Error resource returned in the body of error responses across all bounded contexts.
 *
 * @param message   a human-readable description of the error
 * @param status    the HTTP status code associated with the error
 * @param timestamp an ISO-8601 timestamp of when the error occurred
 * @author Diego Vilca
 */
public record ErrorResource(String message, int status, String timestamp) {
}
