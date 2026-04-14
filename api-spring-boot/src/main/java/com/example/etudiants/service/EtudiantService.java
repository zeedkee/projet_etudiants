package com.example.etudiants.service;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.exception.ResourceNotFoundException;
import com.example.etudiants.mapper.EtudiantMapper;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final DepartementRepository departementRepository;
    private final EtudiantMapper etudiantMapper;

    @Cacheable(value = "etudiants")
    public List<EtudiantDTO> findAll() {
        return etudiantRepository.findAll()
                .stream()
                .map(etudiantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EtudiantDTO findById(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec l'id : " + id));
        return etudiantMapper.toDTO(etudiant);
    }

    public List<EtudiantDTO> findByAnnee(int annee) {
        return etudiantRepository.findByAnneePremiereInscription(annee)
                .stream()
                .map(etudiantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public EtudiantDTO save(EtudiantDTO dto) {
        Etudiant etudiant = etudiantMapper.toEntity(dto);
        resolverDepartement(etudiant, dto.getDepartementId());
        Etudiant saved = etudiantRepository.save(etudiant);
        return etudiantMapper.toDTO(saved);
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public EtudiantDTO update(Long id, EtudiantDTO dto) {
        Etudiant existing = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec l'id : " + id));

        existing.setCin(dto.getCin());
        existing.setNom(dto.getNom());
        existing.setDateNaissance(dto.getDateNaissance());
        existing.setEmail(dto.getEmail());
        existing.setAnneePremiereInscription(dto.getAnneePremiereInscription());
        resolverDepartement(existing, dto.getDepartementId());

        Etudiant updated = etudiantRepository.save(existing);
        return etudiantMapper.toDTO(updated);
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public void delete(Long id) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec l'id : " + id);
        }
        etudiantRepository.deleteById(id);
    }

    private void resolverDepartement(Etudiant etudiant, Long departementId) {
        if (departementId != null) {
            Departement dept = departementRepository.findById(departementId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Département non trouvé avec l'id : " + departementId));
            etudiant.setDepartement(dept);
        }
    }
}