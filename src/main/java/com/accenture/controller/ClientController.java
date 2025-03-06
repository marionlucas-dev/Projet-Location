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

    /**
     * Récupère la liste de tous les clients.
     * @return Une liste de {@link ClientResponseDTO} représentant les clients.
     */
    @GetMapping
    @Operation(summary = "Trouve les clients", description = "Trouve les clients dans le parc.")
    @ApiResponse(responseCode = "200", description = "Clients trouvés avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<ClientResponseDTO> clients() {
        List<ClientResponseDTO> dto = clientService.trouverTous();
        log.info("Afficher une liste de clients : {}", dto);
        return dto;
    }

    /**
     * Récupère un client spécifique par son identifiant.
     *
     * @param id L'identifiant du client à récupérer.
     * @return Un {@link ClientResponseDTO} représentant le client trouvé.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Trouve un client", description = "Trouve un client dans le parc.")
    @ApiResponse(responseCode = "200", description = "Client trouvé avec succès")
    @ApiResponse(responseCode = "404", description = "Client non trouvé")
    ResponseEntity<ClientResponseDTO> trouverUnClient(@PathVariable("id") Long id) {
        ClientResponseDTO trouver = clientService.trouver(id);
        log.info("Afficher un client : {}", trouver);
        return ResponseEntity.ok(trouver);
    }

    /**
     * Ajoute un client à la base de données.
     *
     * @param clientRequestDTO L'objet contenant les informations du client à ajouter.
     * @return Un code de statut HTTP 201 avec un lien vers la ressource nouvellement créée.
     */
    @PostMapping
    @Operation(summary = "Ajouter un client", description = "Ajoute un client dans la base.")
    @ApiResponse(responseCode = "201", description = "Client ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO clientEnreg = clientService.ajouter(clientRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientEnreg.prenom())
                .toUri();
        log.info("Ajouter un client : {}", clientEnreg);
        return ResponseEntity.created(location).build();
    }

    /**
     * Récupère les informations d'un client en vérifiant ses identifiants.
     *
     * @param login    : L'email du client.
     * @param password : Le mot de passe du client.
     * @return Un {@link ClientResponseDTO} contenant les informations du client si l'authentification réussit.
     */
    @GetMapping("/informations")
    @Operation(summary = "Informations clients", description = "Donne les informations d'un client.")
    @ApiResponse(responseCode = "200", description = "Client trouvé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDTO> recupererinfosCompte(@RequestParam String login, @RequestParam String password) {
        ClientResponseDTO infosCompte = clientService.recupererinfosCompte(login, password);
        log.info("Obtenir les infos d'un client : {}", infosCompte);
        return ResponseEntity.ok(infosCompte);
    }

    /**
     * Supprime un client de la base de données en vérifiant ses identifiants.
     *
     * @param login    : L'email du client à supprimer.
     * @param password : Le mot de passe du client.
     * @return Un code de statut HTTP 204 indiquant que le client a été supprimé avec succès.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprime un client de la base.")
    @ApiResponse(responseCode = "204", description = "Client supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Client non trouvé")
    ResponseEntity<ClientResponseDTO> supprimer(@PathVariable("id") Long id, @RequestParam String login, @RequestParam String password) {
        ClientResponseDTO suppCompte = clientService.suppprimer(login, password);
        log.info("Supprimer un compte : {}", suppCompte);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Modifie partiellement ou complètement un client dans la base de données.
     *
     * @param login           : L'email du client à modifier.
     * @param password        : Le mot de passe du client à modifier.
     * @param clientRequestDTO : L'objet contenant les nouvelles valeurs des champs à mettre à jour.
     * @return Un {@link ClientResponseDTO} contenant les informations mises à jour du client.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modifier complètement ou partiellement un client", description = "Modifie un client dans la base.")
    @ApiResponse(responseCode = "200", description = "Client modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDTO> modifier(@PathVariable("id") Long id, @RequestParam String login, @RequestParam String password, @RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO reponse = clientService.modifier(login, password, clientRequestDTO);
        log.info("Modifier un compte : {}", reponse);
        return ResponseEntity.ok(reponse);
    }
}
