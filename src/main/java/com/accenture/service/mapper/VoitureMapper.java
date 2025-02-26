package com.accenture.service.mapper;

import com.accenture.repository.entity.Vehicules.Voiture;
import com.accenture.service.dto.Vehicules.VoitureRequestDTO;
import com.accenture.service.dto.Vehicules.VoitureResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoitureMapper {

    Voiture toVoiture(VoitureRequestDTO voitureRequestDTO);
    VoitureResponseDTO toVoitureResponseDTO(Voiture voiture);



}
