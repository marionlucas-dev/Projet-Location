package com.accenture.service.mapper;

import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper permettant la conversion entre l'entité {@link Voiture}, le DTO {@link VoitureRequestDTO}
 * et le DTO {@link VoitureResponseDTO}.
 * Ce mapper utilise MapStruct pour effectuer les transformations automatiquement.
 */
@Mapper(componentModel = "spring")
public interface VoitureMapper {

    /**
     * Convertit un {@link VoitureRequestDTO} en une entité {@link Voiture}.
     *
     * @param voitureRequestDTO L'objet DTO contenant les informations de la voiture.
     * @return L'entité {@link Voiture} correspondante.
     */
    Voiture toVoiture(VoitureRequestDTO voitureRequestDTO);

    /**
     * Convertit une entité {@link Voiture} en un {@link VoitureResponseDTO}.
     *
     * @param voiture L'entité {@link Voiture} à convertir.
     * @return Le DTO {@link VoitureResponseDTO} contenant les informations de la voiture.
     */
    VoitureResponseDTO toVoitureResponseDTO(Voiture voiture);

    /**
     * Convertit une entité {@link Voiture} en un {@link VoitureRequestDTO}.
     *
     * @param voiture L'entité {@link Voiture} à convertir.
     * @return Le DTO {@link VoitureRequestDTO} contenant les informations de la voiture.
     */
    VoitureRequestDTO toVoitureRequestDTO(Voiture voiture);
}
