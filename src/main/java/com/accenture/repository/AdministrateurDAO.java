package com.accenture.repository;

import com.accenture.repository.entity.utilisateurs.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministrateurDAO extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByLogin(String login);
}
