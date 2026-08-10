package com.caterpillar.mining.backend.mining.domain.services;

import com.caterpillar.mining.backend.mining.domain.exceptions.DuplicateEquipmentSerialNumberException;
import com.caterpillar.mining.backend.mining.domain.exceptions.EquipmentUnitNotFoundException;
import com.caterpillar.mining.backend.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.mining.backend.mining.domain.model.commands.CreateEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.model.commands.DeleteEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.model.commands.UpdateEquipmentUnitCommand;

/**
 * Command service for mining equipment units.
 *
 * @author Diego Vilca
 */
public interface EquipmentUnitCommandService {

    /**
     * Handles the registration of a new mining equipment unit.
     *
     * @param command the {@link CreateEquipmentUnitCommand} containing the equipment unit data
     * @return the created {@link MiningEquipmentUnit}
     * @throws DuplicateEquipmentSerialNumberException if the serial number is already registered
     *                                                  within the same mine site
     */
    MiningEquipmentUnit handle(CreateEquipmentUnitCommand command);

    /**
     * Handles the update of an existing mining equipment unit.
     *
     * @param command the {@link UpdateEquipmentUnitCommand} containing the updated data
     * @return the updated {@link MiningEquipmentUnit}
     * @throws EquipmentUnitNotFoundException          if no unit exists with the given ID
     * @throws DuplicateEquipmentSerialNumberException if the new serial number is already
     *                                                  registered by another unit within the
     *                                                  same mine site
     */
    MiningEquipmentUnit handle(UpdateEquipmentUnitCommand command);

    /**
     * Handles the deletion of an existing mining equipment unit.
     *
     * @param command the {@link DeleteEquipmentUnitCommand} identifying the unit to delete
     * @throws EquipmentUnitNotFoundException if no unit exists with the given ID
     */
    void handle(DeleteEquipmentUnitCommand command);
}
