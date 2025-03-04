package com.accenture.service.dto.vehicules;

import com.accenture.shared.Type;

public record VeloResponseDTO(

        int id,
        String marque,
        String modele,
        String couleur,
        int tailleDuCadre,
        int poids,
        Boolean Electrique,
        int capaciteBatterie,
        int autonomie,
        Boolean FreinsADisque,
        Type type,
        String tarifJournee,
        String kilometrage,
        Boolean actif,
        Boolean retireDuParc





) {
}
