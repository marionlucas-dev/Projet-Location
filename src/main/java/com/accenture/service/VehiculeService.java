package com.accenture.service;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.shared.enumerations.Filtre;
import com.accenture.shared.enumerations.Type;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;

public interface VehiculeService {
   VehiculeDTO  trouverTous();
    VehiculeDTO trouver(long id) throws EntityNotFoundException;
    VehiculeDTO filtrer(Filtre filtre);
    VehiculeDTO rechercherParDateEtTypeEtCategorie(LocalDate dateDebut, LocalDate dateFin, boolean inclureMotos, boolean inclureVoitures, Type type);
}
