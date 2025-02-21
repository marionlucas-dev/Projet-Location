package com.accenture.controller;

import com.accenture.repository.entity.Utilisateurs.Administrateur;
import com.accenture.service.AdministrateurService;
import com.accenture.service.dto.AdministrateurRequestDTO;
import com.accenture.service.dto.AdministrateurResponseDTO;
import com.accenture.service.dto.ClientRequestDTO;
import com.accenture.service.dto.ClientResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/administrateurs")
public class AdministrateurController {

    private final AdministrateurService adminService;
    public AdministrateurController(AdministrateurService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    List<AdministrateurResponseDTO> admin(){
        return adminService.trouverTous();
    }
 @GetMapping("/{id}")
    ResponseEntity<AdministrateurResponseDTO> unAdmin(@PathVariable("id") Long id){
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

    @DeleteMapping("/{id}")
    ResponseEntity<ClientResponseDTO> suppr(@PathVariable("id") Long id) {
        adminService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
