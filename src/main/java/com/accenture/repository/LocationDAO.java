package com.accenture.repository;

import com.accenture.repository.entity.utilisateurs.Location;
import com.accenture.repository.entity.vehicules.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LocationDAO extends JpaRepository<Location, Integer> {

}
