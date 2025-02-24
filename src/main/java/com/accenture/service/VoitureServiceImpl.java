package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.repository.VoitureDAO;
import com.accenture.repository.entity.Voiture.Voiture;
import com.accenture.service.dto.VoitureRequestDTO;
import com.accenture.service.dto.VoitureResponseDTO;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.Permis;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoitureServiceImpl implements VoitureService {

    private final VoitureDAO voitureDAO;
    private final VoitureMapper voitureMapper;


    public VoitureServiceImpl(VoitureDAO voitureDAO, VoitureMapper voitureMapper) {
        this.voitureDAO = voitureDAO;
        this.voitureMapper = voitureMapper;
    }

    @Override
    public List<VoitureResponseDTO> trouverTous() {
        return voitureDAO.findAll().stream()
                .map(voitureMapper::toVoitureResponseDTO)
                .toList();
    }

    @Override
    public VoitureResponseDTO trouver(String modele) throws EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDAO.findByModele(modele);
        if (optVoiture.isEmpty())
            throw new EntityNotFoundException("Voiture non trouvé");
        Voiture voiture = optVoiture.get();
        return voitureMapper.toVoitureResponseDTO(voiture);
    }

    @Override
    public VoitureResponseDTO ajouter(VoitureRequestDTO voitureRequestDTO) throws VoitureException{
        verifierVoiture(voitureRequestDTO);
        Voiture voiture = voitureMapper.toVoiture(voitureRequestDTO);
        if (voitureRequestDTO.nbrPlaces() > 0 && voitureRequestDTO.nbrPlaces() <= 9) {
            voiture.setPermis(Permis.B) ;
        }

        else {
            voiture.setPermis(Permis.D1);
        }

        Voiture voitureEnreg= voitureDAO.save(voiture);
        return voitureMapper.toVoitureResponseDTO(voitureEnreg);
    }

    private static void verifierVoiture(VoitureRequestDTO voitureRequestDTO){
        if (voitureRequestDTO == null)
            throw new VoitureException("La voiture est null");
        if (voitureRequestDTO.marque() == null || voitureRequestDTO.marque().isBlank())
            throw new VoitureException("La marque est obligatoire");
        if (voitureRequestDTO.modele() == null || voitureRequestDTO.modele().isBlank())
            throw new VoitureException("Le modèle est obligaotire");
        if (voitureRequestDTO.couleur() == null || voitureRequestDTO.couleur().isBlank())
            throw new VoitureException("La couleur du véhicule est obligaotire");
        if (voitureRequestDTO.nbrPlaces() != 3 && voitureRequestDTO.nbrPlaces() != 5 )
            throw new VoitureException("Le nombre de places  est obligaotire");
        if (voitureRequestDTO.carburant() == null)
            throw new VoitureException("Le carburant est obligaotire");
        if (voitureRequestDTO.nbrPortes() == null)
            throw new VoitureException("Le nombre de porte est obligatoire (3 ou 5) est obligaotire");
        if (voitureRequestDTO.transmission() == null)
            throw new VoitureException("Le type de transmission est obligaotire");
        if (voitureRequestDTO.clim() == null)
            throw new VoitureException("La présence ou non de clim est obligaotire");
        if (voitureRequestDTO.bagages() <= 0)
            throw new VoitureException("Le modèle est obligaotire");
        if (voitureRequestDTO.type() == null)
            throw new VoitureException("Le type de voiture  est obligaotire");
//        if (voitureRequestDTO.permis() == null)
//            throw new VoitureException("Le permis est obligaotire");
    }







}
