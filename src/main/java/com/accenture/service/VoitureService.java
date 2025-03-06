package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.shared.enumerations.Filtre;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VoitureService {
    List<VoitureResponseDTO> trouverTous();
    VoitureResponseDTO trouver(long id) throws EntityNotFoundException;
    VoitureResponseDTO ajouter (VoitureRequestDTO voitureRequestDTO) throws VoitureException;
    VoitureResponseDTO supprimer(long id) throws EntityNotFoundException;

    VoitureResponseDTO modifier(long id, VoitureRequestDTO voitureRequestDTO) throws EntityNotFoundException;

    List<VoitureResponseDTO> filtrer(Filtre filtre);
}
