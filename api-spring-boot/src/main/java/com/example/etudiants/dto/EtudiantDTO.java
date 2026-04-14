package com.example.etudiants.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO implements Serializable {

    private Long id;
    private String cin;
    private String nom;
    private LocalDate dateNaissance;
    private String email;
    private int anneePremiereInscription;
    private int age;

    // Infos du département (aplati, pas imbriqué)
    private Long departementId;
    private String departementNom;
}