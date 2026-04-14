package com.example.etudiants.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le CIN est obligatoire")
    private String cin;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private LocalDate dateNaissance;

    @Email(message = "L'email doit être valide")
    private String email;

    private int anneePremiereInscription;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @JsonProperty("age")
    public int age() {
        if (this.dateNaissance == null) return 0;
        return Period.between(this.dateNaissance, LocalDate.now()).getYears();
    }
}