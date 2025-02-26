package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.service.dto.Vehicules.VoitureRequestDTO;
import com.accenture.service.dto.Vehicules.VoitureResponseDTO;
import com.accenture.shared.Filtre;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VoitureService {
    List<VoitureResponseDTO> trouverTous();
    VoitureResponseDTO trouver(String modele) throws EntityNotFoundException;
    VoitureResponseDTO ajouter (VoitureRequestDTO voitureRequestDTO) throws VoitureException;
    VoitureResponseDTO supprimer(long id) throws EntityNotFoundException;

    VoitureResponseDTO modifier(long id, VoitureRequestDTO voitureRequestDTO) throws EntityNotFoundException;

    List<VoitureResponseDTO> filtrer(Filtre filtre);
}
