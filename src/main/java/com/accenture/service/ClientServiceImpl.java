package com.accenture.service;

import com.accenture.exception.AdministrateurException;
import com.accenture.exception.ClientException;
import com.accenture.repository.ClientDAO;
import com.accenture.repository.entity.Utilisateurs.Client;
import com.accenture.service.dto.Utilisateurs.ClientRequestDTO;
import com.accenture.service.dto.Utilisateurs.ClientResponseDTO;
import com.accenture.service.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des clients.
 * Cette classe fournit les fonctionnalités suivantes :
 * Récupérer liste de tous les clients / d'un client par son identifiant / ajout et vérification des infos d'un client/
 * supprimer un client s'il n'a pas de location en cours.
 */

@Service
public class ClientServiceImpl implements ClientService {

    public static final String ID_NON_PRESENT = "ID non présent";
    private final ClientDAO clientDAO;
    private final ClientMapper clientMapper;
    //  private final PasswordEncoder passwordEncoder;


    public ClientServiceImpl(ClientDAO clientDAO, ClientMapper clientMapper) {
        this.clientDAO = clientDAO;
        this.clientMapper = clientMapper;
        //    this.passwordEncoder = passwordEncoder;
    }

    /**
     * Récupère la liste de tous les clients et les convertits en objets ClientResponseDTO
     *
     * @return une liste d'objet (@link ClientResponseDTO) représentant tous les clients
     */

    @Override
    public List<ClientResponseDTO> trouverTous() {
        return clientDAO.findAll().stream()
                .map(clientMapper::toClientResponseDTO)
                .toList();
    }

    /**
     * Rechercher un client par son identifiant et le convertit en un object ClientResponseDTO
     *
     * @param id est l'identifiant du client à rechercher
     * @return un object (@link ClientResponseDTO) représentant le client trouvé.
     * @throws EntityNotFoundException si aucun client n'a été trouver avec cet ID.
     */

    @Override
    public ClientResponseDTO trouver(long id) throws EntityNotFoundException {
        Optional<Client> optClient = clientDAO.findById(id);
        if (optClient.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Client client = optClient.get();
        return clientMapper.toClientResponseDTO(client);
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
    public ClientResponseDTO infosCompte(String login, String password) throws EntityNotFoundException {
        Client client = verifClient(login, password);
        return clientMapper.toClientResponseDTO(client);
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
    public ClientResponseDTO suppCompte(String login, String password) throws EntityNotFoundException {
        Client client = verifClient(login, password);
        clientDAO.delete(client);
        return clientMapper.toClientResponseDTO(client);
    }


    /**
     * Ajoute un client et le convertit en un objet ClientResponseDTO
     *
     * @param clientRequestDTO : objet contenant les informations du client à enregistrer.
     * @return un objet (@link ClientResponseDTO) représentant le client ajouté
     * @throws ClientException si un des paramètres n'est pas correct.
     */

    @Override
    public ClientResponseDTO ajouter(ClientRequestDTO clientRequestDTO) throws ClientException {
        verifierClient(clientRequestDTO);
        Client client = clientMapper.toClient(clientRequestDTO);
//       String passwordChiffre = passwordEncoder.encode(client.getPassword());
//       client.setPassword(passwordChiffre);
        Client clientEnreg = clientDAO.save(client);
        return clientMapper.toClientResponseDTO(clientEnreg);
    }

    /**
     * Modifie partiellement les informations d'un client en mettant à jour uniquement les champs non nuls fournis
     * dans {@code clientRequestDTO}.
     *
     * @param login Le login du client à modifier.
     * @param password Le mot de passe du client à modifier.
     * @param clientRequestDTO L'objet contenant les nouvelles valeurs des champs à mettre à jour.
     * @return Un {@code ClientResponseDTO} contenant les informations mises à jour du client.
     * @throws EntityNotFoundException Si le login ou le mot de passe est incorrect.
     * @throws ClientException Si une erreur spécifique liée au client survient.
     */
    @Override
    public ClientResponseDTO modifPartielle(String login, String password, ClientRequestDTO clientRequestDTO) throws EntityNotFoundException, ClientException {
        Client clientExistant = verifClient(login, password);
        Client nouveau = clientMapper.toClient(clientRequestDTO);
        remplacerExistantParNouveau(clientExistant, nouveau);
        ClientRequestDTO dto = clientMapper.toClientRequestDTO(clientExistant);
        verifierClient(dto);
        Client clientEnr = clientDAO.save(clientExistant);
        return clientMapper.toClientResponseDTO(clientEnr);
    }


//************************************************************************************************************************
//                                                      METHODES PRIVEES
//************************************************************************************************************************

    /**
     * Vérifie la validité des informations d'un client avant son enregistrement.
     * Cette méthode s'assure que toutes les informations requises sont bien renseignées
     * et que le client respecte certaines règles (ex : être majeur, mot de passe sécurisé).
     *
     * @param clientRequestDTO Les informations du client à valider.
     * @throws ClientException          Si une donnée obligatoire est manquante ou incorrecte.
     * @throws IllegalArgumentException Si le client n'est pas majeur.
     */


    private static void verifierClient(ClientRequestDTO clientRequestDTO) {
        if (clientRequestDTO == null)
            throw new ClientException("Le client est null ");
        if (clientRequestDTO.nom() == null || clientRequestDTO.nom().isBlank())
            throw new ClientException("Le nom est obligatoire ");
        if (clientRequestDTO.prenom() == null || clientRequestDTO.prenom().isBlank())
            throw new ClientException("Le prenom est obligatoire ");
        if (clientRequestDTO.dateNaissance() == null)
            throw new ClientException("Le date de Naissance est obligatoire ");
        if ((clientRequestDTO.dateNaissance().plusYears(18).isAfter(LocalDate.now())))
            throw new IllegalArgumentException("Vous devez être majeur pour vous inscrire");
        if (clientRequestDTO.password() == null || clientRequestDTO.password().isBlank())
            throw new ClientException("Le mot de passe est obligatoire ");
        if (clientRequestDTO.email() == null || clientRequestDTO.email().isBlank())
            throw new ClientException("L'email est obligatoire ");
        if (clientRequestDTO.adresse() == null)
            throw new ClientException("L'adresse est obligatoire ");
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[&#@_§-]).{8,16}$";
        if (!clientRequestDTO.password().matches(passwordRegex))
            throw new ClientException("Le mot de passe doit contenir entre 8 et 16 caractères, au minimum une minuscule et 1 majuscule" +
                    "un chiffre, et un caractère spécial");
        String emailRegex = "^[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!clientRequestDTO.email().matches(emailRegex)) {
            throw new ClientException("L'email doit contenir un @ et un nom de domaine valide");
        }
    }


    private Client verifClient(String login, String password) {
        Optional<Client> optClient = clientDAO.findByLogin(login);
        if (optClient.isEmpty())
            throw new EntityNotFoundException("Erreur dans l'email ou le mot de passe");
        Client client = optClient.get();
        if (!client.getPassword().equals(password))
            throw new EntityNotFoundException("Erreur dans l'email ou le mot de passe");
        return client;
    }

    private static void remplacerExistantParNouveau(Client clientExistant, Client client) {
        if (client.getPassword() != null)
            clientExistant.setPassword(client.getPassword());
        if (client.getNom() != null)
            clientExistant.setNom(client.getNom());
        if (client.getPrenom() != null)
            clientExistant.setPrenom(client.getPrenom());
        if (client.getDateNaissance() != null)
            clientExistant.setDateNaissance(client.getDateNaissance());
        if (client.getLogin() != null)
            clientExistant.setLogin(client.getLogin());
        if (client.getPermis() != null)
            clientExistant.setPermis(client.getPermis());
        if (client.getAdresse() != null)
            clientExistant.setAdresse(client.getAdresse());
    }



}
