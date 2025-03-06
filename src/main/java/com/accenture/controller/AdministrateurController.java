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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/administrateurs")
@Slf4j
@Schema
@Tag(name = "Gestion des administrateurs", description = "API pour la gestion des administrateurs")
public class AdministrateurController {

    private final AdministrateurService adminService;

    public AdministrateurController(AdministrateurService adminService) {
        this.adminService = adminService;
    }

    /**
     * Récupère la liste de tous les administrateurs.
     * @return Une liste de {@link AdministrateurResponseDTO} représentant les administrateurs.
     */
    @GetMapping
    @Operation(summary = "Trouve les administrateurs", description = "Trouve les administrateurs dans le parc.")
    @ApiResponse(responseCode = "200", description = "Administrateurs trouvées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<AdministrateurResponseDTO> admin() {
        List<AdministrateurResponseDTO> dto = adminService.trouverTous();
        log.info("Afficher une liste d'administrateurs : {} " , dto);
        return dto;
    }

    /**
     * Récupère un administrateur spécifique par son identifiant.
     *
     * @param id L'identifiant de l'administrateur à récupérer.
     * @return Un {@link AdministrateurResponseDTO} représentant l'administrateur trouvé.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Trouve un administrateur", description = "Trouve un administrateur dans le parc.")
    @ApiResponse(responseCode = "200", description = "Administrateur trouvé avec succès")
    @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    ResponseEntity<AdministrateurResponseDTO> unAdmin(@PathVariable("id") Long id) {
        AdministrateurResponseDTO trouver = adminService.trouver(id);
        log.info("Afficher un administrateur : {}", trouver);
        return ResponseEntity.ok(trouver);
    }

    /**
     * Ajoute un administrateur à la base de données.
     *
     * @param adminRequestDTO L'objet contenant les informations de l'administrateur à ajouter.
     * @return Un code de statut HTTP 201 avec un lien vers la ressource nouvellement créée.
     */
    @PostMapping
    @Operation(summary = "Ajouter un administrateur", description = "Ajoute un administrateur dans la base.")
    @ApiResponse(responseCode = "201", description = "Administrateur ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody @Valid AdministrateurRequestDTO adminRequestDTO) {
        AdministrateurResponseDTO adminEnreg = adminService.ajouter(adminRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(adminEnreg.prenom())
                .toUri();
        log.info("Ajouter un administrateur : {}", adminEnreg);
        return ResponseEntity.created(location).build();
    }

    /**
     * Récupère les informations d'un administrateur en vérifiant ses identifiants.
     *
     * @param login    : L'email de l'administrateur.
     * @param password : Le mot de passe de l'administrateur.
     * @return Un {@link AdministrateurResponseDTO} contenant les informations de l'administrateur si l'authentification réussit.
     */
    @GetMapping("/informations")
    @Operation(summary = "Informations administrateurs", description = "Donne les informations d'un administrateur.")
    @ApiResponse(responseCode = "200", description = "Administrateur trouvé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<AdministrateurResponseDTO> recupererinfosCompte(@RequestParam String login, @RequestParam String password) {
        AdministrateurResponseDTO infosCompte = adminService.recupererinfosCompte(login, password);
        log.info("Afficher les informations d'un administrateur : {}", infosCompte);
        return ResponseEntity.ok(infosCompte);
    }

    /**
     * Supprime un administrateur de la base de données en vérifiant ses identifiants.
     *
     * @param login    : L'email de l'administrateur à supprimer.
     * @param password : Le mot de passe de l'administrateur.
     * @return Un code de statut HTTP 204 indiquant que l'administrateur a été supprimé avec succès.
     */

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un administrateur", description = "Supprime un administrateur de la base.")
    @ApiResponse(responseCode = "204", description = "Administrateur supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    ResponseEntity<AdministrateurResponseDTO> supprimer(@PathVariable("id") Long id, @RequestParam String login, @RequestParam String password) {
        AdministrateurResponseDTO suppCompte = adminService.supprimer(login, password);
        log.info("Supprimer un administrateur : {}", suppCompte);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Modifie partiellement les informations d'un administrateur dans la base de données.
     *
     * @param login           : L'email de l'administrateur à modifier.
     * @param password        : Le mot de passe de l'administrateur à modifier.
     * @param adminRequestDTO : L'objet contenant les nouvelles valeurs des champs à mettre à jour.
     * @return Un {@link AdministrateurResponseDTO} contenant les informations mises à jour de l'administrateur.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement un administrateur", description = "Modifie un administrateur dans la base.")
    @ApiResponse(responseCode = "200", description = "Administrateur modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<AdministrateurResponseDTO> modifier(@PathVariable("id") Long id, @RequestParam String login, @RequestParam String password, @RequestBody AdministrateurRequestDTO adminRequestDTO) {
        AdministrateurResponseDTO reponse = adminService.modifPartielle(login, password, adminRequestDTO);
        log.info("Modifier un compte administrateur : {}", reponse);
        return ResponseEntity.ok(reponse);
    }
}

