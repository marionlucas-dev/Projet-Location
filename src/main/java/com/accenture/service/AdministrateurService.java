package com.accenture.service;

import com.accenture.service.dto.AdministrateurRequestDTO;
import com.accenture.service.dto.AdministrateurResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface AdministrateurService {
    List<AdministrateurResponseDTO> trouverTous();
    AdministrateurResponseDTO trouver(long id) throws EntityNotFoundException;
    AdministrateurResponseDTO ajouter(AdministrateurRequestDTO adminRequestDTO);
    void supprimer(long id) throws EntityNotFoundException;
}
