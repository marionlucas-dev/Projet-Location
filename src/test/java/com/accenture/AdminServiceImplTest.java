package com.accenture;

import com.accenture.exception.AdministrateurException;
import com.accenture.repository.AdministrateurDAO;
import com.accenture.repository.entity.Utilisateurs.Administrateur;
import com.accenture.service.AdministrateurServiceImpl;
import com.accenture.service.dto.*;
import com.accenture.service.mapper.AdministrateurMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    AdministrateurDAO daoMock;
    @Mock
    AdministrateurMapper mapperMock;
    @InjectMocks
    AdministrateurServiceImpl service;

    @DisplayName("""
            Test de la méthode trouver (int id) qui doit renvoyer une exception lorsque l'admin n'existe pas en base
            """)
    @Test
    void testTrouverExistePas() {
        Mockito.when(daoMock.findById(50L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver(50));
        assertEquals("ID non présent", ex.getMessage());
    }

    @DisplayName("""
            Test de la méthode trouver (int id) qui doit renvoyer une AdminResponseDTO lorsque l'admin existe  en base
            """)
    @Test
    void testTrouverExiste() {
        Administrateur a = creeAdmin();
        Optional<Administrateur> optClient = Optional.of(a);
        Mockito.when(daoMock.findById(1L)).thenReturn(optClient);
        AdministrateurResponseDTO dto = creerAdmin1ResponseDTO();
        Mockito.when(mapperMock.toAdminResponseDTO(a)).thenReturn(dto);
        assertSame(dto, service.trouver(1));
    }

    @DisplayName("""
            Test de la méthode trouverTous qui doit renvoyer une liste de adminResponseDTO correspondant aux admin existants en base.
            """)
    @Test
    void testTrouverTous() {
        Administrateur admin1 = creeAdmin();
        Administrateur admin2 = creeAdmin2();

        AdministrateurResponseDTO admin1ClientResponseDTO = creerAdmin1ResponseDTO();
        AdministrateurResponseDTO admin2ClientResponseDTO = creerAdmin2ResponseDTO();

        List<Administrateur> admin = List.of(admin1, admin2);
        List<AdministrateurResponseDTO> adminDTO = List.of(admin1ClientResponseDTO, admin2ClientResponseDTO);

        Mockito.when(daoMock.findAll()).thenReturn(admin);
        Mockito.when(mapperMock.toAdminResponseDTO(admin1)).thenReturn(admin1ClientResponseDTO);
        Mockito.when(mapperMock.toAdminResponseDTO(admin2)).thenReturn(admin2ClientResponseDTO);
        assertEquals(adminDTO, service.trouverTous());
    }

//***********************************************************************************************************************
//                               TOUTES LES METHODES POUR TESTER AJOUTER
//                                            + VERIFIERCLIENTS
//***********************************************************************************************************************

    @DisplayName("Si ajouter(null), exception levée")
    @Test
    void testAjouter() {
        assertThrows(AdministrateurException.class, () -> service.ajouter(null));
    }

    @DisplayName("Si ajouter nom null, exception levée")
    @Test
    void testAjouterSansNom() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO(null, "Marion", "moicmama@gmail.com",
                "Azerty@96", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter nom vide, exception levée")
    @Test
    void testAjouterAvecNomVide() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("\t", "Marion", "moicmama@gmail.com",
                "Azerty@96", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter prenom null, exception levée")
    @Test
    void testAjouterSansPrenom() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", null, "moicmama@gmail.com",
                "Azerty@96", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter prenom vide, exception levée")
    @Test
    void testAjouterAvecPrenomVide() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "\t", "moicmama@gmail.com", "Azerty@96", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Si ajouter password null, exception levée")
    @Test
    void testAjouterAvecPasswordNull() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", "moicmama@gmail.com", null, "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter password vide, exception levée")
    @Test
    void testAjouterAvecPasswordVide() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "\t",
                "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter password sans toutes les spécificités , exception levée")
    @Test
    void testAjouterAvecPasswordIncorect() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "azert", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter email null, exception levée")
    @Test
    void testAjouterAvecMailNull() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", null, "Azerty@96", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter email vide, exception levée")
    @Test
    void testAjouterAvecMailVide() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", "\n", "Azerty@96", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter email ne respectant pas les conditions, exception levée")
    @Test
    void testAjouterAvecMailConditions() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", "aejbff", "Azerty@96", "CEO");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter fonction null, exception levée")
    @Test
    void testAjouterAvecFonctionNull() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "Azerty@96", null);
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter fonction vide, exception levée")
    @Test
    void testAjouterAvecFonctionVide() {
        AdministrateurRequestDTO dto = new AdministrateurRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "Azerty@96", "\n");
        assertThrows(AdministrateurException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter (AdminRequestDTO OK)
                Alors save est appeler
                 et AdminRequestDTO renvoyer
            """)
    @Test
    void testAjouterOK() {
        AdministrateurRequestDTO requestDTO = creerAdmin1RequestDTO();
        Administrateur adminAvantEnreg = creeAdmin();
        adminAvantEnreg.setId(0);
        Administrateur adminApresEnreg = creeAdmin();
        AdministrateurResponseDTO responseDTO = creerAdmin1ResponseDTO();

        Mockito.when(mapperMock.toAdministrateur(requestDTO)).thenReturn(adminAvantEnreg);
        Mockito.when(daoMock.save(adminAvantEnreg)).thenReturn(adminApresEnreg);
        Mockito.when(mapperMock.toAdminResponseDTO(adminApresEnreg)).thenReturn(responseDTO);

        assertSame(responseDTO, service.ajouter(requestDTO));
        Mockito.verify(daoMock, Mockito.times(1)).save(adminAvantEnreg);
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
        Administrateur fakeAdmin = new Administrateur();
        fakeAdmin.setPassword("Azerty@96");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(fakeAdmin));
        assertDoesNotThrow(() -> service.infosCompte("moicmama@gmail.com", "Azerty@96"));

    }

    @DisplayName("""
            Test qui vérifie que la méthode lève bien une exception lorsque le password fournis n'existe pas dans la basse de donnée
            """)
    @Test
    void testInfosClientsPassword() {
        Administrateur fakeAdmin = new Administrateur();
        fakeAdmin.setPassword("AutreMDP");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(fakeAdmin));
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.infosCompte("moicmama@gmail.com", "Azerty@96"));
        assertEquals("Erreur dans l'email ou le mot de passe", ex.getMessage());
    }

    @DisplayName("Test qui vérifie la totalité de la méthode infosCompte")
    @Test
    void testMethodeInfosCompte() {
        Administrateur fakeAdmin = new Administrateur();
        fakeAdmin.setPassword("Azerty@96");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(fakeAdmin));

        AdministrateurResponseDTO responseDTO = creerAdmin1ResponseDTO();
        Mockito.when(mapperMock.toAdminResponseDTO(fakeAdmin)).thenReturn(responseDTO);

        AdministrateurResponseDTO resultat = service.infosCompte("moicmama@gmail.com", "Azerty@96");
        assertNotNull(resultat);
        Mockito.verify(mapperMock).toAdminResponseDTO(fakeAdmin);

    }


//***********************************************************************************************************************
//                                                 METHODES SUPP Compte
//***********************************************************************************************************************

    @DisplayName("Supression d'un compte lorsque les infos sont confirmés par le client ")
    @Test
    void testMethodeSuppCompte() {
        Administrateur admin = new Administrateur();
        admin.setLogin("moicmama@gmail.com");
        admin.setPassword("Azerty@96");
        Mockito.when(daoMock.findByLogin("moicmama@gmail.com")).thenReturn(Optional.of(admin));
        AdministrateurResponseDTO responseDTO = creerAdmin1ResponseDTO();
        Mockito.when(mapperMock.toAdminResponseDTO(admin)).thenReturn(responseDTO);

        AdministrateurResponseDTO resultat = service.suppCompte("moicmama@gmail.com", "Azerty@96");
        assertNotNull(resultat);
        Mockito.verify(mapperMock).toAdminResponseDTO(admin);


    }




//***********************************************************************************************************************
//                                               METHODE MODIF PARTIEL
//***********************************************************************************************************************

    @DisplayName("""
            Id qui n'existe sinon renvoyer une exception
            """)
    @Test
    void modifierSiIdNonPresent() {
        //  Mockito.when(daoMock.existsByLogin("moicmama@gmail.com")).thenReturn(false);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.modifPartielle("moicmama@gmail.com", "Azerty@96", creerAdmin1RequestDTO()));
        assertEquals("Erreur dans l'email ou le mot de passe", ex.getMessage());
    }


    @DisplayName("Modification réussie d'un client existant")
    @Test
    void modifierClientExistant() {
        // GIVEN
        String login = "moicmama@gmail.com";
        String password = "Azerty@96";
        Administrateur adminExistant = new Administrateur();
        adminExistant.setLogin(login);
        adminExistant.setPassword(password);

        AdministrateurRequestDTO adminRequestDTO = creerAdmin1RequestDTO();

        Administrateur adminModifie = new Administrateur();
        adminModifie.setLogin(login);
        adminModifie.setPassword(password);
        adminModifie.setNom("Lucas");
        adminModifie.setPrenom("Mélodie");
        adminModifie.setLogin("melodie.marigonez@hotmail.com");
        adminModifie.setFonction("Vice CEO ");


        AdministrateurResponseDTO responseDTO = creerAdmin2ResponseDTO();

        // WHEN
        Mockito.when(daoMock.findByLogin(login)).thenReturn(Optional.of(adminExistant));
        Mockito.when(mapperMock.toAdministrateur(adminRequestDTO)).thenReturn(adminModifie);
        Mockito.when(daoMock.save(adminExistant)).thenReturn(adminExistant);
        Mockito.when(mapperMock.toAdminResponseDTO(adminExistant)).thenReturn(responseDTO);

        // THEN
       AdministrateurResponseDTO result = service.modifPartielle(login, password, adminRequestDTO);

        assertNotNull(result);
        assertEquals("Marigonez", result.nom());
    }




    @DisplayName("Modification réussie d'un client existant")
    @Test
    void modifierClientNull() {
        // GIVEN
        String login = "moicmama@gmail.com";
        String password = "Azerty@96";
        Administrateur adminExistant = new Administrateur();
        adminExistant.setLogin(login);
        adminExistant.setPassword(password);

        AdministrateurRequestDTO adminRequestDTO = creerAdmin1RequestDTO();

        Administrateur adminModifie = new Administrateur();
        adminModifie.setLogin(login);
        adminModifie.setPassword(null);
        adminModifie.setNom(null);
        adminModifie.setPrenom(null);
        adminModifie.setLogin(null);
        adminModifie.setFonction(null);



        AdministrateurResponseDTO responseDTO = creerAdmin2ResponseDTO();

        // WHEN
        Mockito.when(daoMock.findByLogin(login)).thenReturn(Optional.of( adminExistant));
        Mockito.when(mapperMock.toAdministrateur(adminRequestDTO)).thenReturn(adminModifie);
        Mockito.when(daoMock.save( adminExistant)).thenReturn( adminExistant);
        Mockito.when(mapperMock.toAdminResponseDTO(adminExistant)).thenReturn(responseDTO);

        // THEN
        AdministrateurResponseDTO result = service.modifPartielle(login, password,adminRequestDTO);

        assertThrows(EntityNotFoundException.class, () -> service.modifPartielle("melodie.marigonez@hotmail.com", "Erreur dans l'email ou le mot de passe", adminRequestDTO));
        assertEquals("Marigonez", result.nom());
    }
























//***********************************************************************************************************************
//                               TOUTES LES METHODES POUR TESTER SUPPRIMER
//***********************************************************************************************************************

//    @DisplayName("Verification de l'existence de l'ID ")
//    @Test
//    void testIdPresent() {
//        Mockito.when(daoMock.existsById(1L)).thenReturn(false);
//        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.supprimer(1L));
//        assertEquals("ID non présent", ex.getMessage());
//    }
//
//    @DisplayName("Test pour supprimer un client ")
//    @Test
//    void testMethodeSupprimer() {
//        Mockito.when(daoMock.existsById(1L)).thenReturn(true);
//        assertDoesNotThrow(() -> service.supprimer(1L));
//        Mockito.verify(daoMock, Mockito.times(1)).deleteById(1L);
//    }


//***********************************************************************************************************************
//                                                          METHODES PRIVEES
//***********************************************************************************************************************

    private static Administrateur creeAdmin() {
        Administrateur admin = new Administrateur();
        admin.setId(1L);
        admin.setNom("Lucas");
        admin.setPrenom("Marion");
        admin.setFonction("CEO");
        admin.setLogin("moicmama@gmail.com");
        admin.setPassword("Azerty@96");
        return admin;
    }

    private static Administrateur creeAdmin2() {
        Administrateur admin = new Administrateur();
        admin.setId(2L);
        admin.setNom("Marigonez");
        admin.setPrenom("Mélodie");
        admin.setFonction("Vice CEO ");
        admin.setLogin("melodie.marigonez@hotmail.com");
        admin.setPassword("Azerty@96");
        return admin;
    }

    private static AdministrateurResponseDTO creerAdmin1ResponseDTO() {
        return new AdministrateurResponseDTO( 1L,"Lucas", "Marion", "moicmama@gmail.com", "CEO");
    }

    private static AdministrateurResponseDTO creerAdmin2ResponseDTO() {
        return new AdministrateurResponseDTO(1L,"Marigonez", "Mélodie", "melodie.marigonez@hotmail.com","Vice CEO ");

    }

    private static AdministrateurRequestDTO creerAdmin1RequestDTO() {
        return new AdministrateurRequestDTO("Lucas", "Marion", "moicmama@gmail.com", "Azerty@96", "CEO");
    }

    private static AdministrateurRequestDTO creerAdmin2RequestDTO() {
        return new AdministrateurRequestDTO("Marigonez", "Mélodie", "melodie.marigonez@hotmail.com",
                "Azerty@96", "Vice CEO ");
    }


}
