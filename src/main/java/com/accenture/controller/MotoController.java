package com.accenture.controller;

import com.accenture.service.MotoService;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
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
@RequestMapping("/motos")
@Slf4j
@Schema
@Tag(name = "Gestion des motos", description = "API pour la gestion des motos")
public class MotoController {

    private final MotoService motoService;


    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @GetMapping
    @Operation(summary = "Trouve les motos", description = "Trouve les motos dans le parc.")
    @ApiResponse(responseCode = "201", description = "Motos trouvées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<MotoResponseDTO> motos (){
        return motoService.trouverTous();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouve une moto", description = "Trouve une moto dans le parc.")
    @ApiResponse(responseCode = "201", description = "Moto trouvée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<MotoResponseDTO> uneMoto(@PathVariable("id") String modele){
        MotoResponseDTO trouver = motoService.trouver(modele);
        return ResponseEntity.ok(trouver);
    }

    @GetMapping("/filtre")
    @Operation(summary = "Filtrage des motos", description = "Filtre les motos présente dans le parc :" +
            " actif, inactif, dans le parc et hors du parc ")
    @ApiResponse(responseCode = "201", description = "Motos filtrer avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<List<MotoResponseDTO>> filtrer(@RequestParam Filtre filtre){
        List<MotoResponseDTO> motos = motoService.filtrer(filtre);
        return ResponseEntity.ok(motos);
    }

    @PostMapping
    @Operation(summary = "Ajouter une moto", description = "Ajoute une moto dans le parc.")
    @ApiResponse(responseCode = "201", description = "Moto ajouter avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter (@RequestBody @Valid MotoRequestDTO motoRequestDTO){
        MotoResponseDTO motoEnreg = motoService.ajouter(motoRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(motoEnreg.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une moto", description = "Supprime une moto dans le parc.")
    @ApiResponse(responseCode = "201", description = "Moto supprimée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<MotoResponseDTO> suppMoto(@PathVariable("id") Long id){
        MotoResponseDTO suppMoto = motoService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement une moto", description = "Modifie une moto dans le parc.")
    @ApiResponse(responseCode = "201", description = "Moto modifier avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<MotoResponseDTO> modifPartiel(@PathVariable("id") Long id , @RequestBody MotoRequestDTO motoRequestDTO){
        MotoResponseDTO reponse = motoService.modifier(id,motoRequestDTO);
        return ResponseEntity.ok(reponse);
    }










}
