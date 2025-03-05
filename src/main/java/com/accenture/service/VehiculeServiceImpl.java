package com.accenture.service;

import com.accenture.repository.LocationDAO;
import com.accenture.repository.MotoDAO;
import com.accenture.repository.VehiculeDAO;
import com.accenture.repository.VoitureDAO;
import com.accenture.repository.entity.utilisateurs.Location;
import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.repository.entity.vehicules.Vehicule;
import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.service.mapper.LocationMapper;
import com.accenture.service.mapper.MotoMapper;
import com.accenture.service.mapper.VehiculeMapper;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.Filtre;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final LocationDAO locationDAO;
    private final LocationMapper locationMapper;


    public VehiculeServiceImpl(VehiculeDAO vehiculeDAO, VehiculeMapper vehiculeMapper, MotoDAO motoDAO, MotoMapper motoMapper, VoitureDAO voitureDAO, VoitureMapper voitureMapper, LocationDAO locationDAO, LocationMapper locationMapper) {
        this.vehiculeDAO = vehiculeDAO;
        this.vehiculeMapper = vehiculeMapper;
        this.motoDAO = motoDAO;
        this.motoMapper = motoMapper;
        this.voitureDAO = voitureDAO;
        this.voitureMapper = voitureMapper;
        this.locationDAO = locationDAO;
        this.locationMapper = locationMapper;

    }


    @Override
    public VehiculeDTO trouverTous() {
        List<MotoResponseDTO> listeMotos = new ArrayList<>();
        List<VoitureResponseDTO> listeVoitures = new ArrayList<>();
        List<Vehicule> listeVehicules = vehiculeDAO.findAll();
        for (Vehicule v : listeVehicules) {
            if (v instanceof Moto moto) {
                listeMotos.add(motoMapper.toMotoResponseDTO(moto));
            } else if (v instanceof Voiture voiture) {
                listeVoitures.add(voitureMapper.toVoitureResponseDTO(voiture));
            }
        }
        VehiculeDTO dto = new VehiculeDTO(listeMotos, listeVoitures);

        return dto;
    }

    @Override
    public VehiculeDTO trouver(String modele) throws EntityNotFoundException {
        List<MotoResponseDTO> listeMotos = new ArrayList<>();
        List<VoitureResponseDTO> listeVoitures = new ArrayList<>();
        List<Vehicule> listeVehicules = vehiculeDAO.findByModele(modele);
        toVehiculeDTO2(modele, listeVehicules, listeMotos, listeVoitures);
        VehiculeDTO dto = new VehiculeDTO(listeMotos, listeVoitures);
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
        }
        ;
        VehiculeDTO dto = new VehiculeDTO(listeMotos, listeVoitures);
        return dto;
    }

    @Override
    public VehiculeDTO rechercher(LocalDate dateDebut, LocalDate dateFin) {
        List<Location> listeLocations = locationDAO.findAll();
        List<Vehicule> listeVehiculeIndispo = listeLocations.stream()
                .filter(location -> location.getDateDebut().isBefore(dateFin) && location.getDateFin().isAfter(dateDebut))
                .map(Location::getVehicule)
                .toList();
        List<Vehicule> listeTousLesVehicules = vehiculeDAO.findAll();
        listeTousLesVehicules.removeAll(listeVehiculeIndispo);
        List<MotoResponseDTO> listeMotos = new ArrayList<>();
        List<VoitureResponseDTO> listeVoitures = new ArrayList<>();
        toVehiculeDTO(listeTousLesVehicules, listeMotos, listeVoitures);
        VehiculeDTO dto = new VehiculeDTO(listeMotos, listeVoitures);
        return dto;
    }


     @Override
     public VehiculeDTO rechercherParDateEtType(LocalDate dateDebut, LocalDate dateFin, boolean inclureMotos, boolean inclureVoitures) {
        List<Location> listeLocations = locationDAO.findAll();

        // Filtrer les véhicules indisponibles sur la période donnée
        List<Vehicule> listeVehiculeIndispo = listeLocations.stream()
                .filter(location -> location.getDateDebut().isBefore(dateFin) && location.getDateFin().isAfter(dateDebut))
                .map(Location::getVehicule)
                .toList();

        // Récupérer tous les véhicules disponibles
        List<Vehicule> listeTousLesVehicules = vehiculeDAO.findAll();
        listeTousLesVehicules.removeAll(listeVehiculeIndispo);

        // Séparer les véhicules disponibles en motos et voitures
        List<MotoResponseDTO> listeMotos = new ArrayList<>();
        List<VoitureResponseDTO> listeVoitures = new ArrayList<>();

        for (Vehicule vehicule : listeTousLesVehicules) {
            if (inclureMotos && vehicule instanceof Moto moto) {
                listeMotos.add(motoMapper.toMotoResponseDTO(moto)); // Adapter selon ton DTO
            } else if (inclureVoitures && vehicule instanceof Voiture voiture) {
                listeVoitures.add(voitureMapper.toVoitureResponseDTO(voiture)); // Adapter selon ton DTO
            }
        }

        return new VehiculeDTO(listeMotos, listeVoitures);
    }


//************************************************************************************************************************
//                                               METHODES PRIVEES
//************************************************************************************************************************


    private void toVehiculeDTO(List<Vehicule> listeTousLesVehicules, List<MotoResponseDTO> listeMotos, List<VoitureResponseDTO> listeVoitures) {
        for (Vehicule v : listeTousLesVehicules) {
            if (v instanceof Moto moto) {
                Optional<Moto> optMoto = motoDAO.findByModele(moto.getModele());
                listeMotos.add(motoMapper.toMotoResponseDTO(moto));
            } else if (v instanceof Voiture voiture) {
                Optional<Voiture> optVoiture = voitureDAO.findByModele(voiture.getModele());
                listeVoitures.add(voitureMapper.toVoitureResponseDTO(voiture));
            }
        }
    }

    private void toVehiculeDTO2(String modele, List<Vehicule> listeVehicules, List<MotoResponseDTO> listeMotos, List<VoitureResponseDTO> listeVoitures) {
        for (Vehicule v : listeVehicules) {
            if (v instanceof Moto moto) {
                Optional<Moto> optMoto = motoDAO.findByModele(modele);
                listeMotos.add(motoMapper.toMotoResponseDTO(moto));
            } else if (v instanceof Voiture voiture) {
                Optional<Voiture> optVoiture = voitureDAO.findByModele(modele);
                listeVoitures.add(voitureMapper.toVoitureResponseDTO(voiture));
            }
        }
    }
}
