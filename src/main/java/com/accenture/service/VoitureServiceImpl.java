package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.repository.VoitureDAO;
import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.Filtre;
import com.accenture.shared.NombrePortes;
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

    /**
     * Récupère la liste de toutes les voitures et les converties en objet VoitureResponseDTO
     *
     * @return une liste d'objet (@link VoitureResponseDTO) représentant toutes les voitures
     */

    @Override
    public List<VoitureResponseDTO> trouverTous() {
        return voitureDAO.findAll().stream()
                .map(voitureMapper::toVoitureResponseDTO)
                .toList();
    }


    /**
     * Rechercher une voiture par son modèle et le convertit en objet VoitureResponseDTO
     *
     * @param modele : le modèle de la voiture
     * @return un objet (@link VoitureResponseDTO) représentant la voiture trouvé
     * @throws EntityNotFoundException: si aucune voiture n'a été trouvée avec cet ID
     */

    @Override
    public VoitureResponseDTO trouver(String modele) throws EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDAO.findByModele(modele);
        if (optVoiture.isEmpty())
            throw new EntityNotFoundException("Voiture non trouvé");
        Voiture voiture = optVoiture.get();
        return voitureMapper.toVoitureResponseDTO(voiture);
    }


    /**
     * Ajoute une voiture et le converti en un objet VoitureResponseDTO en respectant les paramètres
     *
     * @param voitureRequestDTO : objet contenant les informations de la voiture à enregistrer
     * @return : un objet (@link VoitureResponseDTO) représentant la moto ajouté
     * @throws VoitureException : si un des paramètres n'es pas correct.
     */

    @Override
    public VoitureResponseDTO ajouter(VoitureRequestDTO voitureRequestDTO) throws VoitureException {
        verifierVoiture(voitureRequestDTO);
        Voiture voiture = voitureMapper.toVoiture(voitureRequestDTO);
        PermisParNbrPlaces(voitureRequestDTO, voiture);
        Voiture voitureEnreg = voitureDAO.save(voiture);
        return voitureMapper.toVoitureResponseDTO(voitureEnreg);
    }

    /**
     * Une voiture peut être supprimée si les informations sont correctes
     *
     * @param id : identifiant de la voiture
     * @return un {@Link VoitureResponseDTO} contenannt les infos de la voiture si l'authentification réussit.
     * @throws EntityNotFoundException: si aucune voiture ne correspond aux informations demandées.
     */

    @Override
    public VoitureResponseDTO supprimer(long id) throws EntityNotFoundException {
        Voiture voiture = voitureDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID inconnu"));
        voitureDAO.deleteById(id);
        return voitureMapper.toVoitureResponseDTO(voiture);
    }

    @Override
    public VoitureResponseDTO modifier(long id, VoitureRequestDTO voitureRequestDTO) throws EntityNotFoundException, VoitureException {
        Voiture voitureExistant = verifVoiture(id);
        Voiture nouveau = voitureMapper.toVoiture(voitureRequestDTO);
        if (voitureExistant.getRetireDuParc())
            throw new VoitureException("Une voiture retirée du parc ne peut pas être modifier");
        remplacerExistantParNouveau(voitureExistant, nouveau);
        VoitureRequestDTO dto = voitureMapper.toVoitureRequestDTO(voitureExistant);
        verifierVoiture(dto);
        Voiture voitureEnr = voitureDAO.save(voitureExistant);
        return voitureMapper.toVoitureResponseDTO(voitureEnr);
    }

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

    private Voiture verifVoiture(long id) {
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
        if (voitureRequestDTO.nbrPortes() != NombrePortes.Trois && voitureRequestDTO.nbrPortes() != NombrePortes.Cinq)
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


    private static void PermisParNbrPlaces(VoitureRequestDTO voitureRequestDTO, Voiture voiture) {
        if (voitureRequestDTO.nbrPlaces() > 0 && voitureRequestDTO.nbrPlaces() <= 9) {
            voiture.setPermis(Permis.B);
        } else {
            voiture.setPermis(Permis.D1);
        }
    }

//    private Voiture verifVoiture(String marque, String modele, int id) {
//        Optional<Voiture> optVoiture = voitureDAO.findByModele(modele);
//        if (optVoiture.isEmpty())
//            throw new EntityNotFoundException("Erreur dans le modèle de la voiture");
//        Voiture voiture = optVoiture.get();
//        if (!voiture.getMarque().equals(marque))
//            throw new EntityNotFoundException("Erreur dans la marque de la voiture");
//        if (!(voiture.getId() == id))
//            throw new EntityNotFoundException("Erreur dans l'id de la voiture");
//        return voiture;
//    }


}
