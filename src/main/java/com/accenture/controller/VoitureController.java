package com.accenture.controller;

import com.accenture.service.VoitureService;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.shared.Filtre;
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


    @GetMapping
    @Operation(summary = "Trouve les voitures", description = "Trouve les voitures dans le parc.")
    @ApiResponse(responseCode = "201", description = "Voitures trouvées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<VoitureResponseDTO> voitures() {
        List<VoitureResponseDTO> dto = voitureService.trouverTous();
        log.info("Afficher liste de véhicules : {} ", dto);
        return dto;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouve une voiture", description = "Trouve une voiture dans le parc.")
    @ApiResponse(responseCode = "201", description = "Voiture trouvée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VoitureResponseDTO> uneVoiture(@PathVariable("id") String modele) {
        VoitureResponseDTO trouver = voitureService.trouver(modele);
        log.info("Afficher une voiture : {}" , trouver);
        return ResponseEntity.ok(trouver);
    }

    @GetMapping("/filtre")
    @Operation(summary = "Filtrage des voiture", description = "Filtre les voitures présente dans le parc :" +
            " actif, inactif, dans le parc et hors du parc ")
    @ApiResponse(responseCode = "201", description = "Voitures filtrer avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<List<VoitureResponseDTO>> filtrer(@RequestParam Filtre filtre) {
        List<VoitureResponseDTO> voitures = voitureService.filtrer(filtre);
        log.info("Filtrer les voitures  : {}" , voitures);
        return ResponseEntity.ok(voitures);
    }

    @PostMapping
    @Operation(summary = "Ajouter une voiture", description = "Ajoute une voiture dans le parc.")
    @ApiResponse(responseCode = "201", description = "Voiture ajouter avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody @Valid VoitureRequestDTO voitureRequestDTO) {
        VoitureResponseDTO voitureEnreg = voitureService.ajouter(voitureRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(voitureEnreg.modele())
                .toUri();
        log.info("Ajouter une voiture : {}" , voitureEnreg);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une voiture", description = "Supprime une voiture dans le parc.")
    @ApiResponse(responseCode = "201", description = "Voiture supprimée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VoitureResponseDTO> suppVoiture(@PathVariable("id") Long id) {
        VoitureResponseDTO suppVoiture = voitureService.supprimer(id);
        log.info("Supprimer une voiture : {}" , suppVoiture);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement une voiture", description = "Modifie une voiture dans le parc.")
    @ApiResponse(responseCode = "201", description = "Voiture modifier avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VoitureResponseDTO> modifPartiel(@PathVariable("id") Long id, @RequestBody VoitureRequestDTO voitureRequestDTO) {
        VoitureResponseDTO reponse = voitureService.modifier(id, voitureRequestDTO);
        log.info("Modifier une voiture : {}" , reponse);
        return ResponseEntity.ok(reponse);
    }

}
