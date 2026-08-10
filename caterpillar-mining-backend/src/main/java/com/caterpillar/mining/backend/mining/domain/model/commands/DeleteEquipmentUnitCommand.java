package com.caterpillar.mining.backend.mining.domain.model.commands;

/**
 * Command to delete an existing mining equipment unit.
 *
 * @param id the surrogate ID of the equipment unit to delete; must be greater than 0
 * @author Diego Vilca
 */
public record DeleteEquipmentUnitCommand(Long id) {

    /**
     * Validates the command's fields.
     *
     * @throws IllegalArgumentException if the ID is missing or invalid
     */
    public DeleteEquipmentUnitCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Equipment unit ID must be greater than 0.");
        }
    }
}
