package com.accenture.service;

import com.accenture.service.dto.Utilisateurs.AdministrateurRequestDTO;
import com.accenture.service.dto.Utilisateurs.AdministrateurResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface AdministrateurService {
    List<AdministrateurResponseDTO> trouverTous();
    AdministrateurResponseDTO trouver(long id) throws EntityNotFoundException;
    AdministrateurResponseDTO ajouter(AdministrateurRequestDTO adminRequestDTO);

    AdministrateurResponseDTO suppCompte(String login, String password) throws EntityNotFoundException;

    AdministrateurResponseDTO infosCompte(String login, String password) throws EntityNotFoundException;

    AdministrateurResponseDTO modifPartielle(String login, String password, AdministrateurRequestDTO adminRequestDTO);
}
