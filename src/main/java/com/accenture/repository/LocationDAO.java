package com.accenture.repository;

import com.accenture.repository.entity.utilisateurs.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDAO extends JpaRepository<Location, Integer> {

}
