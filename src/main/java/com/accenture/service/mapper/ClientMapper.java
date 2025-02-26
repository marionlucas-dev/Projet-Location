package com.accenture.service.mapper;

import com.accenture.repository.entity.Utilisateurs.Client;
import com.accenture.service.dto.Utilisateurs.ClientRequestDTO;
import com.accenture.service.dto.Utilisateurs.ClientResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = AdresseMapper.class)
public interface ClientMapper {

    @Mapping(source = "email", target = "login")
    Client toClient (ClientRequestDTO clientRequestDTO);

    @Mapping(source = "login", target = "email")
    ClientResponseDTO toClientResponseDTO(Client client);

    @Mapping(source = "login", target = "email")
    ClientRequestDTO toClientRequestDTO (Client client);
}
