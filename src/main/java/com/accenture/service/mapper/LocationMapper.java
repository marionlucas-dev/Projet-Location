package com.accenture.service.mapper;

import com.accenture.repository.entity.utilisateurs.Location;
import com.accenture.service.dto.LocationDTO;
import org.mapstruct.Mapper;

/**
 * Mapper permettant la conversion entre l'entité {@link Location} et le DTO {@link LocationDTO}.
 * Ce mapper utilise MapStruct pour effectuer la transformation automatiquement.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {

   /**
    * Convertit une entité {@link Location} en un {@link LocationDTO}.
    *
    * @param location L'entité {@link Location} à convertir.
    * @return Le DTO {@link LocationDTO} représentant les informations de la location.
    */
   LocationDTO toLocationDTO(Location location);
}
