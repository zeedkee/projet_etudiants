package com.example.grading.controller;

import com.example.grading.dto.NoteDTO;
import com.example.grading.service.NoteService;
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
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "API de gestion des notes")
public class NoteController {

    private final NoteService noteService;

    @Operation(summary = "Liste toutes les notes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAllNotes(
            @RequestParam(required = false) Long studentId) {
        if (studentId != null) {
            return ResponseEntity.ok(noteService.findByStudentId(studentId));
        }
        return ResponseEntity.ok(noteService.findAll());
    }

    @Operation(summary = "Récupère une note par ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Note trouvée"),
        @ApiResponse(responseCode = "404", description = "Note non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.findById(id));
    }

    @Operation(summary = "Crée une nouvelle note")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Note créée avec succès"),
        @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    })
    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDTO) {
        NoteDTO created = noteService.save(noteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Met à jour une note existante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Note mise à jour"),
        @ApiResponse(responseCode = "404", description = "Note ou étudiant non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(
            @PathVariable Long id,
            @RequestBody NoteDTO noteDTO) {
        return ResponseEntity.ok(noteService.update(id, noteDTO));
    }

    @Operation(summary = "Supprime une note")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Note supprimée"),
        @ApiResponse(responseCode = "404", description = "Note non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}