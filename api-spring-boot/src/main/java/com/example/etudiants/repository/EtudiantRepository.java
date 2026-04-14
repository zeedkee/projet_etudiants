package com.example.etudiants.repository;

import com.example.etudiants.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    /**
     * Requête dérivée : filtre les étudiants par année de première inscription.
     * Exposée via GET /api/etudiants?annee=2022
     */
    List<Etudiant> findByAnneePremiereInscription(int anneePremiereInscription);
}