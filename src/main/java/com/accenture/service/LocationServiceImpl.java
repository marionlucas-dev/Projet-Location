package com.accenture.service;

import com.accenture.repository.LocationDAO;
import com.accenture.service.dto.LocationDTO;
import com.accenture.service.mapper.LocationMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationServiceImpl {

    private final LocationDAO locationDAO;
    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationDAO locationDAO, LocationMapper locationMapper) {
        this.locationDAO = locationDAO;
        this.locationMapper = locationMapper;
    }

    /**
     * Récupère la liste de toutes les locations et les convertit en objets {@link LocationDTO}.
     *
     * @return une liste de {@link LocationDTO} représentant toutes les locations.
     */

    public List<LocationDTO> trouverTous(){
        return locationDAO.findAll().stream()
                .map(locationMapper::toLocationDTO)
                .toList();
    }




}
