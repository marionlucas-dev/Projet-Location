package com.accenture.service;

import com.accenture.service.dto.vehicules.VeloResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VeloService {
    List<VeloResponseDTO> trouverTous();

    VeloResponseDTO trouver(String modele) throws EntityNotFoundException;
}
