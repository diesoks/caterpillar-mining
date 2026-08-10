package com.caterpillar.mining.backend.mining.domain.model.queries;

/**
 * Query to get a mining equipment unit by its surrogate ID.
 *
 * @param id the surrogate ID; must be greater than 0
 * @author Diego Vilca
 */
public record GetEquipmentUnitByIdQuery(Long id) {

    /**
     * Validates the query's fields.
     *
     * @throws IllegalArgumentException if the ID is missing or invalid
     */
    public GetEquipmentUnitByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Equipment unit ID must be greater than 0.");
        }
    }
}
