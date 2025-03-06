package com.accenture.repository.entity.vehicules;

import com.accenture.shared.enumerations.Carburant;
import com.accenture.shared.enumerations.NombrePortes;
import com.accenture.shared.enumerations.Permis;
import com.accenture.shared.enumerations.Transmission;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * Représente une voiture, héritant de {@link Vehicule}.
 * Cette entité est mappée à la table "VOITURES" dans la base de données.
 */



@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VOITURES")
public class Voiture extends Vehicule {

    private int nbrPlaces;
    private Carburant carburant;
    private NombrePortes nbrPortes;
    private Boolean clim;
    private Transmission transmission;
    private int bagages;
    private Permis permis;



}
