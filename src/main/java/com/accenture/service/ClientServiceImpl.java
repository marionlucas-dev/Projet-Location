package com.accenture.service;

import com.accenture.exception.ClientException;
import com.accenture.repository.ClientDAO;
import com.accenture.repository.entity.Utilisateurs.Client;
import com.accenture.service.dto.ClientRequestDTO;
import com.accenture.service.dto.ClientResponseDTO;
import com.accenture.service.mapper.ClientMapper;
// import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<ClientResponseDTO> trouverTous() {
        return clientDAO.findAll().stream()
                .map(clientMapper::toClientResponseDTO)
                .toList();
    }

    @Override
    public ClientResponseDTO trouver(long id) throws EntityNotFoundException {
        Optional<Client> optTache = clientDAO.findById(id);
        if (optTache.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Client client = optTache.get();
        return clientMapper.toClientResponseDTO(client);
    }


    @Override
    public ClientResponseDTO ajouter(ClientRequestDTO clientRequestDTO) throws ClientException {
        verifierClient(clientRequestDTO);
        Client client = clientMapper.toClient(clientRequestDTO);
//       String passwordChiffre = passwordEncoder.encode(client.getPassword());
//       client.setPassword(passwordChiffre);
        Client clientEnreg = clientDAO.save(client);
        return clientMapper.toClientResponseDTO(clientEnreg);
    }





//************************************************************************************************************************
//                                                      METHODES PRIVEES
//************************************************************************************************************************

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
    }


}
