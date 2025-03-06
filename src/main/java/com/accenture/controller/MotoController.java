package com.accenture.controller;

import com.accenture.service.MotoService;
import com.accenture.service.dto.vehicules.*;
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
@RequestMapping("/motos")
@Slf4j
@Schema
@Tag(name = "Gestion des motos", description = "API pour la gestion des motos")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    /**
     * Récupère la liste de toutes les motos présentes dans le parc.
     * @return Une liste de {@link MotoResponseDTO} représentant les motos.
     */
    @GetMapping
    @Operation(summary = "Trouve les motos", description = "Trouve les motos dans le parc.")
    @ApiResponse(responseCode = "200", description = "Motos trouvées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<MotoResponseDTO> trouverUneMoto() {
        List<MotoResponseDTO> dto = motoService.trouverTous();
        log.info("Afficher liste de véhicules : {}", dto);
        return dto;
    }

    /**
     * Récupère une moto spécifique par son identifiant.
     *
     * @param id L'identifiant de la moto à récupérer.
     * @return Un {@link MotoResponseDTO} représentant la moto trouvée.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Trouve une moto", description = "Trouve une moto dans le parc.")
    @ApiResponse(responseCode = "200", description = "Moto trouvée avec succès")
    @ApiResponse(responseCode = "404", description = "Moto non trouvée")
    ResponseEntity<MotoResponseDTO> trouverUneMoto(@PathVariable("id") Long id) {
        MotoResponseDTO trouver = motoService.trouver(id);
        log.info("Afficher une moto : {}", trouver);
        return ResponseEntity.ok(trouver);
    }

    /**
     * Filtre les motos en fonction de différents critères (actif, inactif, dans le parc, hors du parc).
     *
     * @param filtre Le critère de filtrage.
     * @return Une liste filtrée de {@link MotoResponseDTO}.
     */
    @GetMapping("/filtre")
    @Operation(summary = "Filtrage des motos", description = "Filtre les motos présentes dans le parc : actif, inactif, dans le parc, hors du parc.")
    @ApiResponse(responseCode = "200", description = "Motos filtrées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<List<MotoResponseDTO>> filtrer(@RequestParam Filtre filtre) {
        List<MotoResponseDTO> motos = motoService.filtrer(filtre);
        log.info("Filtrer les motos : {}", motos);
        return ResponseEntity.ok(motos);
    }

    /**
     * Ajoute une moto dans le parc.
     *
     * @param motoRequestDTO L'objet contenant les informations de la moto à ajouter.
     * @return Un code de statut HTTP 201 avec un lien vers la ressource nouvellement créée.
     */
    @PostMapping
    @Operation(summary = "Ajouter une moto", description = "Ajoute une moto dans le parc.")
    @ApiResponse(responseCode = "201", description = "Moto ajoutée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody @Valid MotoRequestDTO motoRequestDTO) {
        MotoResponseDTO motoEnreg = motoService.ajouter(motoRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(motoEnreg.id())
                .toUri();
        log.info("Ajouter une moto : {}", motoEnreg);
        return ResponseEntity.created(location).build();
    }

    /**
     * Supprime une moto du parc.
     *
     * @param id L'identifiant de la moto à supprimer.
     * @return Un code de statut HTTP 204 indiquant que la moto a été supprimée avec succès.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une moto", description = "Supprime une moto du parc.")
    @ApiResponse(responseCode = "204", description = "Moto supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Moto non trouvée")
    ResponseEntity<MotoResponseDTO> supprimer(@PathVariable("id") Long id) {
        MotoResponseDTO suppMoto = motoService.supprimer(id);
        log.info("Supprimer une moto : {}", suppMoto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Modifie partiellement ou complètement une moto dans le parc.
     *
     * @param id               L'identifiant de la moto à modifier.
     * @param motoRequestDTO   L'objet contenant les nouvelles valeurs des champs à mettre à jour.
     * @return Un {@link MotoResponseDTO} contenant les informations mises à jour de la moto.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement une moto", description = "Modifie une moto dans le parc.")
    @ApiResponse(responseCode = "200", description = "Moto modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<MotoResponseDTO> modifier(@PathVariable("id") Long id, @RequestBody MotoRequestDTO motoRequestDTO) {
        MotoResponseDTO reponse = motoService.modifier(id, motoRequestDTO);
        log.info("Modifier une moto : {}", reponse);
        return ResponseEntity.ok(reponse);
    }
}


