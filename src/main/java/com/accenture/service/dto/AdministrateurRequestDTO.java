package com.accenture.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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
