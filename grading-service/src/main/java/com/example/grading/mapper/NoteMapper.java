package com.example.grading.mapper;

import com.example.grading.dto.NoteDTO;
import com.example.grading.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteDTO toDTO(Note note) {
        if (note == null) return null;
        return NoteDTO.builder()
                .id(note.getId())
                .studentId(note.getStudentId())
                .matiere(note.getMatiere())
                .valeur(note.getValeur())
                .build();
    }

    public Note toEntity(NoteDTO dto) {
        if (dto == null) return null;
        Note note = new Note();
        note.setId(dto.getId());
        note.setStudentId(dto.getStudentId());
        note.setMatiere(dto.getMatiere());
        note.setValeur(dto.getValeur());
        return note;
    }
}