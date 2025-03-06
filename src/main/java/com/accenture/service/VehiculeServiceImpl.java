package com.accenture.service;

import com.accenture.repository.LocationDAO;
import com.accenture.repository.VehiculeDAO;
import com.accenture.repository.entity.utilisateurs.Location;
import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.repository.entity.vehicules.Vehicule;
import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.service.mapper.MotoMapper;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.enumerations.Filtre;
import com.accenture.shared.enumerations.Type;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implémentation du service {@link VehiculeService} permettant la gestion des véhicules.
 * Cette classe fournit des méthodes pour récupérer, filtrer et rechercher des véhicules en fonction de divers critères.
 * Elle utilise les objets {@link VehiculeDAO}, {@link MotoMapper}, {@link VoitureMapper} et {@link LocationDAO}
 * pour interagir avec les données et effectuer les transformations nécessaires.
 */


@Service
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeDAO vehiculeDAO;
    private final MotoMapper motoMapper;
    private final VoitureMapper voitureMapper;
    private final LocationDAO locationDAO;


    public VehiculeServiceImpl(VehiculeDAO vehiculeDAO, MotoMapper motoMapper, VoitureMapper voitureMapper, LocationDAO locationDAO) {
        this.vehiculeDAO = vehiculeDAO;
        this.motoMapper = motoMapper;
        this.voitureMapper = voitureMapper;
        this.locationDAO = locationDAO;

    }


    /**
     * Récupère la liste de tous les véhicules et les convertit en un objet {@link VehiculeDTO}.
     *
     * @return un objet {@link VehiculeDTO} contenant la liste des véhicules.
     */

    @Override
    public VehiculeDTO trouverTous() {
        return convertirToVehiculeDTO(vehiculeDAO.findAll(), true, true);
    }

    /**
     * Récupère un véhicule en fonction de son identifiant et le convertit en un objet {@link VehiculeDTO}.
     *
     * @param id l'identifiant du véhicule.
     * @return un objet {@link VehiculeDTO} représentant le véhicule trouvé.
     * @throws EntityNotFoundException si aucun véhicule ne correspond à l'identifiant fourni.
     */



    @Override
    public VehiculeDTO trouver(long id) throws EntityNotFoundException {
        Optional<Vehicule> byId = vehiculeDAO.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Pas de véhicule présent avec cet id");
        return convertirToVehiculeDTO(List.of(byId.get()), true, true);
    }

    /**
     * Filtre les véhicules en fonction de leur statut : actif, inactif, dans le parc ou hors du parc.
     *
     * @param filtre une valeur de l'énumération {@link Filtre} définissant le critère de filtrage.
     * @return un objet {@link VehiculeDTO} contenant la liste des véhicules correspondant au filtre appliqué.
     * @throws IllegalArgumentException si le filtre spécifié n'est pas reconnu.
     */

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
        VehiculeDTO dto = new VehiculeDTO(listeMotos, listeVoitures);
        return dto;
    }

    public VehiculeDTO filtrer2(Filtre filtre) {
        VehiculeDTO listeVehicule = trouverTous();
        Predicate<MotoResponseDTO> motoResponseDTOPredicate = motoDto -> false;
        Predicate<VoitureResponseDTO> voitureResponseDTOPredicate = voitureDto -> false;

        switch (filtre) {
            case ACTIF -> {
                motoResponseDTOPredicate = MotoResponseDTO::actif;
                voitureResponseDTOPredicate = VoitureResponseDTO::actif;
            }
            case INACTIF -> {
                motoResponseDTOPredicate = motoResponseDTO -> !motoResponseDTO.actif();
                voitureResponseDTOPredicate = voitureResponseDTO -> !voitureResponseDTO.actif();
            }
            case HORSPARC -> {
                motoResponseDTOPredicate = MotoResponseDTO::retireDuParc;
                voitureResponseDTOPredicate = VoitureResponseDTO::retireDuParc;
            }
            case DANSLEPARC -> {
                motoResponseDTOPredicate = motoResponseDTO -> !motoResponseDTO.retireDuParc();
                voitureResponseDTOPredicate = voitureResponseDTO -> !voitureResponseDTO.retireDuParc();
            }
            default -> throw new IllegalArgumentException(STR."Le filtre n'est pas disponible\{filtre}");
        }

        return new VehiculeDTO(
                extraireMotosResponseDto(listeVehicule, motoResponseDTOPredicate),
                extraireVoituresResponseDto(listeVehicule, voitureResponseDTOPredicate)
        );

    }

    /**
     * Recherche des véhicules disponibles pour la location en fonction des dates de début et de fin,
     * avec la possibilité de filtrer par type de véhicule et d'inclure ou non les motos et les voitures.
     *
     * @param dateDebut date de début de la réservation.
     * @param dateFin date de fin de la réservation.
     * @param inclureMotos {@code true} pour inclure les motos dans la recherche, {@code false} sinon.
     * @param inclureVoitures {@code true} pour inclure les voitures dans la recherche, {@code false} sinon.
     * @param type une valeur de l'énumération {@link Type} représentant le type de véhicule recherché.
     * @return un objet {@link VehiculeDTO} contenant la liste des véhicules disponibles selon les critères spécifiés.
     */


    @Override
    public VehiculeDTO rechercherParDateEtTypeEtCategorie(LocalDate dateDebut, LocalDate dateFin, boolean inclureMotos, boolean inclureVoitures, Type type) {
        List<Vehicule> listeTousLesVehicules = rechercherParDate(dateDebut, dateFin);
        listeTousLesVehicules = filtrerParType(type, listeTousLesVehicules);
        return convertirToVehiculeDTO(listeTousLesVehicules, inclureMotos, inclureVoitures);
    }


//************************************************************************************************************************
//                                               METHODES PRIVEES
//************************************************************************************************************************


    private VehiculeDTO convertirToVehiculeDTO(List<Vehicule> listeVehicules, boolean inclureMotos, boolean inclureVoitures) {
        List<MotoResponseDTO> listeMotos = new ArrayList<>();
        List<VoitureResponseDTO> listeVoitures = new ArrayList<>();
        for (Vehicule v : listeVehicules) {
            if (inclureMotos && v instanceof Moto moto) {
                listeMotos.add(motoMapper.toMotoResponseDTO(moto));
            } else if (inclureVoitures && v instanceof Voiture voiture) {
                listeVoitures.add(voitureMapper.toVoitureResponseDTO(voiture));
            }
        }
        return new VehiculeDTO(listeMotos, listeVoitures);
    }

    private List<Vehicule> rechercherParDate(LocalDate dateDebut, LocalDate dateFin) {
        List<Location> listeLocations = locationDAO.findAll();
        List<Vehicule> listeVehiculeIndispo = listeLocations.stream()
                .filter(location -> location.getDateDebut().isBefore(dateFin) && location.getDateFin().isAfter(dateDebut))
                .map(Location::getVehicule)
                .toList();
        List<Vehicule> listeTousLesVehicules = vehiculeDAO.findAll();
        listeTousLesVehicules.removeAll(listeVehiculeIndispo);
        return listeTousLesVehicules;
    }

    private static List<Vehicule> filtrerParType(Type type, List<Vehicule> listeTousLesVehicules) {
        if (type != null) {
            listeTousLesVehicules = listeTousLesVehicules.stream()
                    .filter(vehicule -> vehicule.getType().equals(type))
                    .toList();
        }
        return listeTousLesVehicules;
    }

    private static List<VoitureResponseDTO> extraireVoituresResponseDto(VehiculeDTO listeVehicule, Predicate<VoitureResponseDTO> voitureResponseDTOPredicate) {
        List<VoitureResponseDTO> listeVoitures = listeVehicule.voitures().stream()
                .filter(voitureResponseDTOPredicate)
                .toList();
        return listeVoitures;
    }

    private static List<MotoResponseDTO> extraireMotosResponseDto(VehiculeDTO listeVehicule, Predicate<MotoResponseDTO> motoResponseDTOPredicate) {
        List<MotoResponseDTO> listeMotos = listeVehicule.motos().stream()
                .filter(motoResponseDTOPredicate)
                .toList();
        return listeMotos;
    }





}
