package com.caterpillar.mining.backend.mining.domain.services;

import com.caterpillar.mining.backend.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.mining.backend.mining.domain.model.queries.GetAllEquipmentUnitsQuery;
import com.caterpillar.mining.backend.mining.domain.model.queries.GetEquipmentUnitByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for mining equipment units.
 *
 * @author Diego Vilca
 */
public interface EquipmentUnitQueryService {

    /**
     * Handles a request to get a mining equipment unit by its surrogate ID.
     *
     * @param query the {@link GetEquipmentUnitByIdQuery} containing the ID
     * @return the {@link MiningEquipmentUnit} if found, otherwise empty
     */
    Optional<MiningEquipmentUnit> handle(GetEquipmentUnitByIdQuery query);

    /**
     * Handles a request to get all mining equipment units.
     *
     * @param query the {@link GetAllEquipmentUnitsQuery}
     * @return the list of all {@link MiningEquipmentUnit} instances
     */
    List<MiningEquipmentUnit> handle(GetAllEquipmentUnitsQuery query);
}
