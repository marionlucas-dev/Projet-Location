package com.accenture.controller;

import com.accenture.service.VehiculeService;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.shared.Filtre;
import com.accenture.shared.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/vehicules")
@Slf4j
@Schema
@Tag(name = "Gestion des véhicules", description = "API pour la gestion des véhicules")

public class VehiculesController {

    private final VehiculeService vehiculeService;


    public VehiculesController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    @Operation(summary = "Filtrage des véhicules", description = "Filtre les véhicules présente dans le parc :" +
            " actif, inactif, dans le parc et hors du parc ")
    @ApiResponse(responseCode = "201", description = "Voitures filtrer avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    VehiculeDTO vehicules() {
       VehiculeDTO dto = vehiculeService.trouverTous();
        log.info("Afficher liste de véhicules : {} ", dto);
        return dto;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouve un véhicule", description = "Trouve un véhicule dans le parc en fonction de son modèle.")
    @ApiResponse(responseCode = "201", description = "Véhicule trouvée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VehiculeDTO> unVehicule(@PathVariable("id") String modele) {
        VehiculeDTO trouver = vehiculeService.trouver(modele);
        log.info(STR."Rechercher une véhicule : \{trouver}");
        return ResponseEntity.ok(trouver);
    }

    @GetMapping("/filtre")
    @Operation(summary = "Filtrage des véhicules", description = "Filtre les Véhicules présente dans le parc :" +
            " actif, inactif, dans le parc et hors du parc ")
    @ApiResponse(responseCode = "201", description = "Véhicules filtrer avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<List<VehiculeDTO>> filtrer(@RequestParam Filtre filtre) {
        VehiculeDTO filtrer = vehiculeService.filtrer(filtre);
        log.info("Ajouter un véhicule : {}", filtrer);
        return ResponseEntity.ok(Collections.singletonList(filtrer));
    }


//    @GetMapping("/search")
//    @Operation(summary = "Recherche des véhicules par dates ", description = "Filtre les véhicules par date ")
//    @ApiResponse(responseCode = "201", description = "Véhicules trouver avec succès")
//    @ApiResponse(responseCode = "400", description = "Données invalides")
//    ResponseEntity<VehiculeDTO> rechercherParDates(LocalDate dateDebut, LocalDate dateFin) {
//        VehiculeDTO parDates = vehiculeService.rechercher(dateDebut, dateFin);
//        log.info("Rechercher par dates : {} ", parDates);
//        return ResponseEntity.ok(parDates);
//    }

    @GetMapping("/search/")
    @Operation(summary = "Recherche des véhicules par dates et catégorie (moto ou voiture) ", description = "Filtre les véhicules par date et catégories ")
    @ApiResponse(responseCode = "201", description = "Véhicules trouver avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VehiculeDTO> rechercherParDatesEtCategories(@RequestParam LocalDate dateDebut, @RequestParam LocalDate dateFin,
                                                               @RequestParam(required = false) boolean inclureMotos,
                                                               @RequestParam(required = false) boolean inclureVoitures,
                                                               @RequestParam(required = false) Type type) {
        VehiculeDTO parDatesEtCategorieEtType = vehiculeService.rechercherParDateEtTypeEtCategorie(dateDebut, dateFin, inclureMotos, inclureVoitures, type);
        log.info("Rechercher par dates et catégories de véhicules : {} ", parDatesEtCategorieEtType);
        return ResponseEntity.ok(parDatesEtCategorieEtType);
    }


}
