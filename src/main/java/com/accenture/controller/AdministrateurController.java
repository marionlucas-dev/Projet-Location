package com.accenture.controller;

import com.accenture.service.AdministrateurService;
import com.accenture.service.dto.utilisateurs.AdministrateurRequestDTO;
import com.accenture.service.dto.utilisateurs.AdministrateurResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Schema
@Tag(name = "Gestion des administrateurs ", description = "API pour la gestion des administrateurs ")
public class AdministrateurController {

    private final AdministrateurService adminService;

    public AdministrateurController(AdministrateurService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @Operation(summary = "Trouve les administrateurs", description = "Trouve les administrateurs dans le parc.")
    @ApiResponse(responseCode = "201", description = "Administrateurs trouvées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<AdministrateurResponseDTO> admin() {
        return adminService.trouverTous();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouve un administrateur", description = "Trouve un administrateur dans le parc.")
    @ApiResponse(responseCode = "201", description = "Administrateur trouvé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<AdministrateurResponseDTO> unAdmin(@PathVariable("id") Long id) {
        AdministrateurResponseDTO trouver = adminService.trouver(id);
        return ResponseEntity.ok(trouver);
    }

    @PostMapping
    @Operation(summary = "Ajouter un administrateur ", description = "Ajoute un administrateur dans la base.")
    @ApiResponse(responseCode = "201", description = "Administrateur ajouter avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
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
    @Operation(summary = "Informations administrateurs", description = "Donne les informations d'un administrateur.")
    @ApiResponse(responseCode = "201", description = "Administrateur trouvé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<AdministrateurResponseDTO> infosClient(String login, String password) {
        AdministrateurResponseDTO infosCompte = adminService.infosCompte(login, password);
        return ResponseEntity.ok(infosCompte);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un administrateur", description = "Supprime un administrateur de la base.")
    @ApiResponse(responseCode = "201", description = "Administrateur supprimé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<AdministrateurResponseDTO> suppClient(@PathVariable("id") String login, String password) {
        AdministrateurResponseDTO suppCompte = adminService.suppCompte(login, password);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement un administrateur", description = "Modifie un administrateur dans la base.")
    @ApiResponse(responseCode = "201", description = "Administrateur modifier avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<AdministrateurResponseDTO> modifPartiel(@PathVariable("id") String login, String password, @RequestBody AdministrateurRequestDTO adminRequestDTO) {
       AdministrateurResponseDTO reponse = adminService.modifPartielle(login, password, adminRequestDTO);
        return ResponseEntity.ok(reponse);
    }


}
