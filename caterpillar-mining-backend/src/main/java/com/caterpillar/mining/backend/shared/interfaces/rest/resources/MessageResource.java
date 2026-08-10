package com.caterpillar.mining.backend.shared.interfaces.rest.resources;

/**
 * Generic message resource returned in the body of successful responses that don't return a
 * resource representation (e.g. deletions).
 *
 * @param message a human-readable confirmation message
 * @author Diego Vilca
 */
public record MessageResource(String message) {
}
