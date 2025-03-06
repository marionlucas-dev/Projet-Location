package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.repository.VoitureDAO;
import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.enumerations.Filtre;
import com.accenture.shared.enumerations.NombrePortes;
import com.accenture.shared.enumerations.Permis;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des voitures.
 * Cette classe fournit des méthodes permettant d'effectuer diverses opérations sur les voitures,
 * telles que l'ajout, la modification, la suppression, la récupération et le filtrage des véhicules.
 * Elle utilise un DAO pour l'accès aux données et un mapper pour la conversion des entités en DTOs.
 */

@Service
public class VoitureServiceImpl implements VoitureService {

    private final VoitureDAO voitureDAO;
    private final VoitureMapper voitureMapper;


    public VoitureServiceImpl(VoitureDAO voitureDAO, VoitureMapper voitureMapper) {
        this.voitureDAO = voitureDAO;
        this.voitureMapper = voitureMapper;
    }

    /**
     * Récupère la liste de toutes les voitures et les convertit en objets {@link VoitureResponseDTO}.
     *
     * @return une liste d'objets {@link VoitureResponseDTO} représentant toutes les voitures.
     */


    @Override
    public List<VoitureResponseDTO> trouverTous() {
        return voitureDAO.findAll().stream()
                .map(voitureMapper::toVoitureResponseDTO)
                .toList();
    }


    /**
     * Recherche une voiture par son identifiant et la convertit en objet {@link VoitureResponseDTO}.
     *
     * @param id l'identifiant de la voiture.
     * @return un objet {@link VoitureResponseDTO} représentant la voiture trouvée.
     * @throws EntityNotFoundException si aucune voiture n'a été trouvée avec cet identifiant.
     */


    @Override
    public VoitureResponseDTO trouver(long id) throws EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDAO.findById(id);
        if (optVoiture.isEmpty())
            throw new EntityNotFoundException("Voiture non trouvé");
        Voiture voiture = optVoiture.get();
        return voitureMapper.toVoitureResponseDTO(voiture);
    }


    /**
     * Ajoute une voiture et la convertit en un objet {@link VoitureResponseDTO} en respectant les paramètres fournis.
     *
     * @param voitureRequestDTO objet contenant les informations de la voiture à enregistrer.
     * @return un objet {@link VoitureResponseDTO} représentant la voiture ajoutée.
     * @throws VoitureException si l'un des paramètres n'est pas correct.
     */


    @Override
    public VoitureResponseDTO ajouter(VoitureRequestDTO voitureRequestDTO) throws VoitureException {
        verifierVoiture(voitureRequestDTO);
        Voiture voiture = voitureMapper.toVoiture(voitureRequestDTO);
        assignerPermisParNbrPlaces(voitureRequestDTO, voiture);
        Voiture voitureEnreg = voitureDAO.save(voiture);
        return voitureMapper.toVoitureResponseDTO(voitureEnreg);
    }

    /**
     * Supprime une voiture si elle est trouvée en base de données.
     *
     * @param id l'identifiant de la voiture à supprimer.
     * @return un objet {@link VoitureResponseDTO} contenant les informations de la voiture supprimée.
     * @throws EntityNotFoundException si aucune voiture ne correspond à l'identifiant fourni.
     */


    @Override
    public VoitureResponseDTO supprimer(long id) throws EntityNotFoundException {
        Voiture voiture = voitureDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID inconnu"));
        voitureDAO.deleteById(id);
        return voitureMapper.toVoitureResponseDTO(voiture);
    }


    /**
     * Modifie complètement ou partiellement une voiture.
     *
     * @param id l'identifiant de la voiture à modifier
     * @param voitureRequestDTO les nouvelles données de la voiture à mettre à jour
     * @return un objet {@link VoitureResponseDTO} représentant la voiture mise à jour
     * @throws EntityNotFoundException si la voiture avec l'identifiant donné n'existe pas
     * @throws VoitureException si la voiture est retirée du parc et ne peut être modifiée
     */


    @Override
    public VoitureResponseDTO modifier(long id, VoitureRequestDTO voitureRequestDTO) throws EntityNotFoundException, VoitureException {
        Voiture voitureExistant = trouverVoitureParID(id);
        Voiture nouveau = voitureMapper.toVoiture(voitureRequestDTO);
        if (voitureExistant.getRetireDuParc())
            throw new VoitureException("Une voiture retirée du parc ne peut pas être modifier");
        remplacerExistantParNouveau(voitureExistant, nouveau);
        VoitureRequestDTO dto = voitureMapper.toVoitureRequestDTO(voitureExistant);
        Voiture voitureEnr = voitureDAO.save(voitureExistant);
        return voitureMapper.toVoitureResponseDTO(voitureEnr);
    }


    /**
     * Filtre les voitures en fonction de leur statut : actif, inactif, dans le parc ou hors du parc.
     *
     * @param filtre une valeur de l'énumération {@link Filtre} définissant le critère de filtrage
     * @return une liste de {@link VoitureResponseDTO} contenant les voitures correspondant au filtre appliqué
     * @throws IllegalArgumentException si le filtre spécifié n'est pas reconnu
     */

    @Override
    public List<VoitureResponseDTO> filtrer(Filtre filtre){
    List<Voiture> listeVoitures = voitureDAO.findAll();
    List<VoitureResponseDTO> result =
            switch (filtre){
                case ACTIF ->
                    listeVoitures.stream()
                            .filter(Voiture::getActif)
                            .map(voitureMapper::toVoitureResponseDTO)
                            .toList();
                case INACTIF ->
                    listeVoitures.stream()
                            .filter(voiture -> !voiture.getActif() )
                            .map(voitureMapper::toVoitureResponseDTO)
                            .toList();
                case HORSPARC ->
                        listeVoitures.stream()
                                .filter(Voiture::getRetireDuParc)
                                .map(voitureMapper::toVoitureResponseDTO)
                                .toList();
                case DANSLEPARC ->
                        listeVoitures.stream()
                                .filter(voiture -> !voiture.getRetireDuParc())
                                .map(voitureMapper::toVoitureResponseDTO)
                                .toList();
                default ->
                        throw new IllegalArgumentException(STR."Le filtre n'est pas disponible\{filtre}");
            };
        return result;
    }




//************************************************************************************************************************
//                                                      METHODES PRIVEE
//************************************************************************************************************************

    private Voiture trouverVoitureParID(long id) {
        Optional<Voiture> optVoiture = voitureDAO.findById(id);
        if (optVoiture.isEmpty())
            throw new EntityNotFoundException("ID incorrect");
        Voiture voiture = optVoiture.get();

        return voiture;
    }

    private static void remplacerExistantParNouveau(Voiture voitureExistant, Voiture voiture) {
        if (voiture.getMarque() != null)
            voitureExistant.setMarque(voiture.getMarque());
        if (voiture.getModele() != null)
            voitureExistant.setModele(voiture.getModele());
        if (voiture.getCouleur() != null)
            voitureExistant.setCouleur(voiture.getCouleur());
        if (voiture.getNbrPlaces() < 1)
            voitureExistant.setNbrPlaces(voiture.getNbrPlaces());
        if (voiture.getCarburant() != null)
            voitureExistant.setCarburant(voiture.getCarburant());
        if (voiture.getNbrPortes() != null)
            voitureExistant.setNbrPortes(voiture.getNbrPortes());
        if (voiture.getTransmission() != null)
            voitureExistant.setTransmission(voiture.getTransmission());
        if (voiture.getClim() != null)
            voitureExistant.setClim(voiture.getClim());
        if (voiture.getBagages() <= 0)
            voitureExistant.setBagages(voiture.getBagages());
        if (voiture.getType() != null)
            voitureExistant.setType(voiture.getType());
        if (voiture.getTransmission() != null)
            voitureExistant.setTransmission(voiture.getTransmission());
    }


    private static void verifierVoiture(VoitureRequestDTO voitureRequestDTO) {
        if (voitureRequestDTO == null)
            throw new VoitureException("La voiture est null");
        if (voitureRequestDTO.marque() == null || voitureRequestDTO.marque().isBlank())
            throw new VoitureException("La marque est obligatoire");
        if (voitureRequestDTO.modele() == null || voitureRequestDTO.modele().isBlank())
            throw new VoitureException("Le modèle est obligatoire");
        if (voitureRequestDTO.couleur() == null || voitureRequestDTO.couleur().isBlank())
            throw new VoitureException("La couleur du véhicule est obligatoire");
        if (voitureRequestDTO.nbrPortes() != NombrePortes.TROIS && voitureRequestDTO.nbrPortes() != NombrePortes.CINQ)
            throw new VoitureException("Le nombre de portes (3 ou 5) est obligatoire");
        if (voitureRequestDTO.carburant() == null)
            throw new VoitureException("Le carburant est obligatoire");
        if (voitureRequestDTO.transmission() == null)
            throw new VoitureException("Le type de transmission est obligatoire");
        if (voitureRequestDTO.clim() == null)
            throw new VoitureException("La présence ou non de clim est obligatoire");
        if (voitureRequestDTO.bagages() <= 0)
            throw new VoitureException("Le modèle est obligatoire");
        if (voitureRequestDTO.type() == null)
            throw new VoitureException("Le type de voiture  est obligatoire");
        if (voitureRequestDTO.nbrPlaces() <= 0 || voitureRequestDTO.nbrPlaces() >= 17)
            throw new VoitureException("Le nombre de passager doit être cohérent");

    }


    private static void assignerPermisParNbrPlaces(VoitureRequestDTO voitureRequestDTO, Voiture voiture) {
        if (voitureRequestDTO.nbrPlaces() > 0 && voitureRequestDTO.nbrPlaces() <10) {
            voiture.setPermis(Permis.B);
        } else {
            voiture.setPermis(Permis.D1);
        }
    }



}
