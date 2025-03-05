package com.accenture.controller;

import com.accenture.service.ClientService;
import com.accenture.service.dto.utilisateurs.ClientRequestDTO;
import com.accenture.service.dto.utilisateurs.ClientResponseDTO;
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
@RequestMapping("/clients")
@Slf4j
@Schema
@Tag(name = "Gestion des clients", description = "API pour la gestion des clients")
public class ClientController {

    private final ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @Operation(summary = "Trouve les clients", description = "Trouve les clients dans le parc.")
    @ApiResponse(responseCode = "201", description = "Clients trouvées avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<ClientResponseDTO> clients() {
        log.info("Afficher une liste de clients : {}" , clients());
        return clientService.trouverTous();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouve un client", description = "Trouve un client dans le parc.")
    @ApiResponse(responseCode = "201", description = "Client trouvé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDTO> unClient(@PathVariable("id") Long id) {
        ClientResponseDTO trouver = clientService.trouver(id);
        log.info("Afficher un client : {}" , trouver);
        return ResponseEntity.ok(trouver);
    }

    @PostMapping
    @Operation(summary = "Ajouter un client ", description = "Ajoute un client dans la base.")
    @ApiResponse(responseCode = "201", description = "Client ajouter avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO clientEnreg = clientService.ajouter(clientRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientEnreg.prenom())
                .toUri();
        log.info("Ajouter un client  : {}" , clientEnreg);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/informations")
    @Operation(summary = "Informations clients", description = "Donne les informations d'un client.")
    @ApiResponse(responseCode = "201", description = "Client trouvé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDTO> infosClient(String login, String password){
        ClientResponseDTO infosCompte = clientService.infosCompte(login, password);
        log.info("Obtenir les infos d'un client : {}" , infosCompte);
        return ResponseEntity.ok(infosCompte);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprime un client de la base.")
    @ApiResponse(responseCode = "201", description = "Client supprimé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDTO> suppClient(@PathVariable("id") String login, String password){
     ClientResponseDTO suppCompte=   clientService.suppCompte(login, password);
        log.info("Supprimer un compte : {}" , suppCompte);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement un client", description = "Modifie un client dans la base.")
    @ApiResponse(responseCode = "201", description = "Client modifier avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDTO> modifPartiel(@PathVariable("id") String login, String password , @RequestBody ClientRequestDTO clientRequestDTO){
     ClientResponseDTO reponse = clientService.modifPartielle(login, password, clientRequestDTO);
        log.info("Modifier un compte : {}" , reponse);
     return ResponseEntity.ok(reponse);
    }

}
