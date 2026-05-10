package com.example.etudiants.unit;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.exception.ResourceNotFoundException;
import com.example.etudiants.mapper.EtudiantMapper;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import com.example.etudiants.service.EtudiantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private DepartementRepository departementRepository;

    @Mock
    private EtudiantMapper etudiantMapper;

    @InjectMocks
    private EtudiantService etudiantService;

    // ===== Helper methods =====

    private Etudiant createEtudiant(Long id, String cin, String nom) {
        Departement dept = new Departement(1L, "Informatique");
        return new Etudiant(id, cin, nom,
                LocalDate.of(2001, 3, 15),
                "test@univ.tn", 2020, dept);
    }

    private EtudiantDTO createEtudiantDTO(Long id, String cin, String nom) {
        return EtudiantDTO.builder()
                .id(id).cin(cin).nom(nom)
                .dateNaissance(LocalDate.of(2001, 3, 15))
                .email("test@univ.tn")
                .anneePremiereInscription(2020)
                .age(Period.between(LocalDate.of(2001, 3, 15), LocalDate.now()).getYears())
                .departementId(1L)
                .departementNom("Informatique")
                .build();
    }

    // ===== findAll() =====

    @Test
    @DisplayName("findAll() doit retourner la liste de tous les étudiants")
    void shouldReturnAllEtudiants() {
        // given
        Etudiant etudiant = createEtudiant(1L, "AB123", "Ahmed");
        EtudiantDTO dto = createEtudiantDTO(1L, "AB123", "Ahmed");
        when(etudiantRepository.findAll()).thenReturn(List.of(etudiant));
        when(etudiantMapper.toDTO(etudiant)).thenReturn(dto);

        // when
        List<EtudiantDTO> result = etudiantService.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Ahmed");
        verify(etudiantRepository).findAll();
    }

    @Test
    @DisplayName("findAll() doit retourner une liste vide quand il n'y a pas d'étudiants")
    void shouldReturnEmptyListWhenNoEtudiants() {
        when(etudiantRepository.findAll()).thenReturn(List.of());

        List<EtudiantDTO> result = etudiantService.findAll();

        assertThat(result).isEmpty();
    }

    // ===== findById() =====

    @Test
    @DisplayName("findById() doit retourner l'étudiant correspondant")
    void shouldReturnEtudiantById() {
        Etudiant etudiant = createEtudiant(1L, "AB123", "Ahmed");
        EtudiantDTO dto = createEtudiantDTO(1L, "AB123", "Ahmed");
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(etudiantMapper.toDTO(etudiant)).thenReturn(dto);

        EtudiantDTO result = etudiantService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getCin()).isEqualTo("AB123");
        assertThat(result.getNom()).isEqualTo("Ahmed");
    }

    @Test
    @DisplayName("findById() doit lancer ResourceNotFoundException pour un ID inexistant")
    void shouldThrowExceptionWhenEtudiantNotFound() {
        when(etudiantRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> etudiantService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    // ===== save() =====

    @Test
    @DisplayName("save() doit créer un étudiant et retourner le DTO")
    void shouldSaveEtudiant() {
        EtudiantDTO inputDTO = createEtudiantDTO(null, "NEW01", "Nouveau");
        Etudiant entity = createEtudiant(null, "NEW01", "Nouveau");
        Etudiant saved = createEtudiant(10L, "NEW01", "Nouveau");
        EtudiantDTO outputDTO = createEtudiantDTO(10L, "NEW01", "Nouveau");

        when(etudiantMapper.toEntity(inputDTO)).thenReturn(entity);
        when(departementRepository.findById(1L)).thenReturn(Optional.of(new Departement(1L, "Informatique")));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(saved);
        when(etudiantMapper.toDTO(saved)).thenReturn(outputDTO);

        EtudiantDTO result = etudiantService.save(inputDTO);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getNom()).isEqualTo("Nouveau");
        verify(etudiantRepository).save(any(Etudiant.class));
    }

    // ===== update() =====

    @Test
    @DisplayName("update() doit modifier un étudiant existant")
    void shouldUpdateEtudiant() {
        Etudiant existing = createEtudiant(1L, "AB123", "Ahmed");
        EtudiantDTO updateDTO = createEtudiantDTO(1L, "AB123", "Ahmed Modifié");
        Etudiant updated = createEtudiant(1L, "AB123", "Ahmed Modifié");
        EtudiantDTO resultDTO = createEtudiantDTO(1L, "AB123", "Ahmed Modifié");

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departementRepository.findById(1L)).thenReturn(Optional.of(new Departement(1L, "Informatique")));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(updated);
        when(etudiantMapper.toDTO(updated)).thenReturn(resultDTO);

        EtudiantDTO result = etudiantService.update(1L, updateDTO);

        assertThat(result.getNom()).isEqualTo("Ahmed Modifié");
    }

    @Test
    @DisplayName("update() doit lancer ResourceNotFoundException pour un ID inexistant")
    void shouldThrowExceptionWhenUpdatingNonExistentEtudiant() {
        EtudiantDTO dto = createEtudiantDTO(999L, "XX", "Ghost");
        when(etudiantRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> etudiantService.update(999L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ===== delete() =====

    @Test
    @DisplayName("delete() doit supprimer un étudiant existant")
    void shouldDeleteEtudiant() {
        when(etudiantRepository.existsById(1L)).thenReturn(true);

        etudiantService.delete(1L);

        verify(etudiantRepository).deleteById(1L);
    }

    @Test
    @DisplayName("delete() doit lancer ResourceNotFoundException pour un ID inexistant")
    void shouldThrowExceptionWhenDeletingNonExistentEtudiant() {
        when(etudiantRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> etudiantService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ===== findByAnnee() =====

    @Test
    @DisplayName("findByAnnee() doit retourner les étudiants filtrés par année d'inscription")
    void shouldReturnEtudiantsByAnnee() {
        Etudiant e1 = createEtudiant(1L, "AB123", "Ahmed");
        EtudiantDTO dto1 = createEtudiantDTO(1L, "AB123", "Ahmed");

        when(etudiantRepository.findByAnneePremiereInscription(2020)).thenReturn(List.of(e1));
        when(etudiantMapper.toDTO(e1)).thenReturn(dto1);

        List<EtudiantDTO> result = etudiantService.findByAnnee(2020);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAnneePremiereInscription()).isEqualTo(2020);
    }

    // ===== age() — test direct sur l'entité =====

    @Test
    @DisplayName("age() doit calculer correctement l'âge à partir de la date de naissance")
    void shouldCalculateAgeCorrectly() {
        Etudiant etudiant = new Etudiant();
        etudiant.setDateNaissance(LocalDate.of(2002, 4, 7));

        int expectedAge = Period.between(LocalDate.of(2002, 4, 7), LocalDate.now()).getYears();
        assertThat(etudiant.age()).isEqualTo(expectedAge);
    }

    @Test
    @DisplayName("age() doit retourner 0 si dateNaissance est null")
    void shouldReturnZeroWhenDateNaissanceIsNull() {
        Etudiant etudiant = new Etudiant();
        etudiant.setDateNaissance(null);

        assertThat(etudiant.age()).isEqualTo(0);
    }
}