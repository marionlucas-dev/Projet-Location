package com.accenture.service.mapper;

import com.accenture.repository.entity.utilisateurs.Adresse;
import com.accenture.service.dto.utilisateurs.AdresseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper permettant la conversion entre les objets {@link Adresse} et {@link AdresseDTO}.
 * Ce mapper utilise MapStruct pour effectuer les transformations automatiquement.
 */
@Mapper(componentModel = "spring")
public interface AdresseMapper {

    /**
     * Convertit un {@link AdresseDTO} en une entité {@link Adresse}.
     *
     * @param adresseDTO L'objet DTO contenant les informations de l'adresse.
     * @return L'entité {@link Adresse} correspondante.
     */
    Adresse toAdresse(AdresseDTO adresseDTO);

    /**
     * Convertit une entité {@link Adresse} en un {@link AdresseDTO}.
     *
     * @param adresse L'entité {@link Adresse} à convertir.
     * @return L'objet DTO contenant les informations de l'adresse.
     */
    AdresseDTO toAdresseDTO(Adresse adresse);
}
