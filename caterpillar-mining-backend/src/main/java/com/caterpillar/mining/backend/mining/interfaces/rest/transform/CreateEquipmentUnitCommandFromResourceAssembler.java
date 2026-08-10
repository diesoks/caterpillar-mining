package com.caterpillar.mining.backend.mining.interfaces.rest.transform;

import com.caterpillar.mining.backend.mining.domain.model.commands.CreateEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.model.valueobjects.OperationStatus;
import com.caterpillar.mining.backend.mining.interfaces.rest.resources.CreateEquipmentUnitResource;
import com.caterpillar.mining.backend.shared.domain.model.valueobjects.GeoCoordinate;

/**
 * Assembler to convert a {@link CreateEquipmentUnitResource} into a {@link CreateEquipmentUnitCommand}.
 *
 * @author Diego Vilca
 */
public class CreateEquipmentUnitCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateEquipmentUnitResource} into a {@link CreateEquipmentUnitCommand}.
     * <p>
     * This is where the {@code operationStatus} string is parsed into an {@link OperationStatus}
     * value object and the GPS coordinates are validated by constructing a {@link GeoCoordinate}
     * - both operations throw {@link IllegalArgumentException} on invalid input.
     * </p>
     *
     * @param resource the {@link CreateEquipmentUnitResource} to convert
     * @return the resulting {@link CreateEquipmentUnitCommand}
     */
    public static CreateEquipmentUnitCommand toCommandFromResource(CreateEquipmentUnitResource resource) {
        var operationStatus = OperationStatus.fromValue(resource.operationStatus());
        var gpsLocation = new GeoCoordinate(resource.gpsLatitude(), resource.gpsLongitude());
        return new CreateEquipmentUnitCommand(
                resource.model(),
                resource.serialNumber(),
                operationStatus,
                resource.assignedMineSite(),
                gpsLocation,
                resource.hoursOfOperation());
    }
}
