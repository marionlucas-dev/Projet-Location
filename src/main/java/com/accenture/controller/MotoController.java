package com.accenture.controller;

import com.accenture.service.MotoService;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.shared.Filtre;
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
public class MotoController {

    private final MotoService motoService;


    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @GetMapping
    List<MotoResponseDTO> motos (){
        return motoService.trouverTous();
    }

    @GetMapping("/{id}")
    ResponseEntity<MotoResponseDTO> uneMoto(@PathVariable("id") String modele){
        MotoResponseDTO trouver = motoService.trouver(modele);
        return ResponseEntity.ok(trouver);
    }

    @GetMapping("/filtre")
    ResponseEntity<List<MotoResponseDTO>> filtrer(@RequestParam Filtre filtre){
        List<MotoResponseDTO> motos = motoService.filtrer(filtre);
        return ResponseEntity.ok(motos);
    }

    @PostMapping
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
    ResponseEntity<MotoResponseDTO> suppMoto(@PathVariable("id") Long id){
        MotoResponseDTO suppMoto = motoService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/{id}")
    ResponseEntity<MotoResponseDTO> modifPartiel(@PathVariable("id") Long id , @RequestBody MotoRequestDTO motoRequestDTO){
        MotoResponseDTO reponse = motoService.modifier(id,motoRequestDTO);
        return ResponseEntity.ok(reponse);
    }










}
