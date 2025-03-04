package com.accenture.service.dto.vehicules;

import com.accenture.shared.*;

/**
 * Représente la réponse contentant les informations d'un client
 *
 * @param id            : identifiant de la voiture
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
 * @param permis        : permis de conduire nécessaire et qui est déduit du nombre de places dans la voiture
 */

public record VoitureResponseDTO(

        int id,
        String marque,
        String modele,
        String couleur,
        Type type,
        int nbrPlaces,
        Carburant carburant,
        NombrePortes nbrPortes,
        Boolean clim,
        Transmission transmission,
        int bagages,
        Permis permis,
       String tarifJournee,
        String kilometrage,
        Boolean actif,
        Boolean retireDuParc

) {


}
