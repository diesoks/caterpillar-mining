package com.caterpillar.mining.backend.mining.application.internal.commandservices;

import com.caterpillar.mining.backend.mining.domain.exceptions.DuplicateEquipmentSerialNumberException;
import com.caterpillar.mining.backend.mining.domain.exceptions.EquipmentUnitNotFoundException;
import com.caterpillar.mining.backend.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.mining.backend.mining.domain.model.commands.CreateEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.model.commands.DeleteEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.model.commands.UpdateEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.services.EquipmentUnitCommandService;
import com.caterpillar.mining.backend.mining.infrastructure.persistence.jpa.repositories.MiningEquipmentUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link EquipmentUnitCommandService}.
 * <p>
 * Enforces the "no duplicate serial number within the same mine site" business rule at the
 * application layer: since {@code serialNumber} is stored encrypted with a non-deterministic
 * scheme (see {@link com.caterpillar.mining.backend.shared.infrastructure.persistence.jpa.converters.SerialNumberEncryptionConverter}),
 * duplicates cannot be detected via a SQL equality check or a database unique constraint on the
 * ciphertext. Instead, all equipment units already assigned to the target mine site are loaded
 * (their serial numbers arrive already decrypted, since the converter runs transparently during
 * hydration) and compared in memory.
 * </p>
 * <p>
 * <strong>Known limitation:</strong> without a database-level uniqueness guarantee or pessimistic
 * locking, two concurrent requests for the same (mine site, serial number) pair could both read
 * zero matches before either commits, resulting in two duplicate rows. {@code @Transactional}
 * only guarantees atomicity for a single request; it does not serialize concurrent requests
 * against each other. This race condition is accepted as an out-of-scope limitation for this
 * learning exercise; a production-grade fix would add a deterministic HMAC of the serial number
 * in a separate, uniquely-indexed column used only for the uniqueness check.
 * </p>
 *
 * @author Diego Vilca
 */
@Service
public class EquipmentUnitCommandServiceImpl implements EquipmentUnitCommandService {

    private final MiningEquipmentUnitRepository miningEquipmentUnitRepository;

    /**
     * Creates a new command service instance.
     *
     * @param miningEquipmentUnitRepository the {@link MiningEquipmentUnitRepository} instance
     */
    public EquipmentUnitCommandServiceImpl(MiningEquipmentUnitRepository miningEquipmentUnitRepository) {
        this.miningEquipmentUnitRepository = miningEquipmentUnitRepository;
    }

    // inherited javadoc
    @Override
    @Transactional
    public MiningEquipmentUnit handle(CreateEquipmentUnitCommand command) {
        var candidatesAtMineSite = miningEquipmentUnitRepository.findByAssignedMineSite(command.assignedMineSite());
        var duplicateExists = candidatesAtMineSite.stream()
                .anyMatch(existing -> existing.getSerialNumber().equals(command.serialNumber()));
        if (duplicateExists) {
            throw new DuplicateEquipmentSerialNumberException(command.serialNumber(), command.assignedMineSite());
        }
        var equipmentUnit = new MiningEquipmentUnit(
                command.model(),
                command.serialNumber(),
                command.operationStatus(),
                command.assignedMineSite(),
                command.gpsLocation(),
                command.hoursOfOperation());
        return miningEquipmentUnitRepository.save(equipmentUnit);
    }

    // inherited javadoc
    @Override
    @Transactional
    public MiningEquipmentUnit handle(UpdateEquipmentUnitCommand command) {
        var equipmentUnit = miningEquipmentUnitRepository.findById(command.id())
                .orElseThrow(() -> new EquipmentUnitNotFoundException(command.id()));
        var candidatesAtMineSite = miningEquipmentUnitRepository
                .findByAssignedMineSiteAndIdIsNot(command.assignedMineSite(), command.id());
        var duplicateExists = candidatesAtMineSite.stream()
                .anyMatch(existing -> existing.getSerialNumber().equals(command.serialNumber()));
        if (duplicateExists) {
            throw new DuplicateEquipmentSerialNumberException(command.serialNumber(), command.assignedMineSite());
        }
        equipmentUnit.updateInformation(
                command.model(),
                command.serialNumber(),
                command.operationStatus(),
                command.assignedMineSite(),
                command.gpsLocation(),
                command.hoursOfOperation());
        return miningEquipmentUnitRepository.save(equipmentUnit);
    }

    // inherited javadoc
    @Override
    @Transactional
    public void handle(DeleteEquipmentUnitCommand command) {
        if (!miningEquipmentUnitRepository.existsById(command.id())) {
            throw new EquipmentUnitNotFoundException(command.id());
        }
        miningEquipmentUnitRepository.deleteById(command.id());
    }
}
