package com.example.grading.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID de l'étudiant est obligatoire")
    private Long studentId;

    @NotBlank(message = "La matière est obligatoire")
    private String matiere;

    @NotNull(message = "La valeur de la note est obligatoire")
    @DecimalMin(value = "0.0", message = "La note minimum est 0")
    @DecimalMax(value = "20.0", message = "La note maximum est 20")
    private Double valeur;
}