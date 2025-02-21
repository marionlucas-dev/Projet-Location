package com.accenture.service;

import com.accenture.exception.AdministrateurException;
import com.accenture.repository.AdministrateurDAO;
import com.accenture.repository.entity.Utilisateurs.Administrateur;
import com.accenture.service.dto.AdministrateurRequestDTO;
import com.accenture.service.dto.AdministrateurResponseDTO;
import com.accenture.service.mapper.AdministrateurMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des administrateurs.
 * Cette classe fournit les fonctionnalités suivantes :
 * Récupérer liste de tous les administrateurs / d'un administrateur par son identifiant / ajout et vérification des infos d'un client/
 * supprimer un administrateur s'il n'a pas de location en cours.
 */

@Service
public class AdministrateurServiceImpl implements AdministrateurService {

    public static final String ID_NON_PRESENT = "ID non présent";
    private final AdministrateurDAO adminDAO;
    private final AdministrateurMapper adminMapper;


    public AdministrateurServiceImpl(AdministrateurDAO administrateurDAO, AdministrateurMapper adminMapper) {
        this.adminDAO = administrateurDAO;
        this.adminMapper = adminMapper;
    }

    /**
     * Recupère la liste de tous les admins et les convertit en object AdminRespondeDTO
     * @return une liste d'objet (@link AdminResponseDTO) représentant tous les clients
     */

    @Override
    public List<AdministrateurResponseDTO> trouverTous() {
        return adminDAO.findAll().stream()
                .map(adminMapper::toAdminResponseDTO)
                .toList();
    }

    /**
     * Rechercher un admin par son Id et le convertit en un objet AdminResponseDTO
     * @param id est l'identifiant du client à rechercher
     * @return un objet (@Link AdminResponseDTO) représentant le client trouvé.
     * @throws EntityNotFoundException si aucun admin n'a été trouver avec cet ID
     */

    @Override
    public AdministrateurResponseDTO trouver(long id) throws EntityNotFoundException {
        Optional<Administrateur> optAdmin = adminDAO.findById(id);
        if (optAdmin.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Administrateur admin = optAdmin.get();
        return adminMapper.toAdminResponseDTO(admin);
    }

    /**
     * Ajoute un admin et le convertit en un objet AdminResponseDTO
     * @param adminRequestDTO : objet contenant les informations de l'admin à enregistrer.
     * @return un objet (@link AdministrateurResponseDTO) représentant l'admin ajouté
     */

    @Override
    public AdministrateurResponseDTO ajouter(AdministrateurRequestDTO adminRequestDTO) {
        verifierAdmin(adminRequestDTO);
        Administrateur admin = adminMapper.toAdministrateur(adminRequestDTO);
        Administrateur adminEnreg = adminDAO.save(admin);
        return adminMapper.toAdminResponseDTO(adminEnreg);
    }

    /**
     *  Supprime un admin en fonction de son identifiant
     * @param id est l'identifiant de l'admin à rechercher
     * @throws EntityNotFoundException si aucun admin correspondant à l'ID fourni n'est trouvé
     */
    @Override
    public void supprimer(long id) throws EntityNotFoundException{
        if (adminDAO.existsById(id))
            adminDAO.deleteById(id);
        else throw new EntityNotFoundException(ID_NON_PRESENT);
    }

//**********************************************************************************************************************
//                                                      METHODES PRIVEES
//**********************************************************************************************************************

    /**
     * Vérifie la validité des informations d'un admin avant son enregistrement.
     *  Cette méthode s'assure que toutes les informations requises sont bien renseignées
     *  et que le client respecte certaines règles (ex : mot de passe sécurisé).
     * @param adminRequestDTO Les informations de l'admin à valider
     */

    private static void verifierAdmin(AdministrateurRequestDTO adminRequestDTO) {
        if (adminRequestDTO == null)
            throw new AdministrateurException("L'administrateur est null");
        if (adminRequestDTO.nom() == null || adminRequestDTO.nom().isBlank())
            throw new AdministrateurException("Le nom est obligatoire ");
        if (adminRequestDTO.prenom() == null || adminRequestDTO.prenom().isBlank())
            throw new AdministrateurException("Le prénom est obligatoire");
        if (adminRequestDTO.email() == null || adminRequestDTO.email().isBlank())
            throw new AdministrateurException("L'email est obligatoire");
        if (adminRequestDTO.password() == null || adminRequestDTO.password().isBlank())
            throw new AdministrateurException("Le mot de passe est obligatoire");
        if (adminRequestDTO.fonction() == null || adminRequestDTO.fonction().isBlank())
            throw new AdministrateurException("La fonction est obligatoire");
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[&#@_§-]).{8,16}$";
        if (!adminRequestDTO.password().matches(passwordRegex))
            throw new AdministrateurException("Le mot de passe doit contenir entre 8 et 16 caractères, au minimum une minuscule et 1 majuscule" +
                    "un chiffre, et un caractère spécial");
        String emailRegex = "^[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!adminRequestDTO.email().matches(emailRegex)) {
            throw new AdministrateurException("L'email doit contenir un @ et un nom de domaine valide");
        }
    }


}
