package com.accenture.service.mapper;

import com.accenture.repository.entity.vehicules.Vehicule;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper permettant la conversion entre l'entité {@link Vehicule} et le DTO {@link VehiculeDTO}.
 * Ce mapper utilise MapStruct pour effectuer les transformations automatiquement.
 */
@Mapper(componentModel = "spring")
public interface VehiculeMapper {

 /**
  * Convertit une entité {@link Vehicule} en un {@link VehiculeDTO}.
  *
  * @param vehicule L'entité {@link Vehicule} à convertir.
  * @return Le DTO {@link VehiculeDTO} contenant les informations du véhicule.
  */
 VehiculeDTO toVehiculeDTO(Vehicule vehicule);
}
