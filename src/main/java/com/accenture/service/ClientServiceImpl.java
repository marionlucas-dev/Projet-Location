package com.accenture.service;

import com.accenture.exception.ClientException;
import com.accenture.repository.ClientDAO;
import com.accenture.repository.entity.Utilisateurs.Client;
import com.accenture.service.dto.ClientRequestDTO;
import com.accenture.service.dto.ClientResponseDTO;
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
        Optional<Client> optTache = clientDAO.findById(id);
        if (optTache.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Client client = optTache.get();
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
     * Supprime un client en fonction de son identifiant
     *
     * @param id est l'identifiant du client à rechercher
     * @throws EntityNotFoundException si aucun client correspondant à l'ID fourni n'est trouvé
     */
    @Override
    public void supprimer(long id) throws EntityNotFoundException {
        if (clientDAO.existsById(id))
            clientDAO.deleteById(id);
        else throw new EntityNotFoundException(ID_NON_PRESENT);
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
    }


}
