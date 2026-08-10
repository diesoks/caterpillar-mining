package com.caterpillar.mining.backend.mining.interfaces.rest;

import com.caterpillar.mining.backend.mining.domain.model.commands.DeleteEquipmentUnitCommand;
import com.caterpillar.mining.backend.mining.domain.model.queries.GetAllEquipmentUnitsQuery;
import com.caterpillar.mining.backend.mining.domain.model.queries.GetEquipmentUnitByIdQuery;
import com.caterpillar.mining.backend.mining.domain.services.EquipmentUnitCommandService;
import com.caterpillar.mining.backend.mining.domain.services.EquipmentUnitQueryService;
import com.caterpillar.mining.backend.mining.interfaces.rest.resources.CreateEquipmentUnitResource;
import com.caterpillar.mining.backend.mining.interfaces.rest.resources.EquipmentUnitResource;
import com.caterpillar.mining.backend.mining.interfaces.rest.resources.UpdateEquipmentUnitResource;
import com.caterpillar.mining.backend.mining.interfaces.rest.transform.CreateEquipmentUnitCommandFromResourceAssembler;
import com.caterpillar.mining.backend.mining.interfaces.rest.transform.EquipmentUnitResourceFromEntityAssembler;
import com.caterpillar.mining.backend.mining.interfaces.rest.transform.UpdateEquipmentUnitCommandFromResourceAssembler;
import com.caterpillar.mining.backend.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing the mining equipment units endpoints.
 *
 * @author Diego Vilca
 */
@RestController
@RequestMapping(value = "/api/v1/equipment-units", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Equipment Units", description = "Endpoints for managing Caterpillar mining equipment units")
public class EquipmentUnitsController {

    private final EquipmentUnitCommandService equipmentUnitCommandService;
    private final EquipmentUnitQueryService equipmentUnitQueryService;

    /**
     * Creates a new controller instance.
     *
     * @param equipmentUnitCommandService the {@link EquipmentUnitCommandService} instance
     * @param equipmentUnitQueryService   the {@link EquipmentUnitQueryService} instance
     */
    public EquipmentUnitsController(
            EquipmentUnitCommandService equipmentUnitCommandService,
            EquipmentUnitQueryService equipmentUnitQueryService) {
        this.equipmentUnitCommandService = equipmentUnitCommandService;
        this.equipmentUnitQueryService = equipmentUnitQueryService;
    }

    /**
     * Registers a new mining equipment unit.
     *
     * @param resource the {@link CreateEquipmentUnitResource} describing the unit to register
     * @return the created {@link EquipmentUnitResource}, with {@code 201 Created}
     */
    @PostMapping
    @Operation(summary = "Register a new mining equipment unit",
            description = "Registers a new mining equipment unit, generating its business identifier automatically.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipment unit created"),
            @ApiResponse(responseCode = "400", description = "Validation error (invalid GPS coordinates, invalid operation status, or a blank required field)"),
            @ApiResponse(responseCode = "409", description = "An equipment unit with the same serial number is already registered at the same mine site")})
    public ResponseEntity<EquipmentUnitResource> createEquipmentUnit(@RequestBody CreateEquipmentUnitResource resource) {
        var command = CreateEquipmentUnitCommandFromResourceAssembler.toCommandFromResource(resource);
        var equipmentUnit = equipmentUnitCommandService.handle(command);
        var equipmentUnitResource = EquipmentUnitResourceFromEntityAssembler.toResourceFromEntity(equipmentUnit);
        return new ResponseEntity<>(equipmentUnitResource, HttpStatus.CREATED);
    }

    /**
     * Gets a mining equipment unit by its surrogate ID.
     *
     * @param equipmentUnitId the surrogate ID
     * @return the {@link EquipmentUnitResource}, with {@code 200 OK}, or {@code 404 Not Found}
     */
    @GetMapping("/{equipmentUnitId}")
    @Operation(summary = "Get a mining equipment unit by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment unit found"),
            @ApiResponse(responseCode = "404", description = "Equipment unit not found")})
    public ResponseEntity<EquipmentUnitResource> getEquipmentUnitById(@PathVariable Long equipmentUnitId) {
        var query = new GetEquipmentUnitByIdQuery(equipmentUnitId);
        var equipmentUnit = equipmentUnitQueryService.handle(query);
        if (equipmentUnit.isEmpty()) return ResponseEntity.notFound().build();
        var equipmentUnitResource = EquipmentUnitResourceFromEntityAssembler.toResourceFromEntity(equipmentUnit.get());
        return ResponseEntity.ok(equipmentUnitResource);
    }

    /**
     * Gets all mining equipment units.
     *
     * @return the list of {@link EquipmentUnitResource}, with {@code 200 OK} (possibly empty)
     */
    @GetMapping
    @Operation(summary = "Get all mining equipment units")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Equipment units retrieved")})
    public ResponseEntity<List<EquipmentUnitResource>> getAllEquipmentUnits() {
        var equipmentUnits = equipmentUnitQueryService.handle(new GetAllEquipmentUnitsQuery());
        var equipmentUnitResources = equipmentUnits.stream()
                .map(EquipmentUnitResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(equipmentUnitResources);
    }

    /**
     * Updates an existing mining equipment unit.
     *
     * @param equipmentUnitId the surrogate ID of the unit to update
     * @param resource        the {@link UpdateEquipmentUnitResource} describing the new data
     * @return the updated {@link EquipmentUnitResource}, with {@code 200 OK}
     */
    @PutMapping("/{equipmentUnitId}")
    @Operation(summary = "Update an existing mining equipment unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment unit updated"),
            @ApiResponse(responseCode = "400", description = "Validation error (invalid GPS coordinates, invalid operation status, or a blank required field)"),
            @ApiResponse(responseCode = "404", description = "Equipment unit not found"),
            @ApiResponse(responseCode = "409", description = "Another equipment unit with the same serial number is already registered at the same mine site")})
    public ResponseEntity<EquipmentUnitResource> updateEquipmentUnit(
            @PathVariable Long equipmentUnitId, @RequestBody UpdateEquipmentUnitResource resource) {
        var command = UpdateEquipmentUnitCommandFromResourceAssembler.toCommandFromResource(equipmentUnitId, resource);
        var equipmentUnit = equipmentUnitCommandService.handle(command);
        var equipmentUnitResource = EquipmentUnitResourceFromEntityAssembler.toResourceFromEntity(equipmentUnit);
        return ResponseEntity.ok(equipmentUnitResource);
    }

    /**
     * Deletes an existing mining equipment unit.
     *
     * @param equipmentUnitId the surrogate ID of the unit to delete
     * @return a {@link MessageResource} confirming the deletion, with {@code 200 OK}
     */
    @DeleteMapping("/{equipmentUnitId}")
    @Operation(summary = "Delete an existing mining equipment unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment unit deleted"),
            @ApiResponse(responseCode = "404", description = "Equipment unit not found")})
    public ResponseEntity<MessageResource> deleteEquipmentUnit(@PathVariable Long equipmentUnitId) {
        equipmentUnitCommandService.handle(new DeleteEquipmentUnitCommand(equipmentUnitId));
        return ResponseEntity.ok(new MessageResource(
                "Mining equipment unit with ID %d was deleted successfully.".formatted(equipmentUnitId)));
    }
}
