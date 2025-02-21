package com.accenture.controller;

import com.accenture.service.ClientService;
import com.accenture.service.dto.ClientRequestDTO;
import com.accenture.service.dto.ClientResponseDTO;
import jakarta.validation.Valid;
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
    List<ClientResponseDTO> clients(){
        return clientService.trouverTous();
    }

    @GetMapping("/{id}")
    ResponseEntity<ClientResponseDTO> unClient(@PathVariable("id") long id){
        ClientResponseDTO trouver = clientService.trouver(id);
        return ResponseEntity.ok(trouver);
    }

@PostMapping
    ResponseEntity<Void> ajouter(@RequestBody ClientRequestDTO clientRequestDTO){
    ClientResponseDTO clientEnreg = clientService.ajouter(clientRequestDTO);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(clientEnreg.prenom())
            .toUri();
    return ResponseEntity.created(location).build();
}



}
