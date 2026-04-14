package com.example.etudiants.bdd;

import com.example.etudiants.entity.Etudiant;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtudiantAgeSteps {

    private Etudiant etudiant;
    private int ageCalcule;

    @Given("un étudiant avec la date de naissance {string}")
    public void un_etudiant_avec_la_date_de_naissance(String dateStr) {
        etudiant = new Etudiant();
        etudiant.setDateNaissance(LocalDate.parse(dateStr));
        etudiant.setCin("TEST001");
        etudiant.setNom("Etudiant Test");
    }

    @When("on calcule son âge")
    public void on_calcule_son_age() {
        ageCalcule = etudiant.age();
    }

    @Then("l'âge retourné doit être cohérent avec la date de naissance {string}")
    public void l_age_retourne_doit_etre_coherent(String dateStr) {
        int expectedAge = Period.between(LocalDate.parse(dateStr), LocalDate.now()).getYears();
        assertEquals(expectedAge, ageCalcule,
            "L'âge calculé devrait correspondre à la différence entre la date de naissance et aujourd'hui");
    }
}