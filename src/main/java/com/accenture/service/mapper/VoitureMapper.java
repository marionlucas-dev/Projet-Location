package com.accenture.service.mapper;

import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoitureMapper {

    Voiture toVoiture(VoitureRequestDTO voitureRequestDTO);

    VoitureResponseDTO toVoitureResponseDTO(Voiture voiture);

    VoitureRequestDTO toVoitureRequestDTO(Voiture voiture);


}
