package com.caterpillar.mining.backend.mining.interfaces.rest.transform;

import com.caterpillar.mining.backend.mining.domain.model.commands.UpdateEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.model.valueobjects.OperationStatus;
import com.caterpillar.mining.backend.mining.interfaces.rest.resources.UpdateEquipmentUnitResource;
import com.caterpillar.mining.backend.shared.domain.model.valueobjects.GeoCoordinate;

/**
 * Assembler to convert an {@link UpdateEquipmentUnitResource} into an {@link UpdateEquipmentUnitCommand}.
 *
 * @author Diego Vilca
 */
public class UpdateEquipmentUnitCommandFromResourceAssembler {

    /**
     * Converts an {@link UpdateEquipmentUnitResource} into an {@link UpdateEquipmentUnitCommand}.
     *
     * @param id       the surrogate ID of the equipment unit to update
     * @param resource the {@link UpdateEquipmentUnitResource} to convert
     * @return the resulting {@link UpdateEquipmentUnitCommand}
     */
    public static UpdateEquipmentUnitCommand toCommandFromResource(Long id, UpdateEquipmentUnitResource resource) {
        var operationStatus = OperationStatus.fromValue(resource.operationStatus());
        var gpsLocation = new GeoCoordinate(resource.gpsLatitude(), resource.gpsLongitude());
        return new UpdateEquipmentUnitCommand(
                id,
                resource.model(),
                resource.serialNumber(),
                operationStatus,
                resource.assignedMineSite(),
                gpsLocation,
                resource.hoursOfOperation());
    }
}
