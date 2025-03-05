package com.accenture.service;

import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.shared.Filtre;
import com.accenture.shared.Type;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface VehiculeService {
    VehiculeDTO trouverTous();

    VehiculeDTO trouver(String modele) throws EntityNotFoundException;

    VehiculeDTO filtrer(Filtre filtre);

    //  VehiculeDTO rechercher(LocalDate dateDebut, LocalDate dateFin);

    // VehiculeDTO rechercherParDateEtType(LocalDate dateDebut, LocalDate dateFin, boolean inclureMotos, boolean inclureVoitures);

    VehiculeDTO rechercherParDateEtTypeEtCategorie(LocalDate dateDebut, LocalDate dateFin, boolean inclureMotos, boolean inclureVoitures, Type type);
}
