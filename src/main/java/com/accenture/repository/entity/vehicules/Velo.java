package com.accenture.repository.entity.vehicules;

import com.accenture.shared.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VELOS")
public class Velo extends Vehicule {

    private int tailleDuCadre;
    private int poids;
    private Boolean Electrique;
    private int capaciteBatterie;
    private int autonomie;
    private Boolean FreinsADisque;
    private Type type;


}
