package com.accenture;

import com.accenture.exception.ClientException;
import com.accenture.repository.ClientDAO;
import com.accenture.repository.entity.Utilisateurs.Adresse;
import com.accenture.repository.entity.Utilisateurs.Client;
import com.accenture.service.dto.Utilisateurs.AdresseDTO;
import com.accenture.service.dto.Utilisateurs.ClientRequestDTO;
import com.accenture.shared.Permis;
import com.accenture.service.ClientServiceImpl;
import com.accenture.service.dto.Utilisateurs.ClientResponseDTO;
import com.accenture.service.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    ClientDAO daoMock;
    @Mock
    ClientMapper mapperMock;
    @InjectMocks
    ClientServiceImpl service;

    @DisplayName("""
            Test de la méthode trouver (int id) qui doit renvoyer une exception lorsque le client n'existe pas en base
            """)
    @Test
    void testTrouverExistePas() {
        Mockito.when(daoMock.findById(50L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver(50));
        assertEquals("ID non présent", ex.getMessage());
    }

    @DisplayName("""
            Test de la méthode trouver (int id) qui doit renvoyer une ClientResponseDTO lorsque le client existe  en base
            """)
    @Test
    void testTrouverExiste() {
        Client c = creeClient();
        Optional<Client> optClient = Optional.of(c);
        Mockito.when(daoMock.findById(1L)).thenReturn(optClient);
        ClientResponseDTO dto = creerClient1ResponseDTO();
        Mockito.when(mapperMock.toClientResponseDTO(c)).thenReturn(dto);
        assertSame(dto, service.trouver(1));
    }


    @DisplayName("""
            Test de la méthode trouverTous qui doit renvoyer une liste de clientsResponseDTO correspondant aux clients existants en base.
            """)
    @Test
    void testTrouverTous() {
        Client client1 = creeClient();
        Client client2 = CreeClient2();

        ClientResponseDTO client1ClientResponseDTO = creerClient1ResponseDTO();
        ClientResponseDTO client2ClientResponseDTO = creerClient2ResponseDTO();

        List<Client> clients = List.of(client1, client2);
        List<ClientResponseDTO> clientsDTO = List.of(client1ClientResponseDTO, client2ClientResponseDTO);

        Mockito.when(daoMock.findAll()).thenReturn(clients);
        Mockito.when(mapperMock.toClientResponseDTO(client1)).thenReturn(client1ClientResponseDTO);
        Mockito.when(mapperMock.toClientResponseDTO(client2)).thenReturn(client2ClientResponseDTO);
        assertEquals(clientsDTO, service.trouverTous());
    }


//***********************************************************************************************************************
//                               TOUTES LES METHODES POUR TESTER AJOUTER
//                                            + VERIFIER CLIENTS
//***********************************************************************************************************************


    @DisplayName("Si ajouter(null), exception levée")
    @Test
    void testAjouter() {
        assertThrows(ClientException.class, () -> service.ajouter(null));
    }

    @DisplayName("Si ajouter nom null, exception levée")
    @Test
    void testAjouterSansNom() {
        ClientRequestDTO dto = new ClientRequestDTO(null, "Marion", "moicmama@gmail.com", "azerty",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter nom vide, exception levée")
    @Test
    void testAjouterAvecNomVide() {
        ClientRequestDTO dto = new ClientRequestDTO("\t", "Marion", "moicmama@gmail.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter prenom null, exception levée")
    @Test
    void testAjouterSansPrenom() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", null, "moicmama@gmail.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter prenom vide, exception levée")
    @Test
    void testAjouterAvecPrenomVide() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "\n", "moicmama@gmail.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter dateNaissance null, exception levée")
    @Test
    void testAjouterAvecDateNaissanceNull() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                null, Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter dateNaissance - 18 ans , exception levée")
    @Test
    void testAjouterAvecDateNaissanceMoins18ans() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(2020, 7, 20), Permis.B1);
        assertThrows(IllegalArgumentException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter password null, exception levée")
    @Test
    void testAjouterAvecPasswordNull() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "moicmama@gmail.com", null,
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter password vide, exception levée")
    @Test
    void testAjouterAvecPasswordVide() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "\t",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter password sans toutes les spécificités , exception levée")
    @Test
    void testAjouterAvecPasswordIncorect() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "azerty",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter email null, exception levée")
    @Test
    void testAjouterAvecMailNull() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", null, "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter email vide, exception levée")
    @Test
    void testAjouterAvecMailVide() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "\t", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter email erroné, exception levée")
    @Test
    void testAjouterAvecMailErreur() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "moicm.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter Adresse null, exception levée")
    @Test
    void testAjouterAvecAdresseNull() {
        ClientRequestDTO dto = new ClientRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "Azerty@44",
                null, LocalDate.of(1996, 7, 20), Permis.B1);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter (ClientRequestDTO OK)
                Alors save est appeler
                 et ClientRequestDTO renvoyer
            """)
    @Test
    void testAjouterOK() {
        ClientRequestDTO requestDTO = creerClient1RequestDTO();
        Client clientAvantEnreg = creeClient();
        clientAvantEnreg.setId(0);
        Client clientApresEnreg = creeClient();
        ClientResponseDTO responseDTO = creerClient1ResponseDTO();

        Mockito.when(mapperMock.toClient(requestDTO)).thenReturn(clientAvantEnreg);
        Mockito.when(daoMock.save(clientAvantEnreg)).thenReturn(clientApresEnreg);
        Mockito.when(mapperMock.toClientResponseDTO(clientApresEnreg)).thenReturn(responseDTO);

        assertSame(responseDTO, service.ajouter(requestDTO));
        Mockito.verify(daoMock, Mockito.times(1)).save(clientAvantEnreg);
    }


//***********************************************************************************************************************
//                               TOUTES LES METHODES POUR TESTER InfosComptes
//***********************************************************************************************************************

    @DisplayName("""
            Test qui vérifie que la méthode lève bien une exception lorsque le login fournis n'existe pas dans la basse de donnée
            """)
    @Test
    void testInfosClientsMail() {
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.infosCompte("moicmama@gmail.com", "Azerty@96"));
        assertEquals("Erreur dans l'email ou le mot de passe", ex.getMessage());
        Mockito.verify(daoMock).findByLogin("moicmama@gmail.com");
    }


    @DisplayName("""
            Test qui vérifie que la méthode fonctionne quand le mot de passe est en base.
            """)
    @Test
    void testInfosClientsPasswordPareil() {
        Client fakeClient = new Client();
        fakeClient.setPassword("Azerty@96");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(fakeClient));
        assertDoesNotThrow(() -> service.infosCompte("moicmama@gmail.com", "Azerty@96"));

    }

    @DisplayName("""
            Test qui vérifie que la méthode lève bien une exception lorsque le password fournis n'existe pas dans la basse de donnée
            """)
    @Test
    void testInfosClientsPassword() {
        Client fakeClient = new Client();
        fakeClient.setPassword("AutreMDP");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(fakeClient));
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.infosCompte("moicmama@gmail.com", "Azerty@96"));
        assertEquals("Erreur dans l'email ou le mot de passe", ex.getMessage());
    }

    @DisplayName("Test qui vérifie la totalité de la méthode infosCompte")
    @Test
    void testMethodeInfosCompte() {
        Client client = new Client();
        client.setPassword("Azerty@96");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(client));

        ClientResponseDTO responseDTO = creerClient1ResponseDTO();
        Mockito.when(mapperMock.toClientResponseDTO(client)).thenReturn(responseDTO);

        ClientResponseDTO resultat = service.infosCompte("moicmama@gmail.com", "Azerty@96");
        assertNotNull(resultat);
        Mockito.verify(mapperMock).toClientResponseDTO(client);

    }


//***********************************************************************************************************************
//                                                 METHODES SUPP Compte
//***********************************************************************************************************************

    @DisplayName("Supression d'un compte lorsque les infos sont confirmés par le client ")
    @Test
    void testMethodeSuppCompte() {
        Client client = new Client();
        client.setLogin("moicmama@gmail.com");
        client.setPassword("Azerty@96");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(client));
        ClientResponseDTO responseDTO = creerClient1ResponseDTO();
        Mockito.when(mapperMock.toClientResponseDTO(client)).thenReturn(responseDTO);

        ClientResponseDTO resultat = service.suppCompte("moicmama@gmail.com", "Azerty@96");
        assertNotNull(resultat);
        Mockito.verify(mapperMock).toClientResponseDTO(client);


    }

//***********************************************************************************************************************
//                                                 METHODES ModiffPartielle
//***********************************************************************************************************************

    @DisplayName("""
            Id qui n'existe sinon renvoyer une exception
            """)
    @Test
    void modifierSiIdNonPresent() {
        //  Mockito.when(daoMock.existsByLogin("moicmama@gmail.com")).thenReturn(false);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.modifPartielle("moicmama@gmail.com", "Azerty@96", creerClient1RequestDTO()));
        assertEquals("Erreur dans l'email ou le mot de passe", ex.getMessage());
    }


    @DisplayName("Modification réussie d'un client existant")
    @Test
    void modifierClientExistant() {
        String login = "moicmama@gmail.com";
        String password = "Azerty@96";
        Client clientExistant = new Client();
        clientExistant.setLogin(login);
        clientExistant.setPassword(password);

        ClientRequestDTO clientRequestDTO = creerClient1RequestDTO();

        Client clientModifie = new Client();
        clientModifie.setLogin(login);
        clientModifie.setPassword(password);
        clientModifie.setNom("Lucas");
        clientModifie.setPrenom("Mélodie");
        clientModifie.setLogin("melodie.marigonez@hotmail.com");
        clientModifie.setAdresse(new Adresse(1, "75 rue du moulin Soline", "44115", "Basse Goulaine"));
        clientModifie.setDateNaissance(LocalDate.of(1999, 7, 27));
        clientModifie.setPermis(Permis.B1);
        clientModifie.setDateInscription(LocalDate.now());


        ClientResponseDTO responseDTO = creerClient2ResponseDTO();


        Mockito.when(daoMock.findByLogin(login)).thenReturn(Optional.of(clientExistant));
        Mockito.when(mapperMock.toClient(clientRequestDTO)).thenReturn(clientModifie);
        Mockito.when(daoMock.save(clientExistant)).thenReturn(clientExistant);
        Mockito.when(mapperMock.toClientRequestDTO(clientExistant)).thenReturn(clientRequestDTO);
        Mockito.when(mapperMock.toClientResponseDTO(clientExistant)).thenReturn(responseDTO);

        ClientResponseDTO result = service.modifPartielle(login, password, clientRequestDTO);

        assertNotNull(result);
        assertEquals("Marigonez", result.nom());
    }


    @DisplayName("Modification réussie d'un client null")
    @Test
    void modifierClientNull() {

        String login = "moicmama@gmail.com";
        String password = "Azerty@96";
        Client clientExistant = new Client();
        clientExistant.setLogin(login);
        clientExistant.setPassword(password);

        ClientRequestDTO clientRequestDTO = creerClient1RequestDTO();

        Client clientModifie = new Client();
        clientModifie.setLogin(login);
        clientModifie.setPassword(null);
        clientModifie.setNom(null);
        clientModifie.setPrenom(null);
        clientModifie.setLogin(null);
        clientModifie.setAdresse(null);
        clientModifie.setDateNaissance(null);
        clientModifie.setPermis(null);
        clientModifie.setDateInscription(null);


        ClientResponseDTO responseDTO = creerClient2ResponseDTO();

        Mockito.when(daoMock.findByLogin(login)).thenReturn(Optional.of(clientExistant));
        Mockito.when(mapperMock.toClient(clientRequestDTO)).thenReturn(clientModifie);
        Mockito.when(daoMock.save(clientExistant)).thenReturn(clientExistant);
        Mockito.when(mapperMock.toClientRequestDTO(clientExistant)).thenReturn(clientRequestDTO);
        Mockito.when(mapperMock.toClientResponseDTO(clientExistant)).thenReturn(responseDTO);


        ClientResponseDTO result = service.modifPartielle(login, password, clientRequestDTO);

        assertThrows(EntityNotFoundException.class, () -> service.modifPartielle("melodie.marigonez@hotmail.com", "Erreur dans l'email ou le mot de passe", clientRequestDTO));
        assertEquals("Marigonez", result.nom());
    }


    @DisplayName("Modification réussie d'un client existant avec quelques paramètres ")
    @Test
    void modifierClientExistantQuelquesParametres() {
        String login = "moicmama@gmail.com";
        String password = "Azerty@96";
        Client clientExistant = new Client();
        clientExistant.setLogin(login);
        clientExistant.setPassword(password);

        ClientRequestDTO clientRequestDTO = creerClient1RequestDTO();

        Client clientModifie = new Client();
        clientModifie.setLogin(login);
        clientModifie.setPassword(password);
        clientModifie.setLogin("melodie.marigonez@hotmail.com");
        clientModifie.setAdresse(new Adresse(1, "75 rue du moulin Soline", "44115", "Basse Goulaine"));
        clientModifie.setDateInscription(LocalDate.now());


        ClientResponseDTO responseDTO = creerClient2ResponseDTO();

        Mockito.when(daoMock.findByLogin(login)).thenReturn(Optional.of(clientExistant));
        Mockito.when(mapperMock.toClient(clientRequestDTO)).thenReturn(clientModifie);
        Mockito.when(daoMock.save(clientExistant)).thenReturn(clientExistant);
        Mockito.when(mapperMock.toClientRequestDTO(clientExistant)).thenReturn(clientRequestDTO);
        Mockito.when(mapperMock.toClientResponseDTO(clientExistant)).thenReturn(responseDTO);

        ClientResponseDTO result = service.modifPartielle(login, password, clientRequestDTO);

        assertNotNull(result);
        assertEquals("Marigonez", result.nom());
    }


//***********************************************************************************************************************
//                                                          METHODES PRIVEES
//***********************************************************************************************************************

    private static Client getClient() {
        Client client = new Client();
        client.setLogin("moicmama@gmail.com");
        client.setPassword("Azerty@96");
        return client;
    }


    private static Client creeClient() {
        Client client = new Client();
        client.setId(1);
        client.setPrenom("Marion");
        client.setNom("Lucas");
        client.setAdresse(new Adresse(1, "75 rue du moulin Soline", "44115", "Basse Goulaine"));
        client.setLogin("moicmama@gmail.com");
        client.setPassword("Azerty@44");
        client.setDateNaissance(LocalDate.of(1996, 7, 20));
        client.setDateInscription(LocalDate.now());
        client.setPermis(Permis.B1);
        client.setDesactive(false);
        return client;
    }

    private static Client CreeClient2() {
        Client client = new Client();
        client.setId(2);
        client.setPrenom("Mélodie");
        client.setNom("Marigonez");
        client.setAdresse(new Adresse(1, "75 rue du moulin Soline", "44115", "Basse Goulaine"));
        client.setLogin("melodie.marigonez@hotemail.com");
        client.setPassword("Azerty@44");
        client.setDateNaissance(LocalDate.of(1999, 7, 27));
        client.setDateInscription(LocalDate.now());
        client.setPermis(Permis.B1);
        client.setDesactive(false);
        return client;
    }

    private static ClientResponseDTO creerClient1ResponseDTO() {
        return new ClientResponseDTO(1, "Lucas", "Marion", "moicmama@gmail.com",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1, LocalDate.now());
    }

    private static ClientResponseDTO creerClient2ResponseDTO() {
        return new ClientResponseDTO(2, "Marigonez", "Mélodie", "melodie.marigonez@hotmail.com",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1999, 7, 27), Permis.B1, LocalDate.now());
    }

    private static ClientRequestDTO creerClient1RequestDTO() {
        return new ClientRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1996, 7, 20), Permis.B1);
    }

    private static ClientRequestDTO creerClient2RequestDTO() {
        return new ClientRequestDTO("Marigonez", "Mélodie", "melodie.marigonez@hotmail.com", "Azerty@44",
                new AdresseDTO("75 rue du moulin Soline", "44115", "Basse Goulaine"),
                LocalDate.of(1999, 7, 27), Permis.B1);
    }


}
