package com.accenture.service.dto.vehicules;

import com.accenture.shared.enumerations.Transmission;
import com.accenture.shared.enumerations.Type;
import jakarta.validation.constraints.NotBlank;

public record MotoRequestDTO(

        @NotBlank(message = "La marque est obligatoire")
        String marque,
        @NotBlank(message = "Le modèle est obligatoire")
        String modele,
        @NotBlank(message = "La couleur est obligatoire")
        String couleur,
        @NotBlank(message = "Le nombre de cylindres est obligatoire")
        int nbrCylindres,
        @NotBlank(message = "Les cylindrées est obligatoire")
        int cylindree,
        @NotBlank(message = "Le poids est obligatoire")
        double poids,
        @NotBlank(message = "La puissance est obligatoire")
        double puissance,
        @NotBlank(message = "La hauteur de selle est obligatoire")
        double hauteurSelle,
        @NotBlank(message = "La la transmission est obligatoire")
        Transmission transmission,
        @NotBlank(message = "Le type est obligatoire")
        Type type,
        @NotBlank(message = "Le tarif à la journée est obligatoire")
        int tarifJournee,
        @NotBlank(message = "Le kilométrage à la journée est obligatoire")
        int kilometrage,
        @NotBlank(message = "Le statut (location ou non) est obligatoire")
        Boolean actif,
        @NotBlank(message = "Le statut (véhicule supprimer ou non est obligatoire")
        Boolean retireDuParc







) {
}
