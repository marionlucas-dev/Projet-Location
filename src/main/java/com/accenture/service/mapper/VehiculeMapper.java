package com.accenture.service.mapper;

import com.accenture.repository.entity.vehicules.Vehicule;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {

 // Vehicule toVehicule(VehiculeDTO vehiculeDTO);
 VehiculeDTO toVehiculeDTO(Vehicule vehicule);



}