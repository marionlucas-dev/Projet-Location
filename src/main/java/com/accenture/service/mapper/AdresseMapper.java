package com.accenture.service.mapper;

import com.accenture.repository.entity.Utilisateurs.Adresse;
import com.accenture.service.dto.AdresseRequestDTO;
import com.accenture.service.dto.AdresseResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface AdresseMapper {

    Adresse toAdresse(AdresseRequestDTO adresseRequestDTO);
    AdresseResponseDTO toAdresseResponseDTO(Adresse adresse);
}
