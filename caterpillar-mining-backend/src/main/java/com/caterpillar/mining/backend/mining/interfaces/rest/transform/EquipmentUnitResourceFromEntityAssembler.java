package com.caterpillar.mining.backend.mining.interfaces.rest.transform;

import com.caterpillar.mining.backend.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.mining.backend.mining.interfaces.rest.resources.EquipmentUnitResource;

/**
 * Assembler to convert a {@link MiningEquipmentUnit} entity into an {@link EquipmentUnitResource}.
 *
 * @author Diego Vilca
 */
public class EquipmentUnitResourceFromEntityAssembler {

    /**
     * Converts a {@link MiningEquipmentUnit} entity into an {@link EquipmentUnitResource}.
     *
     * @param entity the {@link MiningEquipmentUnit} to convert
     * @return the resulting {@link EquipmentUnitResource}
     */
    public static EquipmentUnitResource toResourceFromEntity(MiningEquipmentUnit entity) {
        return new EquipmentUnitResource(
                entity.getId(),
                entity.getEquipmentUnitId().equipmentUnitId().toString(),
                entity.getModel(),
                entity.getSerialNumber(),
                entity.getOperationStatus().getStringValue(),
                entity.getAssignedMineSite(),
                entity.getGpsLocation().latitude(),
                entity.getGpsLocation().longitude(),
                entity.getHoursOfOperation(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
