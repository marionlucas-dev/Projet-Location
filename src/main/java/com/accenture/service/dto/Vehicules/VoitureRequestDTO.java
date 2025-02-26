package com.accenture.service.dto.Vehicules;

import com.accenture.shared.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Représente une requête de création ou de mise à jour d'une voiture.
 * Cette classe utilise le modèle Record pour encapsuler les données de manière immuable.
 * Tous les params sont obligatoires
 *
 * @param marque        : marque de la voiture
 * @param modele        : modèle de la voiture
 * @param couleur       : couleur de la voiture
 * @param type          : type de la voiture (ex: SUV, citadine, etc)
 * @param nbrPlaces     : Nbr de places dispo dans le véhicule (qui engendra la demande de permis)
 * @param carburant:    le type de carburant utilisé
 * @param nbrPortes:    Nbr de porte (3 ou 5)
 * @param clim          : oui / non
 * @param transmission: Auto/manuel
 * @param bagages:      nombre de bagages transportables
 */


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
        int bagages,

        @NotBlank(message = "Le tarif à la journée est obligatoire")
        String tarifJournee,

        @NotBlank(message = "Le kilométrage est obligatoire")
        String kilometrage,

        @NotBlank(message = "Le statut (location ou non) de la voiture  est obligatoire")
        Boolean actif,

        @NotBlank(message = "Le statut (véhicule supprimer ou non est obligatoire")
        Boolean retireDuParc

) {


}
