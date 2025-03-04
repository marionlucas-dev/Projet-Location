package com.accenture.service;

import com.accenture.service.dto.utilisateurs.ClientRequestDTO;
import com.accenture.service.dto.utilisateurs.ClientResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {
    List<ClientResponseDTO> trouverTous();
    ClientResponseDTO trouver(long id) throws EntityNotFoundException;

  ClientResponseDTO suppCompte(String login, String password) throws EntityNotFoundException;

    ClientResponseDTO ajouter(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO infosCompte(String login, String password) throws EntityNotFoundException;

    ClientResponseDTO modifPartielle(String login, String password, ClientRequestDTO clientRequestDTO);
}
