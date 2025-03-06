package com.accenture.service.mapper;

import com.accenture.repository.entity.utilisateurs.Administrateur;
import com.accenture.service.dto.utilisateurs.AdministrateurRequestDTO;
import com.accenture.service.dto.utilisateurs.AdministrateurResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper permettant la conversion entre les objets {@link Administrateur}, {@link AdministrateurRequestDTO}
 * et {@link AdministrateurResponseDTO}.
 *Ce mapper utilise MapStruct pour effectuer les transformations automatiquement.
 */
@Mapper(componentModel = "spring")
public interface AdministrateurMapper {

    /**
     * Convertit un {@link AdministrateurRequestDTO} en une entité {@link Administrateur}.
     *
     * @param adminRequestDTO L'objet DTO contenant les informations de l'administrateur.
     * @return L'entité {@link Administrateur} correspondante.
     */
    @Mapping(source = "email", target = "login")
    Administrateur toAdministrateur(AdministrateurRequestDTO adminRequestDTO);

    /**
     * Convertit une entité {@link Administrateur} en un {@link AdministrateurResponseDTO}.
     *
     * @param admin L'entité {@link Administrateur} à convertir.
     * @return L'objet DTO contenant les informations de l'administrateur.
     */
    @Mapping(source = "login", target = "email")
    AdministrateurResponseDTO toAdminResponseDTO(Administrateur admin);

    /**
     * Convertit une entité {@link Administrateur} en un {@link AdministrateurRequestDTO}.
     *
     * @param admin L'entité {@link Administrateur} à convertir.
     * @return L'objet DTO contenant les informations de l'administrateur.
     */
    @Mapping(source = "login", target = "email")
    AdministrateurRequestDTO toAdminRequestDTO(Administrateur admin);

}

