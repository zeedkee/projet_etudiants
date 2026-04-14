package com.example.etudiants.controller;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.service.EtudiantService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Étudiants", description = "API de gestion des étudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;

    @Operation(summary = "Liste tous les étudiants", description = "Retourne tous les étudiants, avec filtre optionnel par année d'inscription")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<List<EtudiantDTO>> getAllEtudiants(
            @RequestParam(required = false) Integer annee) {
        if (annee != null) {
            return ResponseEntity.ok(etudiantService.findByAnnee(annee));
        }
        return ResponseEntity.ok(etudiantService.findAll());
    }

    @Operation(summary = "Récupère un étudiant par ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Étudiant trouvé"),
        @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EtudiantDTO> getEtudiantById(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.findById(id));
    }

    @Operation(summary = "Crée un nouvel étudiant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Étudiant créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<EtudiantDTO> createEtudiant(@RequestBody EtudiantDTO etudiantDTO) {
        EtudiantDTO created = etudiantService.save(etudiantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Met à jour un étudiant existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Étudiant mis à jour"),
        @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EtudiantDTO> updateEtudiant(
            @PathVariable Long id,
            @RequestBody EtudiantDTO etudiantDTO) {
        return ResponseEntity.ok(etudiantService.update(id, etudiantDTO));
    }

    @Operation(summary = "Supprime un étudiant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Étudiant supprimé"),
        @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}