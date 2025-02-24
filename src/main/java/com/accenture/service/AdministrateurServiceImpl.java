package com.accenture.service;

import com.accenture.exception.AdministrateurException;
import com.accenture.repository.AdministrateurDAO;
import com.accenture.repository.entity.Utilisateurs.Administrateur;
import com.accenture.service.dto.AdministrateurRequestDTO;
import com.accenture.service.dto.AdministrateurResponseDTO;
import com.accenture.service.dto.ClientResponseDTO;
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
     *
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
     *
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
     *
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
     * Récupère les informations du compte d'un client en vérifiant ses identifiants
     *
     * @param login    : adresse mail du client
     * @param password : mot de passe du client
     * @return un {@link ClientResponseDTO} contenant les informations du client si l'authentification réussit
     * @throws EntityNotFoundException Si aucun client correspondant à l'email n'est trouvé ou si le mot de passe est incorrect.
     */

    @Override
    public AdministrateurResponseDTO infosCompte(String login, String password) throws EntityNotFoundException {
        Administrateur admin = verifAdmin(login, password);
        return adminMapper.toAdminResponseDTO(admin);
    }

    /**
     * Le client peut supprimer son compte si les informations sont correctes
     *
     * @param login    : mail du client
     * @param password : mot de passe du client
     * @return un {@link ClientResponseDTO} contenant les informations du client si l'authentification réussit
     * @throws EntityNotFoundException si aucun client correspondant à l'email n'est trouvé ou si le mot de passe est incorrect.
     */

    @Override
    public AdministrateurResponseDTO suppCompte(String login, String password) throws EntityNotFoundException {
        Administrateur admin = verifAdmin(login, password);
        adminDAO.delete(admin);
        return adminMapper.toAdminResponseDTO(admin);
    }

    @Override
    public AdministrateurResponseDTO modifPartielle(String login, String password, AdministrateurRequestDTO adminRequestDTO) {
        Administrateur adminExistant = verifAdmin(login, password);
        Administrateur nouveau = adminMapper.toAdministrateur(adminRequestDTO);
        remplacerExistantParNouveau(adminExistant, nouveau);
        Administrateur adminEnre = adminDAO.save(adminExistant);
        return adminMapper.toAdminResponseDTO(adminEnre);
    }


//**********************************************************************************************************************
//                                                      METHODES PRIVEES
//**********************************************************************************************************************

    /**
     * Vérifie la validité des informations d'un admin avant son enregistrement.
     * Cette méthode s'assure que toutes les informations requises sont bien renseignées
     * et que le client respecte certaines règles (ex : mot de passe sécurisé).
     *
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


    private Administrateur verifAdmin(String login, String password) {
        Optional<Administrateur> optAdmin = adminDAO.findByLogin(login);
        if (optAdmin.isEmpty())
            throw new EntityNotFoundException("Erreur dans l'email ou le mot de passe");
        Administrateur admin = optAdmin.get();
        if (!admin.getPassword().equals(password))
            throw new EntityNotFoundException("Erreur dans l'email ou le mot de passe");
        return admin;
    }

    private static void remplacerExistantParNouveau(Administrateur adminExistant, Administrateur admin) {
        if (admin.getPassword() != null)
            adminExistant.setPassword(admin.getPassword());
        if (admin.getNom() != null)
            adminExistant.setNom(admin.getNom());
        if (admin.getPrenom() != null)
            adminExistant.setPrenom(admin.getPrenom());
        if (admin.getFonction() != null)
            adminExistant.setFonction(admin.getFonction());
        if (admin.getLogin() != null)
            adminExistant.setLogin(admin.getLogin());

    }


}
