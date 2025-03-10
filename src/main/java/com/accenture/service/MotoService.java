package com.accenture.service;

import com.accenture.exception.MotoException;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.shared.enumerations.Filtre;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface MotoService {

    List<MotoResponseDTO> trouverTous();

    MotoResponseDTO trouver(long id) throws EntityNotFoundException;

    MotoResponseDTO ajouter(MotoRequestDTO motoRequestDTO) throws MotoException;

    MotoResponseDTO supprimer(long id) throws EntityNotFoundException;

    MotoResponseDTO modifier(long id, MotoRequestDTO motoRequestDTO) throws EntityNotFoundException, MotoException;

    List<MotoResponseDTO> filtrer(Filtre filtre);
}
