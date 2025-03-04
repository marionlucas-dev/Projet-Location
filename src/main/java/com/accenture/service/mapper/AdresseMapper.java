package com.accenture.service.mapper;

import com.accenture.repository.entity.utilisateurs.Adresse;
import com.accenture.service.dto.utilisateurs.AdresseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdresseMapper {

    Adresse toAdresse(AdresseDTO adresseDTO);
    AdresseDTO toAdresseDTO(Adresse adresse);
}
