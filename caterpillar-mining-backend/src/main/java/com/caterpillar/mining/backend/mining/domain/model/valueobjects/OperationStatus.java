package com.caterpillar.mining.backend.mining.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * OperationStatus value object.
 * <p>
 * Encapsulates the operational status of a {@code MiningEquipmentUnit} as an enumerated value.
 * Allowed values are {@code ACTIVE}, {@code IN_MAINTENANCE} and {@code INACTIVE}.
 * </p>
 *
 * @param status the underlying operational status enum value; must not be {@code null}
 * @author Diego Vilca
 */
@Embeddable
public record OperationStatus(@Enumerated(EnumType.STRING) OperationStatusEnum status) {

    /**
     * Validates that a status value was provided.
     *
     * @throws IllegalArgumentException if the status is {@code null}
     */
    public OperationStatus {
        if (status == null) {
            throw new IllegalArgumentException("Operation status must not be null.");
        }
    }

    /**
     * Safely parses a client-supplied string into an {@link OperationStatus}.
     *
     * @param value the raw status value, expected to match one of {@link OperationStatusEnum}'s
     *              constants (case-insensitive)
     * @return the parsed {@link OperationStatus}
     * @throws IllegalArgumentException if the value is blank or does not match an allowed status
     */
    public static OperationStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Operation status must not be blank.");
        }
        try {
            return new OperationStatus(OperationStatusEnum.valueOf(value.trim().toUpperCase()));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid operation status '%s'. Allowed values: ACTIVE, IN_MAINTENANCE, INACTIVE.".formatted(value));
        }
    }

    /**
     * Returns the status as its plain string name (e.g. {@code "ACTIVE"}).
     *
     * @return the string representation of the status
     */
    public String getStringValue() {
        return status.name();
    }
}
