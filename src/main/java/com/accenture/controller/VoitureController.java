package com.accenture.controller;

import com.accenture.service.VoitureService;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.shared.enumerations.Filtre;
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
@RequestMapping("/voitures")
@Slf4j
@Schema
@Tag(name = "Gestion des voitures", description = "API pour la gestion des voitures")
public class VoitureController {

    private final VoitureService voitureService;

    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }

    /**
     * Recherche toutes les voitures présentes dans le parc.
     * Cette méthode permet de récupérer la liste de toutes les voitures disponibles dans le parc.
     * @return La liste des voitures sous forme de {@link List<VoitureResponseDTO>} disponibles dans le parc.
     */
    @GetMapping
    @Operation(summary = "Trouve les voitures", description = "Récupère toutes les voitures présentes dans le parc.")
    @ApiResponse(responseCode = "200", description = "Voitures trouvées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public List<VoitureResponseDTO> TrouverToutesLesVoitures() {
        List<VoitureResponseDTO> dto = voitureService.trouverTous();
        log.info("Afficher liste de véhicules : {} ", dto);
        return dto;
    }

    /**
     * Recherche une voiture en particulier par son identifiant.
     * Cette méthode permet de récupérer les informations d'une voiture spécifiée par son ID.
     * @param id L'identifiant de la voiture à rechercher.
     * @return La voiture trouvée sous forme de {@link VoitureResponseDTO}.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Trouve une voiture", description = "Récupère les informations d'une voiture spécifique.")
    @ApiResponse(responseCode = "200", description = "Voiture trouvée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public ResponseEntity<VoitureResponseDTO> trouverUneVoiture(@PathVariable("id") Long id) {
        VoitureResponseDTO trouver = voitureService.trouver(id);
        log.info("Afficher une voiture : {}", trouver);
        return ResponseEntity.ok(trouver);
    }

    /**
     * Filtre les voitures présentes dans le parc en fonction de différents critères.
     * Cette méthode permet de filtrer les voitures selon leur statut (actif, inactif, dans le parc, hors du parc).
     * @param filtre Critères de filtrage des voitures.
     * @return La liste des voitures filtrées sous forme de {@link List<VoitureResponseDTO>}.
     */
    @GetMapping("/filtre")
    @Operation(summary = "Filtrage des voitures", description = "Filtre les voitures présentes dans le parc en fonction de différents critères.")
    @ApiResponse(responseCode = "200", description = "Voitures filtrées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public ResponseEntity<List<VoitureResponseDTO>> filtrer(@RequestParam Filtre filtre) {
        List<VoitureResponseDTO> voitures = voitureService.filtrer(filtre);
        log.info("Filtrer les voitures : {}", voitures);
        return ResponseEntity.ok(voitures);
    }

    /**
     * Ajoute une nouvelle voiture dans le parc.
     * Cette méthode permet d'ajouter une voiture en base de données.
     * @param voitureRequestDTO Données de la voiture à ajouter.
     * @return Une réponse HTTP avec le statut 201 (Créée) et l'URL de la nouvelle voiture.
     */
    @PostMapping
    @Operation(summary = "Ajouter une voiture", description = "Ajoute une nouvelle voiture dans le parc.")
    @ApiResponse(responseCode = "201", description = "Voiture ajoutée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public ResponseEntity<Void> ajouter(@RequestBody @Valid VoitureRequestDTO voitureRequestDTO) {
        VoitureResponseDTO voitureEnreg = voitureService.ajouter(voitureRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(voitureEnreg.modele())
                .toUri();
        log.info("Ajouter une voiture : {}", voitureEnreg);
        return ResponseEntity.created(location).build();
    }

    /**
     * Supprime une voiture du parc.
     * Cette méthode permet de supprimer une voiture spécifiée par son ID du parc.
     * @param id L'identifiant de la voiture à supprimer.
     * @return Une réponse HTTP avec le statut 204 (Pas de contenu) si la suppression a été effectuée avec succès.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une voiture", description = "Supprime une voiture spécifique du parc.")
    @ApiResponse(responseCode = "204", description = "Voiture supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Voiture non trouvée")
    public ResponseEntity<VoitureResponseDTO> supprimer(@PathVariable("id") Long id) {
        VoitureResponseDTO suppVoiture = voitureService.supprimer(id);
        log.info("Supprimer une voiture : {}", suppVoiture);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Modifie une voiture dans le parc, que ce soit complètement ou partiellement.
     * Cette méthode permet de modifier les données d'une voiture existante, soit partiellement, soit complètement.
     * @param id L'identifiant de la voiture à modifier.
     * @param voitureRequestDTO Les données de la voiture à modifier.
     * @return La voiture modifiée sous forme de {@link VoitureResponseDTO}.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement une voiture", description = "Modifie une voiture dans le parc.")
    @ApiResponse(responseCode = "200", description = "Voiture modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public ResponseEntity<VoitureResponseDTO> modifier(@PathVariable("id") Long id, @RequestBody VoitureRequestDTO voitureRequestDTO) {
        VoitureResponseDTO reponse = voitureService.modifier(id, voitureRequestDTO);
        log.info("Modifier une voiture : {}", reponse);
        return ResponseEntity.ok(reponse);
    }

}
