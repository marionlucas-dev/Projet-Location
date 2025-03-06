package com.accenture.service.dto.vehicules;

import com.accenture.shared.enumerations.Permis;
import com.accenture.shared.enumerations.Transmission;
import com.accenture.shared.enumerations.Type;

public record MotoResponseDTO(
        int id,
        String marque,
        String modele,
        String couleur,
        int nbrCylindres,
        int cylindree,
        double poids,
        double puissance,
        double hauteurSelle,
        Type type,
        Transmission transmission,
        Permis permis,
        int tarifJournee,
        int kilometrage,
        Boolean actif,
        Boolean retireDuParc



) {
}
