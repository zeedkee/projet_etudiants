package com.example.etudiants.config;

import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final EtudiantRepository etudiantRepository;
    private final DepartementRepository departementRepository;

    public DataLoader(EtudiantRepository etudiantRepository,
                      DepartementRepository departementRepository) {
        this.etudiantRepository = etudiantRepository;
        this.departementRepository = departementRepository;
    }

    @Override
    public void run(String... args) {
        if (departementRepository.count() == 0) {
            Departement info = new Departement(null, "Informatique");
            Departement math = new Departement(null, "Mathématiques");
            Departement phys = new Departement(null, "Physique");
            departementRepository.saveAll(List.of(info, math, phys));
            System.out.println(">>> 3 départements chargés !");

            if (etudiantRepository.count() == 0) {
                List<Etudiant> etudiants = List.of(
                    new Etudiant(null, "AB123456", "Ahmed Ben Ali",
                        LocalDate.of(2001, 3, 15), "ahmed.benali@univ.tn", 2020, info),
                    new Etudiant(null, "CD789012", "Fatma Trabelsi",
                        LocalDate.of(2000, 7, 22), "fatma.trabelsi@univ.tn", 2019, math),
                    new Etudiant(null, "EF345678", "Mohamed Karray",
                        LocalDate.of(2002, 1, 10), "mohamed.karray@univ.tn", 2021, info),
                    new Etudiant(null, "GH901234", "Salma Bouazizi",
                        LocalDate.of(2001, 11, 5), "salma.bouazizi@univ.tn", 2020, phys),
                    new Etudiant(null, "IJ567890", "Youssef Hammami",
                        LocalDate.of(2000, 9, 30), "youssef.hammami@univ.tn", 2019, math)
                );
                etudiantRepository.saveAll(etudiants);
                System.out.println(">>> 5 étudiants chargés avec départements !");
            }
        }
    }
}