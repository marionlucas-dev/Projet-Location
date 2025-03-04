package com.accenture.service.dto.vehicules;

import com.accenture.shared.Carburant;
import com.accenture.shared.NombrePortes;
import com.accenture.shared.Transmission;
import com.accenture.shared.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MotoResponseDTO(
        int id,
        String marque,
        String modele,
        String couleur,
        Type type,
        Transmission transmission,
        String tarifJournee,
        String kilometrage,
        Boolean actif,
        Boolean retireDuParc



) {
}
