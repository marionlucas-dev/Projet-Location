package com.accenture.service.dto;

import com.accenture.shared.*;

public record VoitureResponseDTO (

        long id,
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
        Permis permis
//        String tarifJournee,
//        String kilometrage,
//        Boolean actif,
//        Boolean retireDuParc

) {


}
