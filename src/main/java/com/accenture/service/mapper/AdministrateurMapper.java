package com.accenture.service.mapper;

import com.accenture.repository.entity.Utilisateurs.Administrateur;
import com.accenture.repository.entity.Utilisateurs.Adresse;
import com.accenture.service.dto.AdministrateurRequestDTO;
import com.accenture.service.dto.AdministrateurResponseDTO;
import com.accenture.service.dto.AdresseRequestDTO;
import com.accenture.service.dto.AdresseResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministrateurMapper {

    @Mapping(source = "email", target = "login")
    Administrateur toAdministrateur(AdministrateurRequestDTO AdminRequestDTO);

    @Mapping(source = "login", target = "email")
    AdministrateurResponseDTO toAdminResponseDTO(Administrateur admin);

}
