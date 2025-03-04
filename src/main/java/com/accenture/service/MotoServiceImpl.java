package com.accenture.service;

import com.accenture.exception.MotoException;
import com.accenture.repository.MotoDAO;
import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.service.mapper.MotoMapper;
import com.accenture.shared.Filtre;
import com.accenture.shared.Permis;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoServiceImpl implements MotoService {

    private final MotoDAO motoDAO;
    private final MotoMapper motoMapper;


    public MotoServiceImpl(MotoDAO motoDAO, MotoMapper motoMapper) {
        this.motoDAO = motoDAO;
        this.motoMapper = motoMapper;
    }

    /**
     * Récupère la liste de toutes les motos et les convertis en objet MotoResponseDTO
     *
     * @return une liste d'objet (@link MotoResponseDTO) représentant toutes les motos
     */

    @Override
    public List<MotoResponseDTO> trouverTous() {
        return motoDAO.findAll().stream()
                .map(motoMapper::toMotoResponseDTO)
                .toList();
    }

    /**
     * Rechercher une moto par son modèle et le convertit en objet MotoResponseDTO
     *
     * @param modele : le modèle de la moto
     * @return un objet (@link MotoResponseDTO) représentant la moto trouvé
     * @throws EntityNotFoundException : si aucune moto n'a été trouvée avec cet ID
     */

    @Override
    public MotoResponseDTO trouver(String modele) throws EntityNotFoundException {
        Optional<Moto> optMoto = motoDAO.findByModele(modele);
        if (optMoto.isEmpty())
            throw new EntityNotFoundException("Voiture non trouvé");
        Moto moto = optMoto.get();
        return motoMapper.toMotoResponseDTO(moto);
    }

    /**
     * Ajoute une moto et le converti en un objet MotoResponseDTO en respectant les paramètres
     *
     * @param motoRequestDTO : objet contenant les informations de la moto à enregistrer
     * @return : un objet (@link MotoResponseDTO) représentant la moto ajouté
     * @throws MotoException : si un des paramètres n'es pas correct.
     */

    @Override
    public MotoResponseDTO ajouter(MotoRequestDTO motoRequestDTO) throws MotoException {
        verifierMoto(motoRequestDTO);
        Moto moto = motoMapper.toMoto(motoRequestDTO);
        PermisParCylindreeEtPuissance(motoRequestDTO, moto);
        Moto motoEnreg = motoDAO.save(moto);
        return motoMapper.toMotoResponseDTO(motoEnreg);
    }


    /**
     * Une moto peut être supprimée si les informations sont correctes
     *
     * @param id : identifiant de la moto
     * @return un {@Link MotoResponseDTO} contenannt les infos de la voiture si l'authentification réussit.
     * @throws EntityNotFoundException: si aucune voiture ne correspond aux informations demandées.
     */

    @Override
    public MotoResponseDTO supprimer(long id) throws EntityNotFoundException {
        Moto moto = motoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID inconnu"));
        motoDAO.deleteById(id);
        return motoMapper.toMotoResponseDTO(moto);
    }

    @Override
    public MotoResponseDTO modifier(long id, MotoRequestDTO motoRequestDTO) throws EntityNotFoundException, MotoException {
        Moto motoExistant = verifMoto(id);
        Moto nouveau = motoMapper.toMoto(motoRequestDTO);
        if (motoExistant.getRetireDuParc())
            throw new MotoException("Une voiture retirée du parc ne peut pas être modifier");
        remplacerExistantParNouveau(motoExistant, nouveau);
        MotoRequestDTO dto = motoMapper.toMotoRequestDTO(motoExistant);
        verifierMoto(dto);
        Moto motoEnr = motoDAO.save(motoExistant);
        return motoMapper.toMotoResponseDTO(motoEnr);
    }

    @Override
    public List<MotoResponseDTO> filtrer(Filtre filtre) {
        List<Moto> listeMoto = motoDAO.findAll();
        return switch (filtre) {
            case ACTIF -> listeMoto.stream()
                    .filter(Moto::getActif)
                    .map(motoMapper::toMotoResponseDTO)
                    .toList();
            case INACTIF -> listeMoto.stream()
                    .filter(moto -> !moto.getActif())
                    .map(motoMapper::toMotoResponseDTO)
                    .toList();
            case HORSPARC -> listeMoto.stream()
                    .filter(Moto::getRetireDuParc)
                    .map(motoMapper::toMotoResponseDTO)
                    .toList();
            case DANSLEPARC -> listeMoto.stream()
                    .filter(moto -> !moto.getRetireDuParc())
                    .map(motoMapper::toMotoResponseDTO)
                    .toList();
            default -> throw new IllegalArgumentException(STR."Le filtre n'est pas disponible\{filtre}");
        };
    }


//************************************************************************************************************************
//                                                      METHODES PRIVEE
//************************************************************************************************************************

    private Moto verifMoto(long id) {
        Optional<Moto> optMoto = motoDAO.findById(id);
        if (optMoto.isEmpty())
            throw new EntityNotFoundException("ID incorrect");
        Moto moto = optMoto.get();

        return moto;
    }


    private static void verifierMoto(MotoRequestDTO motoRequestDTO) {
        if (motoRequestDTO == null)
            throw new MotoException("La moto est null");
        if (motoRequestDTO.marque() == null || motoRequestDTO.marque().isBlank())
            throw new MotoException("La marque est obligatoire");
        if (motoRequestDTO.modele() == null || motoRequestDTO.modele().isBlank())
            throw new MotoException("Le modèle est obligatoire");
        if (motoRequestDTO.couleur() == null || motoRequestDTO.couleur().isBlank())
            throw new MotoException("La couleur est obligatoire");
        if (motoRequestDTO.nbrCylindres() <= 0 || motoRequestDTO.nbrCylindres() > 7)
            throw new MotoException("Le nombre de cylindres doit être compris entre 1 et 6 ");
        if (motoRequestDTO.cylindree() <= 0)
            throw new MotoException("Les cylindrées sont obligatoires. Ex: 50/125/250/300 etc");
        if (motoRequestDTO.poids() <= 0)
            throw new MotoException("Le poids de la moto est obligatoire");
        if (motoRequestDTO.puissance() <= 0)
            throw new MotoException("La puissance ne peut pas être inférieure à 0kW");
        if (motoRequestDTO.hauteurSelle() <= 0)
            throw new MotoException("La hauteur de selle est obligatoire");
        if (motoRequestDTO.transmission() == null)
            throw new MotoException("La transmission (auto ou manuelle) est obligatoire");
        if (motoRequestDTO.type() == null)
            throw new MotoException("Le type est obligatoire");

    }


    private static void PermisParCylindreeEtPuissance(MotoRequestDTO motoRequestDTO, Moto moto) {
        if (motoRequestDTO.cylindree() <= 125 && motoRequestDTO.puissance() <= 11)
            moto.setPermis(Permis.A1);
        else if (motoRequestDTO.puissance() <= 35)
            moto.setPermis(Permis.A2);
        else {
            moto.setPermis(Permis.A);
        }
    }


    private static void remplacerExistantParNouveau(Moto motoExistante, Moto moto) {
        if (moto.getMarque() != null)
            motoExistante.setMarque(moto.getMarque());
        if (moto.getModele() != null)
            motoExistante.setModele(moto.getModele());
        if (moto.getCouleur() != null)
            motoExistante.setCouleur(moto.getCouleur());
        if (moto.getNbrCylindres() >= 0)
            motoExistante.setNbrCylindres(moto.getNbrCylindres());
        if (moto.getCylindree() >= 0)
            motoExistante.setCylindree(moto.getCylindree());
        if (moto.getPoids() >= 0)
            motoExistante.setPoids(moto.getPoids());
        if (moto.getPuissance() >= 0)
            motoExistante.setPuissance(moto.getPuissance());
        if (moto.getHauteurSelle() >= 0)
            motoExistante.setHauteurSelle(moto.getHauteurSelle());
        if (moto.getTransmission() != null)
            motoExistante.setTransmission(moto.getTransmission());
        if (moto.getType() != null)
            motoExistante.setType(moto.getType());
    }


}
