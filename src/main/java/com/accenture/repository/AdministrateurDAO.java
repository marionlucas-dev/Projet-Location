package com.accenture.repository;

import com.accenture.repository.entity.Utilisateurs.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrateurDAO extends JpaRepository<Administrateur, Long> {
}
