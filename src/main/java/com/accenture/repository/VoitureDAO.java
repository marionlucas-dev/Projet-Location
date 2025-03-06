package com.accenture.repository;
import com.accenture.repository.entity.vehicules.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoitureDAO extends JpaRepository<Voiture, Long> {
}
