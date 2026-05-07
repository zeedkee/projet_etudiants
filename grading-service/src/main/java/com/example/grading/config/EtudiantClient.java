package com.example.grading.config;

import com.example.grading.dto.EtudiantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "etudiant-service")
public interface EtudiantClient {

    @GetMapping("/api/etudiants/{id}")
    EtudiantDTO getEtudiantById(@PathVariable("id") Long id);
}