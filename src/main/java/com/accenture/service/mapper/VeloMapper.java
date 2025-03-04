package com.accenture.service.mapper;

import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.repository.entity.vehicules.Velo;
import com.accenture.service.dto.vehicules.VeloRequestDTO;
import com.accenture.service.dto.vehicules.VeloResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeloMapper {

    Velo toVelo(VeloRequestDTO veloRequestDTO);
    VeloResponseDTO toVeloResponseDTO(Velo velo);
    VeloRequestDTO toVeloRequestDTO (Velo velo);





}
