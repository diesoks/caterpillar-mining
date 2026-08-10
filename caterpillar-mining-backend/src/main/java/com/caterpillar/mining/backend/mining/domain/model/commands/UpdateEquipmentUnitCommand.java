package com.caterpillar.mining.backend.mining.domain.model.commands;

import com.caterpillar.mining.backend.mining.domain.model.valueobjects.OperationStatus;
import com.caterpillar.mining.backend.shared.domain.model.valueobjects.GeoCoordinate;

/**
 * Command to update an existing mining equipment unit.
 *
 * @param id  the surrogate ID of the equipment unit to update; must be greater than 0
 * @param model             the equipment model; must not be blank
 * @param serialNumber      the equipment serial number; must not be blank
 * @param operationStatus   the operational status; must not be {@code null}
 * @param assignedMineSite  the mine site the unit is assigned to; must not be blank
 * @param gpsLocation       the current GPS location; must not be {@code null}
 * @param hoursOfOperation  the accumulated hours of operation; must not be negative
 * @author Diego Vilca
 */
public record UpdateEquipmentUnitCommand(
        Long id,
        String model,
        String serialNumber,
        OperationStatus operationStatus,
        String assignedMineSite,
        GeoCoordinate gpsLocation,
        int hoursOfOperation) {

    /**
     * Validates the command's fields.
     *
     * @throws IllegalArgumentException if any required field is missing or invalid
     */
    public UpdateEquipmentUnitCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Equipment unit ID must be greater than 0.");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model must not be blank.");
        }
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException("Serial number must not be blank.");
        }
        if (operationStatus == null) {
            throw new IllegalArgumentException("Operation status must not be null.");
        }
        if (assignedMineSite == null || assignedMineSite.isBlank()) {
            throw new IllegalArgumentException("Assigned mine site must not be blank.");
        }
        if (gpsLocation == null) {
            throw new IllegalArgumentException("GPS location must not be null.");
        }
        if (hoursOfOperation < 0) {
            throw new IllegalArgumentException("Hours of operation must not be negative.");
        }
    }
}
