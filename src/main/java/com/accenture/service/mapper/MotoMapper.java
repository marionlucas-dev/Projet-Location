package com.accenture.service.mapper;

import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper permettant la conversion entre les objets {@link Moto}, {@link MotoRequestDTO}
 * et {@link MotoResponseDTO}.
 * Ce mapper utilise MapStruct pour effectuer les transformations automatiquement.
 */
@Mapper(componentModel = "spring")
public interface MotoMapper {

    /**
     * Convertit un {@link MotoRequestDTO} en une entité {@link Moto}.
     *
     * @param motoRequestDTO L'objet DTO contenant les informations de la moto.
     * @return L'entité {@link Moto} correspondante.
     */
    Moto toMoto(MotoRequestDTO motoRequestDTO);

    /**
     * Convertit une entité {@link Moto} en un {@link MotoResponseDTO}.
     *
     * @param moto L'entité {@link Moto} à convertir.
     * @return L'objet DTO représentant les informations de la moto.
     */
    MotoResponseDTO toMotoResponseDTO(Moto moto);

    /**
     * Convertit une entité {@link Moto} en un {@link MotoRequestDTO}.
     *
     * @param moto L'entité {@link Moto} à convertir.
     * @return L'objet DTO représentant les informations de la moto à mettre à jour.
     */
    MotoRequestDTO toMotoRequestDTO(Moto moto);
}
