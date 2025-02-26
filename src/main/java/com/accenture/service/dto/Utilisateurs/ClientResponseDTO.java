package com.accenture.service.dto.Utilisateurs;

import com.accenture.shared.Permis;

import java.time.LocalDate;

/**
 * Représente la réponse contentant les informations d'un client
 *
 * @param id              : id de l'utilisateur
 * @param dateInscription : date de l'inscription
 * @param nom             : nom du client
 * @param prenom          : prénom du client
 * @param email           : mail du client (doit être valide)
 * @param adresse         : adresse du client
 * @param dateNaissance   : date de naissance du client (doit être majeur)
 * @param permis          : le ou les permis du client s'il en dispose
 */

public record ClientResponseDTO(
        long id,
        String nom,
        String prenom,
        String email,
        AdresseDTO adresse,
        LocalDate dateNaissance,
        Permis permis,
        LocalDate dateInscription

) {
}
