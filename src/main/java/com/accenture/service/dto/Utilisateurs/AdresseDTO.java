package com.accenture.service.dto.Utilisateurs;

/**
 * Représente une adresse avec ses principales composantes.
 * Ce record est utilisé pour transférer des données d'adresse de manière immuable.
 *
 * @param rue        Le nom de la rue.
 * @param codePostal Le code postal de l'adresse.
 * @param ville      La ville de l'adresse.
 */


public record AdresseDTO(
        String rue,
        String codePostal,
        String ville
) {
}
