package com.example.etudiants.config;

import com.example.etudiants.model.Etudiant;
import com.example.etudiants.repository.EtudiantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final EtudiantRepository etudiantRepository;

    public DataLoader(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    @Override
    public void run(String... args) {
        if (etudiantRepository.count() == 0) {
            List<Etudiant> etudiants = List.of(
                new Etudiant(null, "AB123456", "Ahmed Ben Ali",      LocalDate.of(2001, 3, 15)),
                new Etudiant(null, "CD789012", "Fatma Trabelsi",     LocalDate.of(2000, 7, 22)),
                new Etudiant(null, "EF345678", "Mohamed Karray",     LocalDate.of(2002, 1, 10)),
                new Etudiant(null, "GH901234", "Salma Bouazizi",     LocalDate.of(2001, 11, 5)),
                new Etudiant(null, "IJ567890", "Youssef Hammami",    LocalDate.of(2000, 9, 30))
            );
            etudiantRepository.saveAll(etudiants);
            System.out.println(">>> 5 étudiants chargés en base de données !");
        }
    }
}