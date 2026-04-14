package com.example.etudiants.controller;

import com.example.etudiants.dto.DepartementDTO;
import com.example.etudiants.service.DepartementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Départements", description = "API de gestion des départements")
public class DepartementController {

    private final DepartementService departementService;

    @Operation(summary = "Liste tous les départements")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<List<DepartementDTO>> getAllDepartements() {
        return ResponseEntity.ok(departementService.findAll());
    }

    @Operation(summary = "Récupère un département par ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Département trouvé"),
        @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartementDTO> getDepartementById(@PathVariable Long id) {
        return ResponseEntity.ok(departementService.findById(id));
    }

    @Operation(summary = "Crée un nouveau département")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Département créé avec succès")
    })
    @PostMapping
    public ResponseEntity<DepartementDTO> createDepartement(@RequestBody DepartementDTO dto) {
        DepartementDTO created = departementService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Met à jour un département existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Département mis à jour"),
        @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DepartementDTO> updateDepartement(
            @PathVariable Long id,
            @RequestBody DepartementDTO dto) {
        return ResponseEntity.ok(departementService.update(id, dto));
    }

    @Operation(summary = "Supprime un département")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Département supprimé"),
        @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        departementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}