package com.accenture.service;

import com.accenture.exception.MotoException;
import com.accenture.exception.VeloException;
import com.accenture.repository.VeloDAO;
import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.repository.entity.vehicules.Velo;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.service.dto.vehicules.VeloRequestDTO;
import com.accenture.service.dto.vehicules.VeloResponseDTO;
import com.accenture.service.mapper.VeloMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeloServiceImpl implements VeloService {

    private final VeloDAO veloDAO;
    private final VeloMapper veloMapper;


    public VeloServiceImpl(VeloDAO veloDAO, VeloMapper veloMapper) {
        this.veloDAO = veloDAO;
        this.veloMapper = veloMapper;
    }

    @Override
    public List<VeloResponseDTO> trouverTous() {
        return veloDAO.findAll().stream()
                .map(veloMapper::toVeloResponseDTO)
                .toList();
    }

    @Override
    public VeloResponseDTO trouver(String modele) throws EntityNotFoundException {
        Optional<Velo> optVelo = veloDAO.findByModele(modele);
        if (optVelo.isEmpty())
            throw new EntityNotFoundException("Voiture non trouvé");
        Velo velo = optVelo.get();
        return veloMapper.toVeloResponseDTO(velo);
    }

//    public VeloResponseDTO ajouter(VeloRequestDTO veloRequestDTO) throws VeloException {
//        verifierVelo(veloRequestDTO);
//        Velo velo = VeloMapper.toVelo(veloRequestDTO);
//        Velo veloEnreg = VeloDAO.save(velo);
//        return veloMapper.toVeloResponseDTO(veloEnreg);
//    }
//



    private static void verifierVelo(VeloRequestDTO veloRequestDTO){
        if (veloRequestDTO == null)
            throw new VeloException("Le vélo est null");
        if (veloRequestDTO.marque() == null || veloRequestDTO.marque().isBlank())
            throw new MotoException("La marque est obligatoire");
        if (veloRequestDTO.modele() == null || veloRequestDTO.modele().isBlank())
            throw new MotoException("Le modèle est obligatoire");
        if (veloRequestDTO.couleur() == null || veloRequestDTO.couleur().isBlank())
            throw new MotoException("La couleur est obligatoire");

    }



}
