package com.caterpillar.mining.backend.mining.application.internal.queryservices;

import com.caterpillar.mining.backend.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.mining.backend.mining.domain.model.queries.GetAllEquipmentUnitsQuery;
import com.caterpillar.mining.backend.mining.domain.model.queries.GetEquipmentUnitByIdQuery;
import com.caterpillar.mining.backend.mining.domain.services.EquipmentUnitQueryService;
import com.caterpillar.mining.backend.mining.infrastructure.persistence.jpa.repositories.MiningEquipmentUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link EquipmentUnitQueryService}.
 *
 * @author Diego Vilca
 */
@Service
public class EquipmentUnitQueryServiceImpl implements EquipmentUnitQueryService {

    private final MiningEquipmentUnitRepository miningEquipmentUnitRepository;

    /**
     * Creates a new query service instance.
     *
     * @param miningEquipmentUnitRepository the {@link MiningEquipmentUnitRepository} instance
     */
    public EquipmentUnitQueryServiceImpl(MiningEquipmentUnitRepository miningEquipmentUnitRepository) {
        this.miningEquipmentUnitRepository = miningEquipmentUnitRepository;
    }

    // inherited javadoc
    @Override
    public Optional<MiningEquipmentUnit> handle(GetEquipmentUnitByIdQuery query) {
        return miningEquipmentUnitRepository.findById(query.id());
    }

    // inherited javadoc
    @Override
    public List<MiningEquipmentUnit> handle(GetAllEquipmentUnitsQuery query) {
        return miningEquipmentUnitRepository.findAll();
    }
}
