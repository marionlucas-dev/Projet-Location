package com.accenture.service.dto.vehicules;

import com.accenture.shared.Type;
import jakarta.validation.constraints.NotBlank;

public record VeloRequestDTO(

        @NotBlank(message = "La marque est obligatoire")
        String marque,
        @NotBlank(message = "Le modèle est obligatoire")
        String modele,
        @NotBlank(message = "La couleur est obligatoire")
        String couleur,
        @NotBlank(message = "La taille du cadre est obligatoire")
        int tailleDuCadre,
        @NotBlank(message = "Le poids est obligatoire")
        int poids,
        @NotBlank(message = "Renseignez si le vélo est électrique ou non est obligatoire")
        Boolean Electrique,
        int capaciteBatterie,
        int autonomie,
        @NotBlank(message = "Renseignez si ce sont des freins à disque ou non est obligatoire")
        Boolean FreinsADisque,
        @NotBlank(message = "Le type est obligatoire")
        Type type,
        @NotBlank(message = "Le tarif à la journée est obligatoire")
        String tarifJournee,
        @NotBlank(message = "Le kilométrage à la journée est obligatoire")
        String kilometrage,
        @NotBlank(message = "Le statut (location ou non) est obligatoire")
        Boolean actif,
        @NotBlank(message = "Le statut (véhicule supprimer ou non est obligatoire")
        Boolean retireDuParc




) {
}
