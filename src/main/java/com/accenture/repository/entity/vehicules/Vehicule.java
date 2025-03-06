package com.accenture.repository.entity.vehicules;

import com.accenture.shared.enumerations.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Représente un véhicule abstrait qui sert de classe de base pour d'autres types de véhicules.
 * Cette classe utilise l'héritage avec la stratégie {@code InheritanceType.JOINED}, ce qui signifie
 * que chaque sous-classe aura sa propre table en base de données tout en partageant les colonnes communes
 * définies ici.
 * Les sous-classes de {@code Vehicule} hériteront des propriétés et comportements communs tout en étant
 * stockées dans leurs propres tables, selon la stratégie d'héritage "JOINED".
 */


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehicule {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;
    private String marque;
    private String modele;
    private String couleur;
    @Enumerated(EnumType.STRING)
    private Type type;
    private int tarifJournee;
    private int kilometrage;
    private Boolean actif;
    private Boolean retireDuParc;
}
