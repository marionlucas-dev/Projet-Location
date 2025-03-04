package com.accenture.repository.entity.utilisateurs;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Représente un admin, héritant de {@link UtilisateurConnecte}.
 * Cette entité est mappé à la table "ADMIN" dans la base de données
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ADMIN")
public class Administrateur extends UtilisateurConnecte {

    private String fonction;


}
