package com.accenture.repository.entity.vehicules;

import com.accenture.shared.Permis;
import com.accenture.shared.Transmission;
import com.accenture.shared.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * Représente une moto, héritant de {@link Vehicule}.
 * Cette entité est mappé à la table "MOTOS" dans la base de données
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MOTOS")
public class Moto extends Vehicule{

    private int nbrCylindres;
    private int cylindree;
    private double poids;
    private double puissance;
    private double hauteurSelle;
    private Transmission transmission;
    private Permis permis;










}
