package com.accenture.service.dto;

import com.accenture.shared.Permis;

import java.time.LocalDate;

public record ClientResponseDTO(
        long id,
        String nom,
        String prenom,
        String email,
        String password,
        AdresseResponseDTO adresse,
        LocalDate dateNaissance,
        Permis permis,
        LocalDate dateInscription

) {
}
