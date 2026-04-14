package com.example.etudiants.mapper;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import org.springframework.stereotype.Component;

@Component
public class EtudiantMapper {

    /**
     * Convertit une entité Etudiant en EtudiantDTO.
     */
    public EtudiantDTO toDTO(Etudiant etudiant) {
        if (etudiant == null) return null;

        return EtudiantDTO.builder()
                .id(etudiant.getId())
                .cin(etudiant.getCin())
                .nom(etudiant.getNom())
                .dateNaissance(etudiant.getDateNaissance())
                .email(etudiant.getEmail())
                .anneePremiereInscription(etudiant.getAnneePremiereInscription())
                .age(etudiant.age())
                .departementId(etudiant.getDepartement() != null ? etudiant.getDepartement().getId() : null)
                .departementNom(etudiant.getDepartement() != null ? etudiant.getDepartement().getNom() : null)
                .build();
    }

    /**
     * Convertit un EtudiantDTO en entité Etudiant.
     * Note : le département doit être résolu séparément (par ID).
     */
    public Etudiant toEntity(EtudiantDTO dto) {
        if (dto == null) return null;

        Etudiant etudiant = new Etudiant();
        etudiant.setId(dto.getId());
        etudiant.setCin(dto.getCin());
        etudiant.setNom(dto.getNom());
        etudiant.setDateNaissance(dto.getDateNaissance());
        etudiant.setEmail(dto.getEmail());
        etudiant.setAnneePremiereInscription(dto.getAnneePremiereInscription());

        // Associer le département si l'ID est fourni
        if (dto.getDepartementId() != null) {
            Departement dept = new Departement();
            dept.setId(dto.getDepartementId());
            etudiant.setDepartement(dept);
        }

        return etudiant;
    }
}