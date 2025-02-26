package com.accenture.controller;
import com.accenture.service.VoitureService;
import com.accenture.service.dto.Utilisateurs.ClientRequestDTO;
import com.accenture.service.dto.Utilisateurs.ClientResponseDTO;
import com.accenture.service.dto.Vehicules.VoitureRequestDTO;
import com.accenture.service.dto.Vehicules.VoitureResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/voitures")
public class VoitureController {


    private final VoitureService voitureService;


    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }


    @GetMapping
    List<VoitureResponseDTO> voiture (){
       return voitureService.trouverTous();
    }

    @GetMapping("/{id}")
    ResponseEntity<VoitureResponseDTO> uneVoiture(@PathVariable("id") String modele){
        VoitureResponseDTO trouver = voitureService.trouver(modele);
        return ResponseEntity.ok(trouver);
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
        return ResponseEntity.ok(suppVoiture);
}

    @PatchMapping("/{id}")
    ResponseEntity<VoitureResponseDTO> modifPartiel(@PathVariable("id") Long id , @RequestBody VoitureRequestDTO voitureRequestDTO){
        VoitureResponseDTO reponse = voitureService.modifier(id, voitureRequestDTO);
        return ResponseEntity.ok(reponse);
    }

}
