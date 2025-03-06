package com.accenture.controller;

import com.accenture.service.VehiculeService;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.shared.enumerations.Filtre;
import com.accenture.shared.enumerations.Type;
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

    /**
     * Récupère la liste de tous les véhicules présents dans le parc.
     * Les véhicules peuvent être filtrés par différents statuts : actif, inactif, dans le parc ou hors du parc.
     *
     * @return Un objet {@link VehiculeDTO} contenant la liste des véhicules.
     */

    @GetMapping
    @Operation(summary = "Liste des véhicules", description = "Liste les véhicules présentes dans le parc :" +
            " actif, inactif, dans le parc et hors du parc ")
    @ApiResponse(responseCode = "201", description = "Voitures filtrer avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    VehiculeDTO trouverTousLesVehicules() {
       VehiculeDTO dto = vehiculeService.trouverTous();
        log.info("Afficher liste de véhicules : {} ", dto);
        return dto;
    }


    /**
     * Récupère un véhicule spécifique par son identifiant.
     *
     * @param id L'identifiant du véhicule à rechercher.
     * @return Le véhicule trouvé sous forme de {@link VehiculeDTO}.
     */


    @GetMapping("/{id}")
    @Operation(summary = "Trouve un véhicule", description = "Trouve un véhicule dans le parc en fonction de son modèle.")
    @ApiResponse(responseCode = "201", description = "Véhicule trouvée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VehiculeDTO> trouverunVehicule(@PathVariable("id") long id) {
        VehiculeDTO trouver = vehiculeService.trouver(id);
        log.info(STR."Rechercher une véhicule : \{trouver}");
        return ResponseEntity.ok(trouver);
    }

    /**
     * Filtre les véhicules présents dans le parc en fonction de différents critères : actif, inactif, dans le parc ou hors du parc.
     *
     * @param filtre Critères de filtrage pour les véhicules.
     * @return Une liste de véhicules correspondant aux critères de filtrage.
     */

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


    /**
     * Recherche des véhicules disponibles dans le parc en fonction des dates et des catégories spécifiées.
     *
     * Cette méthode permet de filtrer les véhicules (motos et voitures) présents dans le parc en fonction de
     * leur date de disponibilité, de leur catégorie (moto ou voiture) et d'autres critères.
     * Si aucune catégorie n'est spécifiée, les deux catégories sont incluses par défaut.
     * La recherche prend en compte la date de début et de fin pour la disponibilité des véhicules.
     *
     * @param dateDebut La date de début de la période pour la recherche des véhicules disponibles.
     * @param dateFin La date de fin de la période pour la recherche des véhicules disponibles.
     * @param inclureMotos Indicateur pour inclure ou exclure les motos dans la recherche. Par défaut, les motos sont incluses.
     * @param inclureVoitures Indicateur pour inclure ou exclure les voitures dans la recherche. Par défaut, les voitures sont incluses.
     * @param type Le type de véhicule à rechercher, qui peut être un type spécifique (voiture ou moto).
     *
     * @return Une réponse contenant une liste de véhicules qui correspondent aux critères de recherche.
     *         La réponse est encapsulée dans un objet {@link VehiculeDTO}.
     *
     * @throws IllegalArgumentException Si les dates ou autres paramètres sont invalides.
     *
     * @see VehiculeDTO
     */


    @GetMapping("/search/")
    @Operation(summary = "Recherche des véhicules par dates, catégories et type de véhicules",
            description = "Filtre les véhicules disponibles dans le parc en fonction des dates, des catégories (moto ou voiture) et des types de véhicules.")
    @ApiResponse(responseCode = "200", description = "Véhicules trouvés avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Aucun véhicule trouvé")
    ResponseEntity<VehiculeDTO> rechercherParDatesEtCategories(
            @RequestParam LocalDate dateDebut,
            @RequestParam LocalDate dateFin,
            @RequestParam(required = false, defaultValue = "true") Boolean inclureMotos,
            @RequestParam(required = false, defaultValue = "true") Boolean inclureVoitures,
            @RequestParam(required = false) Type type) {
        VehiculeDTO parDatesEtCategorieEtType = vehiculeService.rechercherParDateEtTypeEtCategorie(dateDebut, dateFin, inclureMotos, inclureVoitures, type);
        log.info("Recherche de véhicules par dates et catégories : {} ", parDatesEtCategorieEtType);
        return ResponseEntity.ok(parDatesEtCategorieEtType);
    }


}
