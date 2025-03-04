package com.accenture.repository.entity.utilisateurs;

import com.accenture.repository.entity.vehicules.Vehicule;
import com.accenture.shared.EtatLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LOCATION")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Client client;
    @ManyToOne(cascade = CascadeType.ALL)
    private Vehicule vehicule;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String kmParcourus;
    private LocalDate dateValidation;
    private EtatLocation etatLocation;


}