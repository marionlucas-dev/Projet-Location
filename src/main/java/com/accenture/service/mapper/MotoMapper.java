package com.accenture.service.mapper;

import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MotoMapper {

    Moto toMoto (MotoRequestDTO motoRequestDTO);
    MotoResponseDTO toMotoResponseDTO (Moto moto);
    MotoRequestDTO toMotoRequestDTO (Moto moto);




}
