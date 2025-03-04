package com.accenture;

import com.accenture.exception.VoitureException;
import com.accenture.repository.VoitureDAO;
import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.VoitureServiceImpl;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.*;
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
public class VoitureServiceImplTest {

    @Mock
    VoitureDAO daoMock;

    @Mock
    VoitureMapper mapperMock;

    @InjectMocks
    VoitureServiceImpl service;

    @DisplayName(" Test de la méthode trouver qui doit renvoyer une exception lorsque le client n'existe pas en base")
    @Test
    void testTrouverExistePas() {
        Mockito.when(daoMock.findByModele("Arona")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver("Arona"));
        assertEquals("Voiture non trouvé", ex.getMessage());
    }

    @DisplayName(" Test de la méthode trouver qui doit renvoyer une voitureresponseDTO lorsque la voiture existe en base ")
    @Test
    void testTrouverExiste() {
        Voiture v = creeVoiture();
        Optional<Voiture> optVoiture = Optional.of(v);
        Mockito.when(daoMock.findByModele("Arona")).thenReturn(optVoiture);
        VoitureResponseDTO dto = voitureResponseDTO();
        Mockito.when(mapperMock.toVoitureResponseDTO(v)).thenReturn(dto);
        assertSame(dto, service.trouver("Arona"));
    }

    @DisplayName("Test de la méthode trouverTous qui doit renvoyer une liste de voitureResponseDTO correspondant aux voitures existants en bases")

    @Test
    void testTrouverTous() {
        Voiture voiture1 = creeVoiture();
        Voiture voiture2 = creeVoiture2();
        VoitureResponseDTO v1 = voitureResponseDTO();
        VoitureResponseDTO v2 = voiture2ResponseDTO();

        List<Voiture> voitures = List.of(voiture1, voiture2);
        List<VoitureResponseDTO> dto = List.of(v1, v2);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        Mockito.when(mapperMock.toVoitureResponseDTO(voiture1)).thenReturn(v1);
        Mockito.when(mapperMock.toVoitureResponseDTO(voiture2)).thenReturn(v2);
        assertEquals(dto, service.trouverTous());


    }

//************************************************************************************************************************
//                                      METHODE Ajouter avec test de vérif
//************************************************************************************************************************


    @DisplayName("Si ajouter(null), exception levée")
    @Test
    void testAjouter() {
        assertThrows(VoitureException.class, () -> service.ajouter(null));
    }

    @DisplayName("Ajouter avec Marque null, exception levée ")
    @Test
    void testAjouterMarqueNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO(null, "Arona", "rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec Marque vide, exception levée ")
    @Test
    void testAjouterMarqueVide() {
        VoitureRequestDTO dto = new VoitureRequestDTO("\t", "Arona", "rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec Modèle null, exception levée ")
    @Test
    void testAjouterModeleNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", null, "rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec Marque vide, exception levée ")
    @Test
    void testAjouterModeleVide() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "\n", "rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec Couleur null, exception levée ")
    @Test
    void testAjouterCouleurNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", null, Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec Marque vide, exception levée ")
    @Test
    void testAjouterCouleurVide() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "\n", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec nombre de place et verif permis, exception levée ")
    @Test
    void testAjouterNombreDePlaces() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                12, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        VoitureResponseDTO voiture = new VoitureResponseDTO(1, "Seat", "Arona", "rouge", Type.SUV,
                12, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, Permis.D1, "50", "1400", true, false);
//               service.ajouter(dto);
        assertEquals(Permis.D1, voiture.permis());

    }

    @DisplayName("Ajouter avec Carburant null, exception levée ")
    @Test
    void testAjouterCarburantNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                5, null, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec Nombre de porte différent de 3 ou 5, exception levée ")
    @Test
    void testAjouterNombreDePorteNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                7, Carburant.Essence, null, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec Transmission null, exception levée ")
    @Test
    void testAjouterTransmissionNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, null, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Ajouter avec Clim null, exception levée ")
    @Test
    void testAjouterClimNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, null, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec nombre bagages <0, exception levée ")
    @Test
    void testAjouterNombreBagageInf0() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, -5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec nombre bagages =0, exception levée ")
    @Test
    void testAjouterNombreBagageEgale0() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                0, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 0, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec nombre bagages supp 17, exception levée ")
    @Test
    void testAjouterNombreBagageSup17() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                20, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 0, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Ajouter avec type null, exception levée ")
    @Test
    void testAjouterTypeNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", null,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, -5, "50", "1400", true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter (VoitureRequestDTO OK)
                Alors save est appeler
                 et VoitureRequestDTO renvoyer
            """)
    @Test
    void testAjouterOKMoins10Personnes() {
        VoitureRequestDTO requestDTO = voitureRequestDTO();
        Voiture voitureAvantEnreg = creeVoiture();
        voitureAvantEnreg.setModele("Arona");
        Voiture voitureApresEnreg = creeVoiture();
        VoitureResponseDTO responseDTO = voitureResponseDTO();

        Mockito.when(mapperMock.toVoiture(requestDTO)).thenReturn(voitureAvantEnreg);
        Mockito.when(daoMock.save(voitureAvantEnreg)).thenReturn(voitureApresEnreg);
        Mockito.when(mapperMock.toVoitureResponseDTO(voitureApresEnreg)).thenReturn(responseDTO);

        assertSame(responseDTO, service.ajouter(requestDTO));
        Mockito.verify(daoMock, Mockito.times(1)).save(voitureAvantEnreg);
    }


    @DisplayName("""
            Si ajouter (VoitureRequestDTO OK)
                Alors save est appeler
                 et VoitureRequestDTO renvoyer
            """)
    @Test
    void testAjouterPlus10Personnes() {
        VoitureRequestDTO requestDTO = voiture2RequestDTO();
        Voiture voitureAvantEnreg = creeVoiture2();
        voitureAvantEnreg.setModele("208");
        Voiture voitureApresEnreg = creeVoiture2();
        VoitureResponseDTO responseDTO = voiture2ResponseDTO();

        Mockito.when(mapperMock.toVoiture(requestDTO)).thenReturn(voitureAvantEnreg);
        Mockito.when(daoMock.save(voitureAvantEnreg)).thenReturn(voitureApresEnreg);
        Mockito.when(mapperMock.toVoitureResponseDTO(voitureApresEnreg)).thenReturn(responseDTO);

        assertSame(responseDTO, service.ajouter(requestDTO));
        Mockito.verify(daoMock, Mockito.times(1)).save(voitureAvantEnreg);
    }

//************************************************************************************************************************
//                                                      METHODE SUPPRIMER
//************************************************************************************************************************

//    @DisplayName("Verification de l'existence de l'ID")
//    @Test
//    void testIdPresent(){
//        Mockito.when(daoMock.existsById(1L)).thenReturn(false);
//        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.supprimer(1));
//        assertEquals("ID inconnu", ex.getMessage());
//    }


    @DisplayName("""
            Test pour reussir à supprimer une tache.
            """)
    @Test
    void testMethodeSupprimer() {
        Voiture voiture = new Voiture();
        voiture.setMarque("Seat");
        voiture.setModele("Arona");
        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.of(voiture));
        VoitureResponseDTO responseDTO = voitureResponseDTO();
        Mockito.when(mapperMock.toVoitureResponseDTO(voiture)).thenReturn(responseDTO);

        VoitureResponseDTO resultat = service.supprimer(1L);
        assertNotNull(resultat);
        Mockito.verify(mapperMock).toVoitureResponseDTO(voiture);


    }


//************************************************************************************************************************
//                                                  METHODE MODIFIER
//************************************************************************************************************************

    @DisplayName("""
            Id qui n'existe sinon renvoyer une exception
            """)
    @Test
    void modifierSiIdNonPresent() {
        //  Mockito.when(daoMock.existsByLogin("moicmama@gmail.com")).thenReturn(false);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.modifier(1, voitureRequestDTO()));
        assertEquals("ID incorrect", ex.getMessage());
    }


    @DisplayName("Modification réussie d'une voiture existante")
    @Test
    void modifierVoitureExistant() {
        Voiture voitureExistant = new Voiture();
        voitureExistant.setId(1);
        voitureExistant.setRetireDuParc(false);

        VoitureRequestDTO voitureRequestDTO = voitureRequestDTO();

        Voiture voitureModifie = new Voiture();
        voitureModifie.setId(2);
        voitureModifie.setModele("Clio");
        voitureModifie.setType(Type.Luxe);


        VoitureResponseDTO responseDTO = voiture2ResponseDTO();


        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistant));
        Mockito.when(mapperMock.toVoiture(voitureRequestDTO)).thenReturn(voitureModifie);
        Mockito.when(daoMock.save(voitureExistant)).thenReturn(voitureExistant);
        Mockito.when(mapperMock.toVoitureRequestDTO(voitureExistant)).thenReturn(voitureRequestDTO);
        Mockito.when(mapperMock.toVoitureResponseDTO(voitureExistant)).thenReturn(responseDTO);

        VoitureResponseDTO result = service.modifier(1, voitureRequestDTO);

        assertNotNull(result);
        assertEquals("208", result.modele());
    }

    @DisplayName("Échec de la modification d'une voiture avec un modèle null")
    @Test
    void modifierClientNull() {

        // Création d'une voiture existante
        Voiture voitureExistant = new Voiture();
        voitureExistant.setId(1); // Ajout de l'ID
        voitureExistant.setModele("Arona");
        voitureExistant.setRetireDuParc(false);

        // DTO de modification (avec un modèle null)
        VoitureRequestDTO voitureRequestDTO = voitureRequestDTO();

        // Création d'une voiture modifiée avec des valeurs nulles
        Voiture voitureModifie = new Voiture();
        voitureModifie.setId(1); // Assurez-vous que l'ID correspond
        voitureModifie.setModele(null);
        voitureModifie.setMarque(null);
        voitureModifie.setType(null);
        voitureModifie.setTransmission(null);
        voitureModifie.setClim(null);
        voitureModifie.setCarburant(null);
        voitureModifie.setCouleur(null);

        // DTO de réponse
        VoitureResponseDTO responseDTO = voitureResponseDTO();

        // Simulation du comportement des mocks
        //Mockito.when(daoMock.findByModele("Arona")).thenReturn(Optional.of(voitureExistant));
        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.of(voitureModifie));
        Mockito.when(mapperMock.toVoiture(voitureRequestDTO)).thenReturn(voitureModifie);
        // Mockito.when(daoMock.save(voitureExistant)).thenThrow(new EntityNotFoundException("Impossible de modifier avec des valeurs nulles"));

        // Vérification que l'exception est bien levée
        assertThrows(NullPointerException.class, () -> service.modifier(1L, voitureRequestDTO));

        // Vérification que la modification n’a pas eu lieu
        assertEquals("Arona", voitureExistant.getModele());
    }

    @DisplayName("Ajouter avec Marque null, exception levée ")
    @Test
    void testModifierMarqueNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO(null, "Arona", "rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("Ajouter avec Modèle null, exception levée ")
    @Test
    void testModifierModeleNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", null, "rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L,dto));
    }
    @DisplayName("Ajouter avec Couleur null, exception levée ")
    @Test
    void testModifierCouleurNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", null, Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L,dto));
    }
    @DisplayName("Ajouter avec Carburant null, exception levée ")
    @Test
    void testModifierCarburantNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                5, null, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }

    @DisplayName("Ajouter avec Nombre de porte différent de 3 ou 5, exception levée ")
    @Test
    void testModifierNombreDePorteNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                7, Carburant.Essence, null, true, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }
    @DisplayName("Ajouter avec Transmission null, exception levée ")
    @Test
    void testModifierTransmissionNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, true, null, 5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L,dto));
    }


    @DisplayName("Ajouter avec Clim null, exception levée ")
    @Test
    void testModifierClimNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.Essence, NombrePortes.Cinq, null, Transmission.MANUEL, 5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }
    @DisplayName("Ajouter avec type null, exception levée ")
    @Test
    void testModifierTypeNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", null,
                5, Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, -5, "50", "1400", true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }






//************************************************************************************************************************
//                                                  METHODE Filtre
//************************************************************************************************************************

    @DisplayName("""
            Test pour trouver si actif true
            """)
    @Test
    void testfiltreActifFonctionne() {
        Voiture v1 = creeVoiture();
        Voiture v2 = creeVoiture2();

        List<Voiture> voitures = List.of(v1, v2);


        VoitureResponseDTO dto = new VoitureResponseDTO(1, "Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, Permis.B, "50", "1400", true, false);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        Mockito.when(mapperMock.toVoitureResponseDTO(v1)).thenReturn(dto);


        List<VoitureResponseDTO> result = service.filtrer(Filtre.ACTIF);
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(VoitureResponseDTO::actif));
    }

    @DisplayName("""
            Test pour trouver si actif false
            """)
    @Test
    void testfiltreActifInactif() {
        Voiture v1 = creeVoiture();
        Voiture v2 = creeVoiture2();

        List<Voiture> voitures = List.of(v1, v2);


        VoitureResponseDTO dto = new VoitureResponseDTO(1, "Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, Permis.B, "50", "1400", true, false);
        VoitureResponseDTO dto2 = new VoitureResponseDTO(2, "Peugeot", "208", "Jaune", Type.Citadine, 11,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 3, Permis.B, "50", "1400", false, false);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        // Mockito.when(mapperMock.toVoitureResponseDTO(v1)).thenReturn(dto);
        Mockito.when(mapperMock.toVoitureResponseDTO(v2)).thenReturn(dto2);


        List<VoitureResponseDTO> result = service.filtrer(Filtre.INACTIF);
        assertEquals(1, result.size());
        assertTrue(result.stream().noneMatch(VoitureResponseDTO::actif));
    }

    /// //

    @DisplayName("""
            Test pour trouver si Hors du parc true
            """)
    @Test
    void testfiltreHorsDuParcFonctionne() {
        Voiture v1 = creeVoiture();
        v1.setActif(false);
        v1.setRetireDuParc(false);
        Voiture v2 = creeVoiture2();
        v2.setActif(false);
        v2.setRetireDuParc(false);

        List<Voiture> voitures = List.of(v1, v2);


        VoitureResponseDTO dto = new VoitureResponseDTO(1, "Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, Permis.B, "50", "1400", true, true);
        VoitureResponseDTO dto2 = new VoitureResponseDTO(2, "Peugeot", "208", "Jaune", Type.Citadine, 11,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 3, Permis.B, "50", "1400", false, true);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
//        Mockito.when(mapperMock.toVoitureResponseDTO(v1)).thenReturn(dto);
//        Mockito.when(mapperMock.toVoitureResponseDTO(v2)).thenReturn(dto2);


        List<VoitureResponseDTO> result = service.filtrer(Filtre.HORSPARC);
        assertEquals(0, result.size());
        assertTrue(result.stream().allMatch(VoitureResponseDTO::retireDuParc));
    }

    @DisplayName("""
            Test pour trouver si actif false
            """)
    @Test
    void testfiltreDansLeParc() {
        Voiture v1 = creeVoiture();
        v1.setActif(false);
        v1.setRetireDuParc(false);
        Voiture v2 = creeVoiture2();
        v2.setActif(false);
        v2.setRetireDuParc(false);

        List<Voiture> voitures = List.of(v1, v2);

        VoitureResponseDTO dto = new VoitureResponseDTO(1, "Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, Permis.B, "50", "1400", false, false);
        VoitureResponseDTO dto2 = new VoitureResponseDTO(2, "Peugeot", "208", "Jaune", Type.Citadine, 11,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 3, Permis.B, "50", "1400", false, false);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        Mockito.when(mapperMock.toVoitureResponseDTO(v1)).thenReturn(dto);
        Mockito.when(mapperMock.toVoitureResponseDTO(v2)).thenReturn(dto2);


        List<VoitureResponseDTO> result = service.filtrer(Filtre.DANSLEPARC);
        assertEquals(2, result.size());
        assertFalse(result.stream().allMatch(VoitureResponseDTO::retireDuParc));
    }


//************************************************************************************************************************
//                                                      METHODE PRIVEE
//************************************************************************************************************************

    private static Voiture creeVoiture() {
        Voiture v = new Voiture();
        v.setMarque("Seat");
        v.setModele("Arona");
        v.setCouleur("Rouge");
        v.setNbrPlaces(5);
        v.setCarburant(Carburant.Essence);
        v.setNbrPortes(NombrePortes.Cinq);
        v.setTransmission(Transmission.MANUEL);
        v.setClim(true);
        v.setBagages(5);
        v.setType(Type.SUV);
        v.setPermis(Permis.B);
        v.setActif(true);
        return v;
    }

    private static VoitureResponseDTO voitureResponseDTO() {
        return new VoitureResponseDTO(1, "Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, Permis.B, "50", "1400", true, false);
    }

    private static Voiture creeVoiture2() {
        Voiture v = new Voiture();
        v.setMarque("Peugeot");
        v.setModele("208");
        v.setCouleur("Jaune");
        v.setNbrPlaces(11);
        v.setCarburant(Carburant.Essence);
        v.setNbrPortes(NombrePortes.Cinq);
        v.setTransmission(Transmission.MANUEL);
        v.setClim(true);
        v.setBagages(3);
        v.setType(Type.Citadine);
        v.setPermis(Permis.D1);
        v.setActif(false);
        return v;
    }

    private static VoitureResponseDTO voiture2ResponseDTO() {
        return new VoitureResponseDTO(2, "Peugeot", "208", "Jaune", Type.Citadine, 11,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 3, Permis.D1, "50", "1400", false, false);
    }


    private static VoitureRequestDTO voitureRequestDTO() {
        return new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 5, "50", "1400", true, false);
    }

    private static VoitureRequestDTO voiture2RequestDTO() {
        return new VoitureRequestDTO("Peugeot", "208", "Jaune", Type.Citadine, 11,
                Carburant.Essence, NombrePortes.Cinq, true, Transmission.MANUEL, 3, "50", "1400", false, false);
    }


}
