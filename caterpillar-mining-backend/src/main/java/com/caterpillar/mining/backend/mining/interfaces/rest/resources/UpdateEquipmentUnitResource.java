package com.caterpillar.mining.backend.mining.interfaces.rest.resources;

/**
 * Resource for updating an existing mining equipment unit.
 *
 * @param model             the equipment model
 * @param serialNumber      the equipment serial number
 * @param operationStatus   the operational status, as a string (e.g. {@code "ACTIVE"})
 * @param assignedMineSite  the mine site the unit is assigned to
 * @param gpsLatitude       the current GPS latitude
 * @param gpsLongitude      the current GPS longitude
 * @param hoursOfOperation  the accumulated hours of operation
 * @author Diego Vilca
 */
public record UpdateEquipmentUnitResource(
        String model,
        String serialNumber,
        String operationStatus,
        String assignedMineSite,
        double gpsLatitude,
        double gpsLongitude,
        int hoursOfOperation) {

    /**
     * Validates the resource's fields.
     *
     * @throws IllegalArgumentException if any required field is missing or invalid
     */
    public UpdateEquipmentUnitResource {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model is required.");
        }
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException("Serial number is required.");
        }
        if (operationStatus == null || operationStatus.isBlank()) {
            throw new IllegalArgumentException("Operation status is required.");
        }
        if (assignedMineSite == null || assignedMineSite.isBlank()) {
            throw new IllegalArgumentException("Assigned mine site is required.");
        }
        if (hoursOfOperation < 0) {
            throw new IllegalArgumentException("Hours of operation must not be negative.");
        }
    }
}
