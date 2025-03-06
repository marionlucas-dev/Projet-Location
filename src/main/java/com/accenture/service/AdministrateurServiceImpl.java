package com.accenture.service;
import com.accenture.exception.AdministrateurException;
import com.accenture.repository.AdministrateurDAO;
import com.accenture.repository.entity.utilisateurs.Administrateur;
import com.accenture.service.dto.utilisateurs.AdministrateurRequestDTO;
import com.accenture.service.dto.utilisateurs.AdministrateurResponseDTO;
import com.accenture.service.mapper.AdministrateurMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des administrateurs.
 * Cette classe fournit les fonctionnalités suivantes :
 * - Récupérer la liste de tous les administrateurs.
 * - Récupérer un administrateur par son identifiant.
 * - Ajouter un administrateur et vérifier les informations d'un administrateur.
 * - Supprimer un administrateur si ce dernier n'a pas de location en cours.
 */


@Slf4j
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
     * Récupère la liste de tous les administrateurs et les convertit en objets {@link AdministrateurResponseDTO}.
     *
     * @return une liste d'objets {@link AdministrateurResponseDTO} représentant tous les administrateurs.
     */


    @Override
    public List<AdministrateurResponseDTO> trouverTous() {
        return adminDAO.findAll().stream()
                .map(adminMapper::toAdminResponseDTO)
                .toList();
    }

    /**
     * Recherche un administrateur par son identifiant et le convertit en un objet {@link AdministrateurResponseDTO}.
     *
     * @param id l'identifiant de l'administrateur à rechercher.
     * @return un objet {@link AdministrateurResponseDTO} représentant l'administrateur trouvé.
     * @throws EntityNotFoundException si aucun administrateur n'a été trouvé avec cet identifiant.
     */


    @Override
    public AdministrateurResponseDTO trouver(long id) throws EntityNotFoundException {
        Optional<Administrateur> optAdmin = adminDAO.findById(id);
        if (optAdmin.isEmpty()) {
            log.error(STR."trouver\{ID_NON_PRESENT}");
            throw new EntityNotFoundException(ID_NON_PRESENT);
        }
        Administrateur admin = optAdmin.get();
        return adminMapper.toAdminResponseDTO(admin);
    }

    /**
     * Ajoute un administrateur et le convertit en un objet {@link AdministrateurResponseDTO}.
     *
     * @param adminRequestDTO l'objet contenant les informations de l'administrateur à enregistrer.
     * @return un objet {@link AdministrateurResponseDTO} représentant l'administrateur ajouté.
     */


    @Override
    public AdministrateurResponseDTO ajouter(AdministrateurRequestDTO adminRequestDTO) {
        verifierAdmin(adminRequestDTO);
        Administrateur admin = adminMapper.toAdministrateur(adminRequestDTO);
        Administrateur adminEnreg = adminDAO.save(admin);
        return adminMapper.toAdminResponseDTO(adminEnreg);
    }


    /**
     * Récupère les informations du compte d'un administrateur en vérifiant ses identifiants.
     *
     * @param login    l'adresse mail de l'administrateur.
     * @param password le mot de passe de l'administrateur.
     * @return un {@link AdministrateurResponseDTO} contenant les informations de l'administrateur si l'authentification réussit.
     * @throws EntityNotFoundException si aucun administrateur correspondant à l'email n'est trouvé ou si le mot de passe est incorrect.
     */


    @Override
    public AdministrateurResponseDTO recupererinfosCompte(String login, String password) throws EntityNotFoundException {
        Administrateur admin = verifAdmin(login, password);
        return adminMapper.toAdminResponseDTO(admin);
    }

    /**
     * L'administrateur peut supprimer son compte si les informations sont correctes.
     *
     * @param login    l'adresse mail de l'administrateur.
     * @param password le mot de passe de l'administrateur.
     * @return un {@link AdministrateurResponseDTO} contenant les informations de l'administrateur si l'authentification réussit.
     * @throws EntityNotFoundException si aucun administrateur correspondant à l'email n'est trouvé ou si le mot de passe est incorrect.
     */


    @Override
    public AdministrateurResponseDTO supprimer(String login, String password) throws EntityNotFoundException {
        Administrateur admin = verifAdmin(login, password);
        adminDAO.delete(admin);
        return adminMapper.toAdminResponseDTO(admin);
    }

    /**
     * Modifie partiellement les informations d'un administrateur en mettant à jour uniquement les champs non nuls fournis
     * dans {@code adminRequestDTO}.
     *
     * @param login           Le login de l'administrateur à modifier.
     * @param password        Le mot de passe de l'administrateur à modifier.
     * @param adminRequestDTO L'objet contenant les nouvelles valeurs des champs à mettre à jour.
     * @return Un {@code AdministrateurResponseDTO} contenant les informations mises à jour de l'administrateur.
     * @throws EntityNotFoundException Si le login ou le mot de passe est incorrect.
     * @throws AdministrateurException Si une erreur spécifique liée à l'administrateur survient.
     */

    @Override
    public AdministrateurResponseDTO modifPartielle(String login, String password, AdministrateurRequestDTO adminRequestDTO) {
        Administrateur adminExistant = verifAdmin(login, password);
        Administrateur nouveau = adminMapper.toAdministrateur(adminRequestDTO);
        remplacerExistantParNouveau(adminExistant, nouveau);
        AdministrateurRequestDTO dto = adminMapper.toAdminRequestDTO(adminExistant);
        verifierAdmin(dto);
        Administrateur adminEnre = adminDAO.save(adminExistant);
        return adminMapper.toAdminResponseDTO(adminEnre);
    }


//**********************************************************************************************************************
//                                                      METHODES PRIVEES
//**********************************************************************************************************************



    private static void verifierAdmin(AdministrateurRequestDTO adminRequestDTO) {
        if (adminRequestDTO == null) {
            log.error("verifier admin" + "admin est null");
            throw new AdministrateurException("L'administrateur est null");
        }
        if (adminRequestDTO.nom() == null || adminRequestDTO.nom().isBlank()) {
            log.error("verifier admin" + "le nom est obligatoire");
            throw new AdministrateurException("Le nom est obligatoire ");
        }
        if (adminRequestDTO.prenom() == null || adminRequestDTO.prenom().isBlank()) {
            log.error("verifier admin" + "le prénom est obligatoire");
            throw new AdministrateurException("Le prénom est obligatoire");
        }
        if (adminRequestDTO.email() == null || adminRequestDTO.email().isBlank()) {
            log.error("verifier admin" + "l'email est obligatoire");
            throw new AdministrateurException("L'email est obligatoire");
        }
        if (adminRequestDTO.password() == null || adminRequestDTO.password().isBlank()) {
            log.error("verifier admin" + "le mot de passe est obligatoire");
            throw new AdministrateurException("Le mot de passe est obligatoire");
        }
        if (adminRequestDTO.fonction() == null || adminRequestDTO.fonction().isBlank()) {
            log.error("verifier admin" + "la fonction est obligatoire");
            throw new AdministrateurException("La fonction est obligatoire");

        }
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[&#@_§-]).{8,16}$";
        if (!adminRequestDTO.password().matches(passwordRegex)) {
            log.error("verifier admin" + "le mot de passe doit respecter le Regex ");
            throw new AdministrateurException("Le mot de passe doit contenir entre 8 et 16 caractères, au minimum une minuscule et 1 majuscule" +
                    "un chiffre, et un caractère spécial");
        }
        String emailRegex = "^[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!adminRequestDTO.email().matches(emailRegex)) {
            log.error("verifier admin" + "l'email doit respecter le Regex");
            throw new AdministrateurException("L'email doit contenir un @ et un nom de domaine valide");
        }
    }


    private Administrateur verifAdmin(String login, String password) {
        Optional<Administrateur> optAdmin = adminDAO.findByLogin(login);
        if (optAdmin.isEmpty()) {
            log.error("verifadmin" + "Erreur dans l'email ou le mot de passe ");
            throw new EntityNotFoundException("Erreur dans l'email ou le mot de passe");
        }
        Administrateur admin = optAdmin.get();
        if (!admin.getPassword().equals(password)) {
            log.error("verifadmin" + "Erreur dans l'email ou le mot de passe ");
            throw new EntityNotFoundException("Erreur dans l'email ou le mot de passe");
        }
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
