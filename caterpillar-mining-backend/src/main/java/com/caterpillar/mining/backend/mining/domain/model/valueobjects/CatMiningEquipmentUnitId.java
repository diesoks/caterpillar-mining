package com.caterpillar.mining.backend.mining.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.UUID;

/**
 * CatMiningEquipmentUnitId value object.
 * <p>
 * Represents the business identifier of a {@code MiningEquipmentUnit}. It wraps a non-null
 * {@link UUID} that is generated automatically at registration time and is never supplied
 * by a client.
 * </p>
 *
 * @param equipmentUnitId the underlying UUID; must not be {@code null}
 * @author Diego Vilca
 */
@Embeddable
public record CatMiningEquipmentUnitId(UUID equipmentUnitId) {

    /**
     * Generates a new, random equipment unit identifier.
     */
    public CatMiningEquipmentUnitId() {
        this(UUID.randomUUID());
    }

    /**
     * Validates that an identifier value was provided.
     *
     * @throws IllegalArgumentException if the identifier is {@code null}
     */
    public CatMiningEquipmentUnitId {
        if (equipmentUnitId == null) {
            throw new IllegalArgumentException("Equipment unit ID must not be null.");
        }
    }
}
