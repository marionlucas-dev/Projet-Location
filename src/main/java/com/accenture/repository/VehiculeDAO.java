package com.accenture.repository;

import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.repository.entity.vehicules.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculeDAO extends JpaRepository<Vehicule, Integer> {


    List<Vehicule> findByModele(String modele);
}
