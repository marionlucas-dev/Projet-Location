package com.accenture.service.dto;

import com.accenture.shared.Permis;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Représente une requête de création ou de mise à jour d'un client.
 * Cette classe utilise le modèle Record pour encapsuler les données de manière immuable.
 * Tous les params sont obligatoires (sauf permis).
 * @param nom : nom du client
 * @param prenom : prénom du client
 * @param email : mail du client (doit être valide)
 * @param password : mode de passe de l'admin
 * @param adresse : adresse du client
 * @param dateNaissance : date de naissance du client (doit être majeur)
 * @param permis : le ou les permis du client s'il en dispose
 */



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
        AdresseDTO adresse,

        @NotNull(message = "La date de naissance est obligatoire")
        @Past(message = "Vous devez avoir 18 ans pour crée un compte")
        LocalDate dateNaissance,
        Permis permis


) {
}
