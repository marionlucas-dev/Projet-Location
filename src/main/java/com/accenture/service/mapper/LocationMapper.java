package com.accenture.service.mapper;

import com.accenture.repository.entity.utilisateurs.Location;
import com.accenture.service.dto.LocationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

   LocationDTO toLocationDTO (Location location);
}
