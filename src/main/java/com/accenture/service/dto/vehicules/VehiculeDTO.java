package com.accenture.service.dto.vehicules;

import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.repository.entity.vehicules.Vehicule;
import com.accenture.repository.entity.vehicules.Voiture;

import java.util.List;

public record VehiculeDTO(

        List<MotoResponseDTO> motos,
        List<VoitureResponseDTO> voitures









) {
}
