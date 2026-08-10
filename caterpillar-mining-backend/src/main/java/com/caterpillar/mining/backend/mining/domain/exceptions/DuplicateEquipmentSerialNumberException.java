package com.caterpillar.mining.backend.mining.domain.exceptions;

import com.caterpillar.mining.backend.shared.domain.exceptions.DuplicateResourceException;

/**
 * Exception thrown when attempting to register a mining equipment unit whose serial number is
 * already registered within the same assigned mine site.
 *
 * @author Diego Vilca
 */
public class DuplicateEquipmentSerialNumberException extends DuplicateResourceException {

    /**
     * Creates a new exception describing the duplicate serial number.
     *
     * @param serialNumber     the duplicate serial number
     * @param assignedMineSite the mine site where the duplicate was found
     */
    public DuplicateEquipmentSerialNumberException(String serialNumber, String assignedMineSite) {
        super("An equipment unit with serial number '%s' is already registered at mine site '%s'."
                .formatted(serialNumber, assignedMineSite));
    }
}
