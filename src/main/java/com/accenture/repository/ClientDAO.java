package com.accenture.repository;

import com.accenture.repository.entity.utilisateurs.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDAO extends JpaRepository<Client, Long> {
    Optional<Client> findByLogin(String login);
}
