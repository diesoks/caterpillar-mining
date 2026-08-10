package com.caterpillar.mining.backend.mining.domain.model.aggregates;

import com.caterpillar.mining.backend.mining.domain.model.valueobjects.CatMiningEquipmentUnitId;
import com.caterpillar.mining.backend.mining.domain.model.valueobjects.OperationStatus;
import com.caterpillar.mining.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.caterpillar.mining.backend.shared.domain.model.valueobjects.GeoCoordinate;
import com.caterpillar.mining.backend.shared.infrastructure.persistence.jpa.converters.SerialNumberEncryptionConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * MiningEquipmentUnit aggregate root.
 * <p>
 * Represents a single piece of Caterpillar mining equipment tracked by the platform: its model,
 * serial number, operational status, assigned mine site, current GPS location and accumulated
 * hours of operation. It belongs to the {@code mining} bounded context.
 * </p>
 *
 * @author Diego Vilca
 */
@Getter
@Entity
public class MiningEquipmentUnit extends AuditableAbstractAggregateRoot<MiningEquipmentUnit> {

    @Embedded
    private CatMiningEquipmentUnitId equipmentUnitId;

    @Column(nullable = false)
    private String model;

    /**
     * Stored encrypted at rest (see {@link SerialNumberEncryptionConverter}); transparently
     * decrypted to plain text whenever the entity is loaded. Deliberately not marked
     * {@code unique = true}: because encryption uses a random initialization vector per value,
     * equal plain-text serial numbers never produce equal ciphertext, so a database-level unique
     * constraint on this column would be meaningless. The "same serial number within the same
     * mine site" business rule is instead enforced in the application layer by comparing the
     * already-decrypted values of candidate rows scoped to a single {@code assignedMineSite}.
     */
    @Convert(converter = SerialNumberEncryptionConverter.class)
    @Column(nullable = false, length = 512)
    private String serialNumber;

    @Embedded
    private OperationStatus operationStatus;

    @Column(nullable = false)
    private String assignedMineSite;

    @Embedded
    private GeoCoordinate gpsLocation;

    @Column(nullable = false)
    private int hoursOfOperation;

    /**
     * Required by JPA.
     */
    public MiningEquipmentUnit() {
        this.equipmentUnitId = new CatMiningEquipmentUnitId();
    }

    /**
     * Creates a new mining equipment unit, generating its business identifier automatically.
     *
     * @param model            the equipment model
     * @param serialNumber     the equipment serial number
     * @param operationStatus  the operational status
     * @param assignedMineSite the mine site the unit is assigned to
     * @param gpsLocation      the current GPS location
     * @param hoursOfOperation the accumulated hours of operation
     */
    public MiningEquipmentUnit(
            String model,
            String serialNumber,
            OperationStatus operationStatus,
            String assignedMineSite,
            GeoCoordinate gpsLocation,
            int hoursOfOperation) {
        this();
        this.model = model;
        this.serialNumber = serialNumber;
        this.operationStatus = operationStatus;
        this.assignedMineSite = assignedMineSite;
        this.gpsLocation = gpsLocation;
        this.hoursOfOperation = hoursOfOperation;
    }

    /**
     * Updates this unit's information. The surrogate {@code id} and the business
     * {@code equipmentUnitId} are never changed.
     *
     * @param model            the equipment model
     * @param serialNumber     the equipment serial number
     * @param operationStatus  the operational status
     * @param assignedMineSite the mine site the unit is assigned to
     * @param gpsLocation      the current GPS location
     * @param hoursOfOperation the accumulated hours of operation
     * @return this instance, updated
     */
    public MiningEquipmentUnit updateInformation(
            String model,
            String serialNumber,
            OperationStatus operationStatus,
            String assignedMineSite,
            GeoCoordinate gpsLocation,
            int hoursOfOperation) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.operationStatus = operationStatus;
        this.assignedMineSite = assignedMineSite;
        this.gpsLocation = gpsLocation;
        this.hoursOfOperation = hoursOfOperation;
        return this;
    }
}
