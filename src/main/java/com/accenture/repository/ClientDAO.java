package com.accenture.repository;

import com.accenture.repository.entity.Utilisateurs.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.function.LongToIntFunction;

public interface ClientDAO extends JpaRepository<Client, Long> {
    boolean existsByPassword(String password);
    boolean existsByLogin(String login);

    Optional<Client> findByLogin(String login);
}
