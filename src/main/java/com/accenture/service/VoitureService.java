package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.service.dto.VoitureRequestDTO;
import com.accenture.service.dto.VoitureResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VoitureService {
    List<VoitureResponseDTO> trouverTous();

    VoitureResponseDTO trouver(String modele) throws EntityNotFoundException;

    VoitureResponseDTO ajouter (VoitureRequestDTO voitureRequestDTO) throws VoitureException;
}
