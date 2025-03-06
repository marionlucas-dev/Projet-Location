package com.accenture.service;

import com.accenture.exception.ClientException;
import com.accenture.service.dto.utilisateurs.ClientRequestDTO;
import com.accenture.service.dto.utilisateurs.ClientResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {
    List<ClientResponseDTO> trouverTous();
    ClientResponseDTO trouver(long id) throws EntityNotFoundException;


    ClientResponseDTO recupererinfosCompte(String login, String password) throws EntityNotFoundException;

    ClientResponseDTO suppprimer(String login, String password) throws EntityNotFoundException;

    ClientResponseDTO ajouter(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO modifier(String login, String password, ClientRequestDTO clientRequestDTO) throws EntityNotFoundException, ClientException;
}
