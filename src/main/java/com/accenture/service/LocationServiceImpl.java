package com.accenture.service;

import com.accenture.repository.LocationDAO;
import com.accenture.service.dto.LocationDTO;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.service.mapper.LocationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LocationServiceImpl {

    private final LocationDAO locationDAO;
    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationDAO locationDAO, LocationMapper locationMapper) {
        this.locationDAO = locationDAO;
        this.locationMapper = locationMapper;
    }


    public List<LocationDTO> trouverTous(){
        return locationDAO.findAll().stream()
                .map(locationMapper::toLocationDTO)
                .toList();
    }
//
//    public List<LocationDTO> rechercher(LocalDate dateDebut, LocalDate dateFin) {
//        return trouverTous().stream()
//                .filter(locationDTO ->
//                        dateDebut.isBefore(locationDTO.getDateFin()) && dateFin.isAfter(locationDTO.getDateDebut())
//                )
//                .map(locationDTO ->locationDTO.))
//                .toList();
//    }




}
