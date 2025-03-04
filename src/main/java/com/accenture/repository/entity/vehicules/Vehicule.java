package com.accenture.repository.entity.vehicules;

import com.accenture.shared.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * /**
 * Représente un véhicule abstrait qui sert de classe de base pour d'autres types de véhicules.
 * Cette classe utilise l'héritage avec la stratégie {@code InheritanceType.JOINED}, ce qui signifie
 * que chaque sous-classe aura sa propre table en base de données tout en partageant les colonnes communes
 * définies ici.
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehicule {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    private String marque;
    private String modele;
    private String couleur;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String tarifJournee;
    private String kilometrage;
    private Boolean actif;
    private Boolean retireDuParc;
}
