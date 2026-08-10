package com.caterpillar.mining.backend.mining.interfaces.rest.resources;

import java.util.Date;

/**
 * Resource representing a mining equipment unit in API responses.
 *
 * @param id                the surrogate primary key
 * @param equipmentUnitId   the business identifier, as a string (UUID)
 * @param model             the equipment model
 * @param serialNumber      the equipment serial number, decrypted
 * @param operationStatus   the operational status, as a string
 * @param assignedMineSite  the mine site the unit is assigned to
 * @param gpsLatitude       the current GPS latitude
 * @param gpsLongitude      the current GPS longitude
 * @param hoursOfOperation  the accumulated hours of operation
 * @param createdAt         the creation timestamp
 * @param updatedAt         the last update timestamp
 * @author Diego Vilca
 */
public record EquipmentUnitResource(
        Long id,
        String equipmentUnitId,
        String model,
        String serialNumber,
        String operationStatus,
        String assignedMineSite,
        double gpsLatitude,
        double gpsLongitude,
        int hoursOfOperation,
        Date createdAt,
        Date updatedAt) {
}
