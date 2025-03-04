package com.accenture.service.dto.utilisateurs;

/**
 * Représente la réponse contenant les informations d'un admin.
 * Cette classe est un reccord immuable utilisé pour le transfert des données.
 * @param nom : nom de l'admin
 * @param prenom : prénom de l'admin
 * @param email : mail de l'admin (doit être valide)
 * @param fonction : fonction de l'admin
 */
public record AdministrateurResponseDTO(
        long id,
        String nom,
        String prenom,
        String email,
        String fonction
) {
}
