package com.caterpillar.mining.backend.mining.domain.model.aggregates;

import com.caterpillar.mining.backend.mining.domain.model.valueobjects.OperationStatus;
import com.caterpillar.mining.backend.mining.domain.model.valueobjects.OperationStatusEnum;
import com.caterpillar.mining.backend.shared.domain.model.valueobjects.GeoCoordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MiningEquipmentUnit} aggregate root, following the
 * Arrange-Act-Assert pattern.
 *
 * @author Diego Vilca
 */
class MiningEquipmentUnitTest {

    @Test
    void constructorSetsAllProvidedFields() {
        // Arrange
        var operationStatus = new OperationStatus(OperationStatusEnum.ACTIVE);
        var gpsLocation = new GeoCoordinate(-9.5417, -77.0619);

        // Act
        var equipmentUnit = new MiningEquipmentUnit(
                "Cat 793F", "SN-793F-0001", operationStatus, "Antamina", gpsLocation, 1200);

        // Assert
        assertEquals("Cat 793F", equipmentUnit.getModel());
        assertEquals("SN-793F-0001", equipmentUnit.getSerialNumber());
        assertEquals(operationStatus, equipmentUnit.getOperationStatus());
        assertEquals("Antamina", equipmentUnit.getAssignedMineSite());
        assertEquals(gpsLocation, equipmentUnit.getGpsLocation());
        assertEquals(1200, equipmentUnit.getHoursOfOperation());
    }

    @Test
    void constructorGeneratesUniqueNonNullEquipmentUnitId() {
        // Arrange
        var operationStatus = new OperationStatus(OperationStatusEnum.ACTIVE);
        var gpsLocation = new GeoCoordinate(-9.5417, -77.0619);

        // Act
        var firstUnit = new MiningEquipmentUnit(
                "Cat 793F", "SN-793F-0001", operationStatus, "Antamina", gpsLocation, 1200);
        var secondUnit = new MiningEquipmentUnit(
                "Cat 793F", "SN-793F-0001", operationStatus, "Antamina", gpsLocation, 1200);

        // Assert
        assertNotNull(firstUnit.getEquipmentUnitId());
        assertNotNull(secondUnit.getEquipmentUnitId());
        assertNotEquals(firstUnit.getEquipmentUnitId(), secondUnit.getEquipmentUnitId());
    }

    @Test
    void updateInformationUpdatesFieldsButKeepsIdentity() {
        // Arrange
        var equipmentUnit = new MiningEquipmentUnit(
                "Cat 793F", "SN-793F-0001", new OperationStatus(OperationStatusEnum.ACTIVE),
                "Antamina", new GeoCoordinate(-9.5417, -77.0619), 1200);
        var originalEquipmentUnitId = equipmentUnit.getEquipmentUnitId();
        var updatedStatus = new OperationStatus(OperationStatusEnum.IN_MAINTENANCE);
        var updatedLocation = new GeoCoordinate(-16.5, -71.5);

        // Act
        var result = equipmentUnit.updateInformation(
                "Cat 785D", "SN-785D-0002", updatedStatus, "Cerro Verde", updatedLocation, 350);

        // Assert
        assertEquals("Cat 785D", equipmentUnit.getModel());
        assertEquals("SN-785D-0002", equipmentUnit.getSerialNumber());
        assertEquals(updatedStatus, equipmentUnit.getOperationStatus());
        assertEquals("Cerro Verde", equipmentUnit.getAssignedMineSite());
        assertEquals(updatedLocation, equipmentUnit.getGpsLocation());
        assertEquals(350, equipmentUnit.getHoursOfOperation());
        assertEquals(originalEquipmentUnitId, equipmentUnit.getEquipmentUnitId());
        assertSame(equipmentUnit, result);
    }
}
