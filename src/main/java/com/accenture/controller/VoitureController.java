package com.accenture.controller;
import com.accenture.service.VoitureService;
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
@RequestMapping("/voitures")
@Slf4j
public class VoitureController {


    private final VoitureService voitureService;


    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }


    @GetMapping
    List<VoitureResponseDTO> voitures (){
       return voitureService.trouverTous();
    }

    @GetMapping("/{id}")
    ResponseEntity<VoitureResponseDTO> uneVoiture(@PathVariable("id") String modele){
        VoitureResponseDTO trouver = voitureService.trouver(modele);
        return ResponseEntity.ok(trouver);
    }
    
    @GetMapping("/filtre")
    ResponseEntity<List<VoitureResponseDTO>> filtrer(@RequestParam Filtre filtre){
        List<VoitureResponseDTO> voitures = voitureService.filtrer(filtre);
        return ResponseEntity.ok(voitures);
    }

    @PostMapping
    ResponseEntity<Void> ajouter (@RequestBody @Valid VoitureRequestDTO voitureRequestDTO){
        VoitureResponseDTO voitureEnreg = voitureService.ajouter(voitureRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(voitureEnreg.modele())
                .toUri();
        return ResponseEntity.created(location).build();
    }

@DeleteMapping("/{id}")
    ResponseEntity<VoitureResponseDTO> suppVoiture(@PathVariable("id") Long id){
        VoitureResponseDTO suppVoiture = voitureService.supprimer(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

}

    @PatchMapping("/{id}")
    ResponseEntity<VoitureResponseDTO> modifPartiel(@PathVariable("id") Long id , @RequestBody VoitureRequestDTO voitureRequestDTO){
        VoitureResponseDTO reponse = voitureService.modifier(id, voitureRequestDTO);
        return ResponseEntity.ok(reponse);
    }

}
