package com.accenture.repository;

import com.accenture.repository.entity.vehicules.Moto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotoDAO extends JpaRepository<Moto, Long> {
    Optional<Moto> findByModele(String modele);
}
