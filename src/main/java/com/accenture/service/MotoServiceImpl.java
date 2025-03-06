package com.accenture.service;
import com.accenture.exception.MotoException;
import com.accenture.repository.MotoDAO;
import com.accenture.repository.entity.vehicules.Moto;
import com.accenture.service.dto.vehicules.MotoRequestDTO;
import com.accenture.service.dto.vehicules.MotoResponseDTO;
import com.accenture.service.mapper.MotoMapper;
import com.accenture.shared.enumerations.Filtre;
import com.accenture.shared.enumerations.Permis;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service {@link MotoService} permettant la gestion des motos.
 * Cette classe fournit des fonctionnalités pour ajouter, modifier, supprimer, récupérer et filtrer les motos.
 *
 * Elle interagit avec {@link MotoDAO} pour l'accès aux données et utilise {@link MotoMapper} pour la conversion
 * entre les entités et les DTOs.
 *
 * <p>Les principales fonctionnalités incluent :</p>
 * <ul>
 *     <li>Récupération de toutes les motos</li>
 *     <li>Recherche d'une moto par son identifiant</li>
 *     <li>Ajout, modification et suppression d'une moto</li>
 *     <li>Filtrage des motos selon leur statut</li>
 *     <li>Vérification et validation des données des motos</li>
 * </ul>
 *
 * @see MotoService
 * @see MotoDAO
 * @see MotoMapper
 */


@Service
public class MotoServiceImpl implements MotoService {

    private final MotoDAO motoDAO;
    private final MotoMapper motoMapper;


    public MotoServiceImpl(MotoDAO motoDAO, MotoMapper motoMapper) {
        this.motoDAO = motoDAO;
        this.motoMapper = motoMapper;
    }

    /**
     * Récupère la liste de toutes les motos et les convertit en objets MotoResponseDTO.
     * @return une liste d'objets {@link MotoResponseDTO} représentant toutes les motos.
     */

    @Override
    public List<MotoResponseDTO> trouverTous() {
        return motoDAO.findAll().stream()
                .map(motoMapper::toMotoResponseDTO)
                .toList();
    }

    /**
     * Recherche une moto par son ID et la convertit en objet MotoResponseDTO.
     *
     * @param id l'ID de la moto.
     * @return un objet {@link MotoResponseDTO} représentant la moto trouvée.
     * @throws EntityNotFoundException si aucune moto n'a été trouvée avec cet ID.
     */
    @Override
    public MotoResponseDTO trouver(long id) throws EntityNotFoundException {
        Optional<Moto> optMoto = motoDAO.findById(id);
        if (optMoto.isEmpty())
            throw new EntityNotFoundException("Moto non trouvée");
        Moto moto = optMoto.get();
        return motoMapper.toMotoResponseDTO(moto);
    }


    /**
     * Ajoute une moto et la convertit en un objet MotoResponseDTO en respectant les paramètres fournis.
     *
     * @param motoRequestDTO objet contenant les informations de la moto à enregistrer.
     * @return un objet {@link MotoResponseDTO} représentant la moto ajoutée.
     * @throws MotoException si un des paramètres n'est pas correct.
     */
    @Override
    public MotoResponseDTO ajouter(MotoRequestDTO motoRequestDTO) throws MotoException {
        verifierMoto(motoRequestDTO);
        Moto moto = motoMapper.toMoto(motoRequestDTO);
        attribuerPermisParCylindreeEtPuissance(motoRequestDTO, moto);
        Moto motoEnreg = motoDAO.save(moto);
        return motoMapper.toMotoResponseDTO(motoEnreg);
    }

    /**
     * Supprime une moto par son identifiant si elle existe.
     *
     * @param id identifiant de la moto.
     * @return un objet {@link MotoResponseDTO} contenant les informations de la moto supprimée.
     * @throws EntityNotFoundException si aucune moto ne correspond à l'identifiant fourni.
     */
    @Override
    public MotoResponseDTO supprimer(long id) throws EntityNotFoundException {
        Moto moto = motoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID inconnu"));
        motoDAO.deleteById(id);
        return motoMapper.toMotoResponseDTO(moto);
    }



    /**
     * Modifie une moto à partir de son identifiant.
     *
     * @param id identifiant de la moto à modifier.
     * @param motoRequestDTO objet contenant les nouvelles informations à enregistrer.
     * @return un objet {@link MotoResponseDTO} contenant les informations de la moto modifiée.
     * @throws EntityNotFoundException si aucune moto ne correspond à l'identifiant fourni.
     * @throws MotoException si un des paramètres n'est pas correct ou si la moto a été retirée du parc.
     */

    @Override
    public MotoResponseDTO modifier(long id, MotoRequestDTO motoRequestDTO) throws EntityNotFoundException, MotoException {
        Moto motoExistant = trouverMotoParID(id);
        Moto nouveau = motoMapper.toMoto(motoRequestDTO);
        if (motoExistant.getRetireDuParc())
            throw new MotoException("Une voiture retirée du parc ne peut pas être modifier");
        remplacerExistantParNouveau(motoExistant, nouveau);
        MotoRequestDTO dto = motoMapper.toMotoRequestDTO(motoExistant);
        Moto motoEnr = motoDAO.save(motoExistant);
        return motoMapper.toMotoResponseDTO(motoEnr);
    }

    /**
     * Filtre les motos en fonction de leur statut : actif, inactif, dans le parc ou hors du parc.
     *
     * @param filtre une valeur de l'énumération {@link Filtre} définissant le critère de filtrage.
     * @return une liste de {@link MotoResponseDTO} correspondant aux motos filtrées selon le statut fourni.
     * @throws IllegalArgumentException si le filtre spécifié n'est pas reconnu.
     */
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
            default -> throw new IllegalArgumentException(STR."Le filtre n'est pas disponible : \{filtre}");
        };
    }



//************************************************************************************************************************
//                                                      METHODES PRIVEE
//************************************************************************************************************************

    private Moto trouverMotoParID(long id) {
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
        if (motoRequestDTO.cylindree() <= 50)
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


    private void attribuerPermisParCylindreeEtPuissance(MotoRequestDTO motoRequestDTO, Moto moto) {
        if ((motoRequestDTO.cylindree() >= 0 && motoRequestDTO.cylindree()<=125) && (motoRequestDTO.puissance() >=1 && motoRequestDTO.puissance() <= 11))
            moto.setPermis(Permis.A1);
        else if ((motoRequestDTO.cylindree() > 125  &&  motoRequestDTO.puissance() <= 35))
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
