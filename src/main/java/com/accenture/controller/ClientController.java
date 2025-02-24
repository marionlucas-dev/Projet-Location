package com.accenture.controller;

import com.accenture.service.ClientService;
import com.accenture.service.dto.ClientRequestDTO;
import com.accenture.service.dto.ClientResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    List<ClientResponseDTO> clients() {
        return clientService.trouverTous();
    }

    @GetMapping("/{id}")
    ResponseEntity<ClientResponseDTO> unClient(@PathVariable("id") Long id) {
        ClientResponseDTO trouver = clientService.trouver(id);
        return ResponseEntity.ok(trouver);
    }

    @PostMapping
    ResponseEntity<Void> ajouter(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO clientEnreg = clientService.ajouter(clientRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientEnreg.prenom())
                .toUri();
        return ResponseEntity.created(location).build();
    }

//    @DeleteMapping("/{id}")
//    ResponseEntity<ClientResponseDTO> suppr(@PathVariable("id") Long id) {
//        clientService.supprimer(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

    @GetMapping("/informations")
    ResponseEntity<ClientResponseDTO> infosClient(String login, String password){
        ClientResponseDTO infosCompte = clientService.infosCompte(login, password);
        return ResponseEntity.ok(infosCompte);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ClientResponseDTO> suppClient(@PathVariable("id") String login, String password){
     ClientResponseDTO suppCompte=   clientService.suppCompte(login, password);
        return ResponseEntity.ok(suppCompte);
    }


    @PatchMapping("/{id}")
    ResponseEntity<ClientResponseDTO> modifPartiel(@PathVariable("id") String login, String password , @RequestBody ClientRequestDTO clientRequestDTO){
     ClientResponseDTO reponse = clientService.modifPartielle(login, password, clientRequestDTO);
     return ResponseEntity.ok(reponse);
    }

}
