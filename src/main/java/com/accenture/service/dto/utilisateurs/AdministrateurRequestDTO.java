package com.accenture.service.dto.utilisateurs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Représente une requête de création ou de mise à jour d'un administrateur.
 * Cette classe utilise le modèle Record pour encapsuler les données de manière immuable.
 * Tous les params sont obligatoires.
 * @param nom : nom de l'admin
 * @param prenom : prénom de l'admin
 * @param email : mail de l'admin (doit être valide)
 * @param password : mode de passe de l'admin
 * @param fonction : fonction de l'admin
 */

public record AdministrateurRequestDTO(

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "Le prenom est obligatoire")
        String prenom,

        @NotBlank(message = "L'email est obligatoire")
        @Email
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password,

        @NotBlank (message = "La fonction est obligatoire")
        String fonction



) {
}
