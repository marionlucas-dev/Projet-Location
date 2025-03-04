package com.accenture.service;

import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.shared.Filtre;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VehiculeService {
    VehiculeDTO trouverTous();

    VehiculeDTO trouver(String modele) throws EntityNotFoundException;

    VehiculeDTO filtrer(Filtre filtre);
}
