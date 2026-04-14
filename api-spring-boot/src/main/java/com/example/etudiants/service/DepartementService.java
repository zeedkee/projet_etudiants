package com.example.etudiants.service;

import com.example.etudiants.dto.DepartementDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.exception.ResourceNotFoundException;
import com.example.etudiants.mapper.DepartementMapper;
import com.example.etudiants.repository.DepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartementService {

    private final DepartementRepository departementRepository;
    private final DepartementMapper departementMapper;

    public List<DepartementDTO> findAll() {
        return departementRepository.findAll()
                .stream()
                .map(departementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DepartementDTO findById(Long id) {
        Departement dept = departementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'id : " + id));
        return departementMapper.toDTO(dept);
    }

    public DepartementDTO save(DepartementDTO dto) {
        Departement dept = departementMapper.toEntity(dto);
        Departement saved = departementRepository.save(dept);
        return departementMapper.toDTO(saved);
    }

    public DepartementDTO update(Long id, DepartementDTO dto) {
        Departement existing = departementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'id : " + id));
        existing.setNom(dto.getNom());
        Departement updated = departementRepository.save(existing);
        return departementMapper.toDTO(updated);
    }

    public void delete(Long id) {
        if (!departementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Département non trouvé avec l'id : " + id);
        }
        departementRepository.deleteById(id);
    }
}