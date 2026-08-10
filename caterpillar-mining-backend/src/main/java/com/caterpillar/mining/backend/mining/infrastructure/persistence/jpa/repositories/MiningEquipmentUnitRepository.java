package com.caterpillar.mining.backend.mining.infrastructure.persistence.jpa.repositories;

import com.caterpillar.mining.backend.mining.domain.model.aggregates.MiningEquipmentUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link MiningEquipmentUnit} aggregates.
 *
 * @author Diego Vilca
 */
@Repository
public interface MiningEquipmentUnitRepository extends JpaRepository<MiningEquipmentUnit, Long> {

    /**
     * Finds all equipment units assigned to the given mine site.
     * <p>
     * Used to enforce the "no duplicate serial number within the same mine site" business rule:
     * each returned entity's serial number is transparently decrypted by
     * {@link com.caterpillar.mining.backend.shared.infrastructure.persistence.jpa.converters.SerialNumberEncryptionConverter}
     * during hydration, so callers can safely compare it in plain text.
     * </p>
     *
     * @param assignedMineSite the mine site to search by
     * @return the list of equipment units assigned to that mine site
     */
    List<MiningEquipmentUnit> findByAssignedMineSite(String assignedMineSite);

    /**
     * Finds all equipment units assigned to the given mine site, excluding one specific unit by
     * ID. Used when updating a unit, so it does not flag itself as a duplicate of its own
     * previous serial number.
     *
     * @param assignedMineSite the mine site to search by
     * @param id               the ID to exclude from the results
     * @return the list of matching equipment units
     */
    List<MiningEquipmentUnit> findByAssignedMineSiteAndIdIsNot(String assignedMineSite, Long id);
}
