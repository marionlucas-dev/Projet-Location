package com.accenture.repository.entity.Utilisateurs;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe abstraite représentant un utilisateur connecté.
 * Cette entité utilise une stratégie d'héritage de type joined.
 * Permettant aux classes dérivées de stocker leurs propres attributs tout en partageant une clé primaire unique.
 */



@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UtilisateurConnecte {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @Column(unique = true)
    private String login;

    private String password;
    private String nom;
    private String prenom;

}
