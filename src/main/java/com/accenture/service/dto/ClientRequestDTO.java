package com.accenture.service.dto;

import com.accenture.shared.Permis;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientRequestDTO(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "Le prenom est obligatoire")
        String prenom,

        @NotBlank(message = "L'email est obligatoire")
        @Email
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password,

        @NotNull(message = "L'adresse est obligatoire")
        AdresseRequestDTO adresse,

        @NotNull(message = "La date de naissance est obligatoire")
        @Past(message = "Vous devez avoir 18 ans pour crée un compte")
        LocalDate dateNaissance,
        Permis permis


) {
}
