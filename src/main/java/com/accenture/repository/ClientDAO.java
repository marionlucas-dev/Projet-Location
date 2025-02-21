package com.accenture.repository;

import com.accenture.repository.entity.Utilisateurs.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.LongToIntFunction;

public interface ClientDAO extends JpaRepository<Client, Long> {
}
