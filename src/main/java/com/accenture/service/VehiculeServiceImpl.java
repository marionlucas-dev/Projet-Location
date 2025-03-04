package com.accenture.service;

import com.accenture.repository.MotoDAO;
import com.accenture.repository.VehiculeDAO;
import com.accenture.repository.VoitureDAO;
import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.repository.entity.vehicules.Vehicule;
import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.service.mapper.MotoMapper;
import com.accenture.service.mapper.VehiculeMapper;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.Filtre;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeDAO vehiculeDAO;
    private final VehiculeMapper vehiculeMapper;
    private final MotoDAO motoDAO;
    private final MotoMapper motoMapper;
    private final VoitureDAO voitureDAO;
    private final VoitureMapper voitureMapper;


    public VehiculeServiceImpl(VehiculeDAO vehiculeDAO, VehiculeMapper vehiculeMapper, MotoDAO motoDAO, MotoMapper motoMapper, VoitureDAO voitureDAO, VoitureMapper voitureMapper) {
        this.vehiculeDAO = vehiculeDAO;
        this.vehiculeMapper = vehiculeMapper;
        this.motoDAO = motoDAO;
        this.motoMapper = motoMapper;
        this.voitureDAO = voitureDAO;
        this.voitureMapper = voitureMapper;
    }


    @Override
    public VehiculeDTO trouverTous() {
        List<MotoResponseDTO> listeMotos = new ArrayList<>();
        List<VoitureResponseDTO> listeVoitures= new ArrayList<>();
        List<Vehicule> listeVehicules = vehiculeDAO.findAll();
        for (Vehicule v : listeVehicules) {
            if (v instanceof Moto moto) {
                listeMotos.add(motoMapper.toMotoResponseDTO(moto));
            } else if (v instanceof Voiture voiture) {
                listeVoitures.add(voitureMapper.toVoitureResponseDTO(voiture));
            }
        }
        VehiculeDTO dto= new VehiculeDTO(listeMotos,listeVoitures);

        return dto;
    }


    @Override
    public VehiculeDTO trouver(String modele) throws EntityNotFoundException {
       List<MotoResponseDTO> listeMotos = new ArrayList<>();
       List<VoitureResponseDTO> listeVoitures = new ArrayList<>();

       List<Vehicule> listeVehicules = vehiculeDAO.findByModele(modele);

       for (Vehicule v : listeVehicules){
           if (v instanceof Moto moto){
               Optional<Moto> optMoto = motoDAO.findByModele(modele);
              listeMotos.add(motoMapper.toMotoResponseDTO(moto));
           } else if (v instanceof Voiture voiture) {
               Optional<Voiture> optVoiture = voitureDAO.findByModele(modele);
               listeVoitures.add(voitureMapper.toVoitureResponseDTO(voiture));
           }


       }
        VehiculeDTO dto = new VehiculeDTO(listeMotos,listeVoitures);
        return dto;
    }


    @Override
    public VehiculeDTO filtrer(Filtre filtre) {
        VehiculeDTO listeVehicule = trouverTous();
        List<MotoResponseDTO> listeMotos = new ArrayList<>();
        List<VoitureResponseDTO> listeVoitures = new ArrayList<>();
                switch (filtre) {
                    case ACTIF -> {
                        listeMotos = listeVehicule.motos().stream()
                                .filter(MotoResponseDTO::actif)
                                .toList();

                        listeVoitures = listeVehicule.voitures().stream()
                                .filter(VoitureResponseDTO::actif)
                                .toList();
                    }

                    case INACTIF -> {
                        listeMotos = listeVehicule.motos().stream()
                                .filter(motoResponseDTO -> !motoResponseDTO.actif())
                                .toList();

                        listeVoitures = listeVehicule.voitures().stream()
                                .filter(voitureResponseDTO -> !voitureResponseDTO.actif())
                                .toList();
                    }

                    case HORSPARC -> {
                        listeMotos = listeVehicule.motos().stream()
                                .filter(MotoResponseDTO::retireDuParc)
                                .toList();

                        listeVoitures = listeVehicule.voitures().stream()
                                .filter(VoitureResponseDTO::retireDuParc)
                                .toList();
                    }


                    case DANSLEPARC -> {
                        listeMotos = listeVehicule.motos().stream()
                                .filter(motoResponseDTO -> !motoResponseDTO.retireDuParc())
                                .toList();

                        listeVoitures = listeVehicule.voitures().stream()
                                .filter(voitureResponseDTO -> !voitureResponseDTO.retireDuParc())
                                .toList();
                    }
                    default -> throw new IllegalArgumentException(STR."Le filtre n'est pas disponible\{filtre}");
                };
        VehiculeDTO dto = new VehiculeDTO(listeMotos,listeVoitures);
        return dto;
    }


}
