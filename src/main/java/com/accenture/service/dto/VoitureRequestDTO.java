package com.accenture.service.dto;

import com.accenture.shared.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VoitureRequestDTO(

        @NotBlank(message = "La marque est obligatoire")
        String marque,

        @NotBlank(message = "Le modèle est obligatoire")
        String modele,

        @NotBlank(message = "La couleur est obligatoire")
        String couleur,

        @NotNull(message = "Le type est obligatoire")
        Type type,
        @NotBlank(message = "Le nombre de place est obligatoire")
        int nbrPlaces,

        @NotNull(message = "Le carburant est obligatoire")
        Carburant carburant,

        @NotNull(message = "Le nombre de porte est obligatoire")
        NombrePortes nbrPortes,

        @NotBlank(message = "L'info sur la clim est obligatoire")
        Boolean clim,

        @NotNull(message = "La transmission est obligatoire")
        Transmission transmission,

        @NotBlank(message = "Le bagage est obligatoire")
        int bagages

//        @NotNull(message = "Le permis est obligatoire")
//        Permis permis


) {


}
