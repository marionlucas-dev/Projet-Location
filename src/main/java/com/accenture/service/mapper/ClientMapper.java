package com.accenture.service.mapper;

import com.accenture.repository.entity.utilisateurs.Client;
import com.accenture.service.dto.utilisateurs.ClientRequestDTO;
import com.accenture.service.dto.utilisateurs.ClientResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper permettant la conversion entre les objets {@link Client}, {@link ClientRequestDTO},
 * {@link ClientResponseDTO} ainsi que l'utilisation du {@link AdresseMapper} pour la conversion des adresses.
 * Ce mapper utilise MapStruct pour effectuer les transformations automatiquement.
 */
@Mapper(componentModel = "spring", uses = AdresseMapper.class)
public interface ClientMapper {

    /**
     * Convertit un {@link ClientRequestDTO} en une entité {@link Client}.
     *
     * @param clientRequestDTO L'objet DTO contenant les informations du client.
     * @return L'entité {@link Client} correspondante.
     */
    @Mapping(source = "email", target = "login")
    Client toClient(ClientRequestDTO clientRequestDTO);

    /**
     * Convertit une entité {@link Client} en un {@link ClientResponseDTO}.
     *
     * @param client L'entité {@link Client} à convertir.
     * @return L'objet DTO contenant les informations du client.
     */
    @Mapping(source = "login", target = "email")
    ClientResponseDTO toClientResponseDTO(Client client);

    /**
     * Convertit une entité {@link Client} en un {@link ClientRequestDTO}.
     *
     * @param client L'entité {@link Client} à convertir.
     * @return L'objet DTO contenant les informations du client.
     */
    @Mapping(source = "login", target = "email")
    ClientRequestDTO toClientRequestDTO(Client client);
}
