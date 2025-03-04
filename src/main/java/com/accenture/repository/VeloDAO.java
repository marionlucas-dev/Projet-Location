package com.accenture.repository;

import com.accenture.repository.entity.vehicules.Velo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeloDAO extends JpaRepository<Velo, Integer> {
    Optional<Velo> findByModele(String modele);
}
