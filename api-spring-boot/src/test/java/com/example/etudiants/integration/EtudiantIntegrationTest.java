package com.example.etudiants.integration;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EtudiantIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        // Désactiver Redis et Eureka pour les tests d'intégration
        registry.add("spring.cache.type", () -> "none");
        registry.add("eureka.client.enabled", () -> "false");
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private DepartementRepository departementRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/etudiants";
        etudiantRepository.deleteAll();
        departementRepository.deleteAll();
    }

    private Departement createDepartement() {
        Departement dept = new Departement(null, "Informatique");
        return departementRepository.save(dept);
    }

    private Etudiant createAndSaveEtudiant(String cin, String nom, Departement dept) {
        Etudiant e = new Etudiant(null, cin, nom,
                LocalDate.of(2001, 3, 15),
                "test@univ.tn", 2020, dept);
        return etudiantRepository.save(e);
    }

    @Test
    @DisplayName("Doit persister et récupérer un étudiant depuis PostgreSQL")
    void shouldPersistAndRetrieveEtudiant() {
        Departement dept = createDepartement();
        createAndSaveEtudiant("AB123", "Ahmed", dept);

        assertThat(etudiantRepository.findAll()).isNotEmpty();
        assertThat(etudiantRepository.findAll().get(0).getNom()).isEqualTo("Ahmed");
    }

    @Test
    @DisplayName("GET /api/etudiants doit retourner HTTP 200 et une liste non vide")
    void shouldReturnEtudiantsViaRestEndpoint() {
        Departement dept = createDepartement();
        createAndSaveEtudiant("AB123", "Ahmed", dept);

        ResponseEntity<EtudiantDTO[]> response =
                restTemplate.getForEntity(baseUrl, EtudiantDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("GET /api/etudiants/9999 doit retourner HTTP 404")
    void shouldReturn404ForMissingEtudiant() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/9999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("POST /api/etudiants doit créer un étudiant et retourner HTTP 201")
    void shouldCreateEtudiant() {
        Departement dept = createDepartement();

        EtudiantDTO newEtudiant = EtudiantDTO.builder()
                .cin("NEW001")
                .nom("Nouveau Étudiant")
                .dateNaissance(LocalDate.of(2003, 6, 20))
                .email("new@univ.tn")
                .anneePremiereInscription(2022)
                .departementId(dept.getId())
                .build();

        ResponseEntity<EtudiantDTO> response =
                restTemplate.postForEntity(baseUrl, newEtudiant, EtudiantDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getNom()).isEqualTo("Nouveau Étudiant");
    }
}