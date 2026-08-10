package com.caterpillar.mining.backend.mining.domain.exceptions;

import com.caterpillar.mining.backend.shared.domain.exceptions.ResourceNotFoundException;

/**
 * Exception thrown when a requested mining equipment unit does not exist.
 *
 * @author Diego Vilca
 */
public class EquipmentUnitNotFoundException extends ResourceNotFoundException {

    /**
     * Creates a new exception describing the missing equipment unit.
     *
     * @param equipmentUnitId the surrogate ID that could not be found
     */
    public EquipmentUnitNotFoundException(Long equipmentUnitId) {
        super("Mining equipment unit with ID %d not found.".formatted(equipmentUnitId));
    }
}
