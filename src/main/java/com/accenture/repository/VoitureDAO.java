package com.accenture.repository;

import com.accenture.repository.entity.Vehicules.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoitureDAO extends JpaRepository<Voiture, Long> {
    Optional<Voiture> findByModele(String modele);
}
