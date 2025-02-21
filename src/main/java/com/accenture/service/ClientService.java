package com.accenture.service;

import com.accenture.service.dto.ClientRequestDTO;
import com.accenture.service.dto.ClientResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {
    List<ClientResponseDTO> trouverTous();
    ClientResponseDTO trouver(long id) throws EntityNotFoundException;
    ClientResponseDTO ajouter(ClientRequestDTO clientRequestDTO);

}
