package com.accenture.controller;

import com.accenture.service.AdministrateurService;
import com.accenture.service.dto.utilisateurs.AdministrateurRequestDTO;
import com.accenture.service.dto.utilisateurs.AdministrateurResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/administrateurs")
@Slf4j
public class AdministrateurController {

    private final AdministrateurService adminService;

    public AdministrateurController(AdministrateurService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    List<AdministrateurResponseDTO> admin() {
        return adminService.trouverTous();
    }

    @GetMapping("/{id}")
    ResponseEntity<AdministrateurResponseDTO> unAdmin(@PathVariable("id") Long id) {
        AdministrateurResponseDTO trouver = adminService.trouver(id);
        return ResponseEntity.ok(trouver);
    }

    @PostMapping
    ResponseEntity<Void> ajouter(@RequestBody @Valid AdministrateurRequestDTO adminRequestDTO) {
        AdministrateurResponseDTO adminEnreg = adminService.ajouter(adminRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(adminEnreg.prenom())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/informations")
    ResponseEntity<AdministrateurResponseDTO> infosClient(String login, String password) {
        AdministrateurResponseDTO infosCompte = adminService.infosCompte(login, password);
        return ResponseEntity.ok(infosCompte);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<AdministrateurResponseDTO> suppClient(@PathVariable("id") String login, String password) {
        AdministrateurResponseDTO suppCompte = adminService.suppCompte(login, password);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PatchMapping("/{id}")
    ResponseEntity<AdministrateurResponseDTO> modifPartiel(@PathVariable("id") String login, String password, @RequestBody AdministrateurRequestDTO adminRequestDTO) {
       AdministrateurResponseDTO reponse = adminService.modifPartielle(login, password, adminRequestDTO);
        return ResponseEntity.ok(reponse);
    }


}
