package com.accenture.controller;

import com.accenture.service.VehiculeService;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.shared.Filtre;
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
 VehiculeDTO vehicules(){
        return vehiculeService.trouverTous();
 }

 @GetMapping("/{id}")
 @Operation(summary = "Trouve un véhicule", description = "Trouve un véhicule dans le parc en fonction de son modèle.")
 @ApiResponse(responseCode = "201", description = "Véhicule trouvée avec succès")
 @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VehiculeDTO> unVehicule(@PathVariable ("id") String modele){
        VehiculeDTO trouver = vehiculeService.trouver(modele);
        return ResponseEntity.ok(trouver);
 }

 @GetMapping("/filtre")
 @Operation(summary = "Filtrage des voiture", description = "Filtre les voitures présente dans le parc :" +
         " actif, inactif, dans le parc et hors du parc ")
 @ApiResponse(responseCode = "201", description = "Voitures filtrer avec succès")
 @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<List<VehiculeDTO>> filtrer (@RequestParam Filtre filtre){
        VehiculeDTO filtrer = vehiculeService.filtrer(filtre);
        return ResponseEntity.ok(Collections.singletonList(filtrer));
 }


@GetMapping("/search")
    ResponseEntity<VehiculeDTO> rechercherParDates(LocalDate dateDebut, LocalDate dateFin){
        VehiculeDTO parDates = vehiculeService.rechercher(dateDebut, dateFin);
        return ResponseEntity.ok(parDates);
}





}
