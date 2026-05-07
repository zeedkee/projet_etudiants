package com.example.grading.service;

import com.example.grading.config.EtudiantClient;
import com.example.grading.dto.NoteDTO;
import com.example.grading.entity.Note;
import com.example.grading.exception.ResourceNotFoundException;
import com.example.grading.mapper.NoteMapper;
import com.example.grading.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final EtudiantClient etudiantClient;

    public List<NoteDTO> findAll() {
        return noteRepository.findAll()
                .stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NoteDTO findById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'id : " + id));
        return noteMapper.toDTO(note);
    }

    public List<NoteDTO> findByStudentId(Long studentId) {
        return noteRepository.findByStudentId(studentId)
                .stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NoteDTO save(NoteDTO dto) {
        // Vérifier que l'étudiant existe via Feign
        verifyStudentExists(dto.getStudentId());

        Note note = noteMapper.toEntity(dto);
        Note saved = noteRepository.save(note);
        return noteMapper.toDTO(saved);
    }

    public NoteDTO update(Long id, NoteDTO dto) {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'id : " + id));

        // Vérifier que l'étudiant existe via Feign
        verifyStudentExists(dto.getStudentId());

        existing.setStudentId(dto.getStudentId());
        existing.setMatiere(dto.getMatiere());
        existing.setValeur(dto.getValeur());

        Note updated = noteRepository.save(existing);
        return noteMapper.toDTO(updated);
    }

    public void delete(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note non trouvée avec l'id : " + id);
        }
        noteRepository.deleteById(id);
    }

    /**
     * Appelle etudiant-service via Feign pour vérifier que l'étudiant existe.
     * Si le service est indisponible ou l'étudiant introuvable, lance une exception.
     */
    private void verifyStudentExists(Long studentId) {
        try {
            etudiantClient.getEtudiantById(studentId);
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    "Étudiant non trouvé avec l'id : " + studentId +
                    " (le service étudiant est peut-être indisponible)");
        }
    }
}