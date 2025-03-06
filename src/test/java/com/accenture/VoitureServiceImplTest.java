package com.accenture;

import com.accenture.exception.VoitureException;
import com.accenture.repository.VoitureDAO;
import com.accenture.repository.entity.vehicules.Voiture;
import com.accenture.service.VoitureServiceImpl;
import com.accenture.service.dto.vehicules.VoitureRequestDTO;
import com.accenture.service.dto.vehicules.VoitureResponseDTO;
import com.accenture.service.mapper.VoitureMapper;
import com.accenture.shared.enumerations.*;
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

    @DisplayName("Doit lever une exception EntityNotFoundException lorsque la voiture n'existe pas dans la base de données")
    @Test
    void testTrouverExistePas() {
        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver(1L));
        assertEquals("Voiture non trouvé", ex.getMessage());
    }


    @DisplayName("Doit renvoyer un VoitureResponseDTO lorsque la voiture existe dans la base de données")
    @Test
    void testTrouverExiste() {
        Voiture v = creeVoiture();
        Optional<Voiture> optVoiture = Optional.of(v);
        Mockito.when(daoMock.findById(1L)).thenReturn(optVoiture);

        VoitureResponseDTO dto = voitureResponseDTO();
        Mockito.when(mapperMock.toVoitureResponseDTO(v)).thenReturn(dto);

        assertSame(dto, service.trouver(1L));
    }


    @DisplayName("Doit renvoyer une liste de VoitureResponseDTO correspondant aux voitures existantes dans la base de données")
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


    @DisplayName("Doit lever une exception VoitureException si l'on tente d'ajouter un objet null")
    @Test
    void testAjouter() {
        assertThrows(VoitureException.class, () -> service.ajouter(null));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque la marque de la voiture est null")
    @Test
    void testAjouterMarqueNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO(null, "Arona", "rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque la marque de la voiture est vide")
    @Test
    void testAjouterMarqueVide() {
        VoitureRequestDTO dto = new VoitureRequestDTO("\t", "Arona", "rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Doit lever une exception VoitureException lorsque le modèle de la voiture est null")
    @Test
    void testAjouterModeleNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", null, "rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le modèle de la voiture est vide")
    @Test
    void testAjouterModeleVide() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "\n", "rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Doit lever une exception VoitureException lorsque la couleur de la voiture est null")
    @Test
    void testAjouterCouleurNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", null, Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque la couleur de la voiture est vide")
    @Test
    void testAjouterCouleurVide() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "\n", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Doit lever une exception et vérifier le permis en fonction du nombre de places")
    @Test
    void testAjouterNombreDePlaces() {
        VoitureResponseDTO voiture = new VoitureResponseDTO(1, "Seat", "Arona", "rouge", Type.SUV,
                12, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, Permis.D1, 50, 1400, true, false);
        assertEquals(Permis.D1, voiture.permis());
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le carburant est null")
    @Test
    void testAjouterCarburantNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                5, null, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le nombre de portes est différent de 3 ou 5")
    @Test
    void testAjouterNombreDePorteInvalide() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                7, Carburant.ESSENCE, null, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque la transmission est null")
    @Test
    void testAjouterTransmissionNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, null, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le climatiseur est null")
    @Test
    void testAjouterClimNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, null, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le nombre de bagages est inférieur à 0")
    @Test
    void testAjouterNombreBagageInf0() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, -5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le nombre de bagages est égal à 0")
    @Test
    void testAjouterNombreBagageEgale0() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                0, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 0, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le nombre de bagages est supérieur à 17")
    @Test
    void testAjouterNombreBagageSup17() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                20, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 0, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Doit lever une exception VoitureException lorsque le type est null")
    @Test
    void testAjouterTypeNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", null,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, -5, 50, 1400, true, false);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("Lors de l'ajout d'un VoitureRequestDTO valide, le service appelle save et renvoie un VoitureResponseDTO")
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



    @DisplayName("Lors de l'ajout d'un VoitureRequestDTO valide avec plus de 10 personnes, le service appelle save et renvoie un VoitureResponseDTO")
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

    @DisplayName("Test de suppression d'une voiture, avec succès lorsque la voiture est trouvée")
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

    @DisplayName("Lancer une exception EntityNotFoundException si l'ID n'existe pas")
    @Test
    void modifierSiIdNonPresent() {
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.modifier(1, voitureRequestDTO()));
        assertEquals("ID incorrect", ex.getMessage());
    }


    @DisplayName("Modifier une voiture existante avec succès")
    @Test
    void modifierVoitureExistant() {
        Voiture voitureExistant = new Voiture();
        voitureExistant.setId(1);
        voitureExistant.setRetireDuParc(false);

        VoitureRequestDTO voitureRequestDTO = voitureRequestDTO();

        Voiture voitureModifie = new Voiture();
        voitureModifie.setId(2);
        voitureModifie.setModele("Clio");
        voitureModifie.setType(Type.LUXE);

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
        VoitureRequestDTO voitureRequestDTO = new VoitureRequestDTO(
                "Seat", null, "rouge", Type.SUV, 5, Carburant.ESSENCE, NombrePortes.CINQ,
                true, Transmission.MANUEL, 5, 50, 1400, true, false
        );

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

        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistant));
        Mockito.when(mapperMock.toVoiture(voitureRequestDTO)).thenReturn(voitureModifie);

        // Lancement du test et vérification de l'exception
        assertThrows(VoitureException.class, () -> service.modifier(1L, voitureRequestDTO));

        // Vérification que la voiture existante n'a pas été modifiée
        assertEquals("Arona", voitureExistant.getModele());
    }


    @DisplayName("Modifier une voiture avec une marque null, exception levée")
    @Test
    void testModifierMarqueNull() {
        // Création du DTO avec Marque null
        VoitureRequestDTO dto = new VoitureRequestDTO(null, "Arona", "rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);

        // Assurez-vous que l'exception levée est celle que vous attendez
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("Modifier une voiture avec un modèle null, exception levée")

    @Test
    void testModifierModeleNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", null, "rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);

        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }

    @DisplayName("Modifier une voiture avec une couleur null, exception levée")
    @Test
    void testModifierCouleurNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", null, Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("Modifier une voiture avec un carburant null, exception levée")
    @Test
    void testModifierCarburantNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                5, null, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("Modifier une voiture avec un nombre de portes différent de 3 ou 5, exception levée")
    @Test
    void testModifierNombreDePorteNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "rouge", Type.SUV,
                7, Carburant.ESSENCE, null, true, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("Modifier une voiture avec transmission null, exception levée")
    @Test
    void testModifierTransmissionNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, null, 5, 50, 1400, true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("Modifier une voiture avec climatisation null, exception levée")
    @Test
    void testModifierClimNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV,
                5, Carburant.ESSENCE, NombrePortes.CINQ, null, Transmission.MANUEL, 5, 50, 1400, true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("Modifier une voiture avec type null, exception levée")
    @Test
    void testModifierTypeNull() {
        VoitureRequestDTO dto = new VoitureRequestDTO("Seat", "Arona", "Rouge", null,
                5, Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, -5, 50, 1400, true, false);
        assertThrows(EntityNotFoundException.class, () -> service.modifier(1L, dto));
    }


    @DisplayName("""
            Modifier tous les attributs d'une voiture sauf la marque,
            et renvoyer une VoitureResponseDTO avec toutes les modifications appliquées
            """)
    @Test
    void testModificationToutSaufMarque() {
        VoitureRequestDTO requestDto = new VoitureRequestDTO("Fiat", "500", "rose", Type.CITADINE,
                4, Carburant.ESSENCE, NombrePortes.TROIS, true, Transmission.MANUEL, 3,
                20, 30000, true, false);
        Voiture nouvelleVoiture = nouvelleVoitureTestModifier();


        Voiture vraieVoiture = creeVoiture();
        Voiture voitureRemplace = VoitureMethodeModifier();

        VoitureResponseDTO responseDto = new VoitureResponseDTO(1,
                "Renault", "Twingo", "rose", Type.SUV, 7, Carburant.DIESEL, NombrePortes.CINQ, false, Transmission.AUTO, 11,
                Permis.B1, 100, 4, false, true);


        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.of(vraieVoiture));
        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(nouvelleVoiture);
        Mockito.when(mapperMock.toVoitureResponseDTO(voitureRemplace)).thenReturn(responseDto);
        Mockito.when(mapperMock.toVoitureRequestDTO(vraieVoiture)).thenReturn(requestDto);
        Mockito.when(daoMock.save(vraieVoiture)).thenReturn(voitureRemplace);

        Mockito.when(mapperMock.toVoitureResponseDTO(voitureRemplace)).thenReturn(responseDto);
        assertEquals(responseDto, service.modifier(1, requestDto));
        Mockito.verify(daoMock, Mockito.times(1)).save(vraieVoiture);
        assertEquals("Renault", vraieVoiture.getMarque());
        assertEquals("Twingo", vraieVoiture.getModele());
        assertEquals("rose", vraieVoiture.getCouleur());
        assertEquals(NombrePortes.CINQ, vraieVoiture.getNbrPortes());
        assertEquals(5, vraieVoiture.getNbrPlaces());
        assertEquals(Carburant.DIESEL, vraieVoiture.getCarburant());
        assertEquals(Type.SUV, vraieVoiture.getType());
        assertEquals(Transmission.AUTO, vraieVoiture.getTransmission());
        assertEquals(false, vraieVoiture.getClim());
        assertEquals(5, vraieVoiture.getBagages());
        assertEquals(Permis.B, vraieVoiture.getPermis());
    }


//************************************************************************************************************************
//                                                  METHODE Filtre
//************************************************************************************************************************

    @DisplayName("Filtrer les voitures actives (actif=true) et vérifier que le résultat contient uniquement des voitures actives")
    @Test
    void testfiltreActifFonctionne() {
        Voiture v1 = creeVoiture();
        Voiture v2 = creeVoiture2();

        List<Voiture> voitures = List.of(v1, v2);


        VoitureResponseDTO dto = new VoitureResponseDTO(1, "Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, Permis.B, 50, 1400, true, false);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        Mockito.when(mapperMock.toVoitureResponseDTO(v1)).thenReturn(dto);


        List<VoitureResponseDTO> result = service.filtrer(Filtre.ACTIF);
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(VoitureResponseDTO::actif));
    }


    @DisplayName("Filtrer les voitures inactives (actif=false) et vérifier que le résultat contient uniquement des voitures inactives")
    @Test
    void testfiltreActifInactif() {
        Voiture v1 = creeVoiture();
        Voiture v2 = creeVoiture2();

        List<Voiture> voitures = List.of(v1, v2);
        VoitureResponseDTO dto2 = new VoitureResponseDTO(2, "Peugeot", "208", "Jaune", Type.CITADINE, 11,
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 3, Permis.B, 50, 1400, false, false);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        Mockito.when(mapperMock.toVoitureResponseDTO(v2)).thenReturn(dto2);


        List<VoitureResponseDTO> result = service.filtrer(Filtre.INACTIF);
        assertEquals(1, result.size());
        assertTrue(result.stream().noneMatch(VoitureResponseDTO::actif));
    }

    @DisplayName("Filtrer les voitures retirées du parc (retireDuParc=true) et vérifier que le résultat contient uniquement des voitures retirées du parc")
    @Test
    void testfiltreHorsDuParcFonctionne() {
        Voiture v1 = creeVoiture();
        v1.setActif(false);
        v1.setRetireDuParc(false);
        Voiture v2 = creeVoiture2();
        v2.setActif(false);
        v2.setRetireDuParc(false);
        List<Voiture> voitures = List.of(v1, v2);
        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        List<VoitureResponseDTO> result = service.filtrer(Filtre.HORSPARC);
        assertEquals(0, result.size());
        assertTrue(result.stream().allMatch(VoitureResponseDTO::retireDuParc));
    }


    @DisplayName("Filtrer les voitures dans le parc (retireDuParc=false) et vérifier que le résultat contient uniquement des voitures non retirées du parc")
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
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, Permis.B, 50, 1400, false, false);
        VoitureResponseDTO dto2 = new VoitureResponseDTO(2, "Peugeot", "208", "Jaune", Type.CITADINE, 11,
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 3, Permis.B, 50, 1400, false, false);

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
        v.setCarburant(Carburant.ESSENCE);
        v.setNbrPortes(NombrePortes.CINQ);
        v.setTransmission(Transmission.MANUEL);
        v.setClim(true);
        v.setBagages(5);
        v.setType(Type.SUV);
        v.setPermis(Permis.B);
        v.setActif(true);
        v.setRetireDuParc(false);
        return v;
    }

    private static VoitureResponseDTO voitureResponseDTO() {
        return new VoitureResponseDTO(1, "Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, Permis.B, 50, 1400, true, false);
    }

    private static Voiture creeVoiture2() {
        Voiture v = new Voiture();
        v.setMarque("Peugeot");
        v.setModele("208");
        v.setCouleur("Jaune");
        v.setNbrPlaces(11);
        v.setCarburant(Carburant.ESSENCE);
        v.setNbrPortes(NombrePortes.CINQ);
        v.setTransmission(Transmission.MANUEL);
        v.setClim(true);
        v.setBagages(3);
        v.setType(Type.CITADINE);
        v.setPermis(Permis.D1);
        v.setActif(false);
        return v;
    }

    private static VoitureResponseDTO voiture2ResponseDTO() {
        return new VoitureResponseDTO(2, "Peugeot", "208", "Jaune", Type.CITADINE, 11,
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 3, Permis.D1, 50, 1400, false, false);
    }


    private static VoitureRequestDTO voitureRequestDTO() {
        return new VoitureRequestDTO("Seat", "Arona", "Rouge", Type.SUV, 5,
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 5, 50, 1400, true, false);
    }

    private static VoitureRequestDTO voiture2RequestDTO() {
        return new VoitureRequestDTO("Peugeot", "208", "Jaune", Type.CITADINE, 11,
                Carburant.ESSENCE, NombrePortes.CINQ, true, Transmission.MANUEL, 3, 50, 1400, false, false);
    }

    private static Voiture nouvelleVoitureTestModifier() {
        Voiture nouvelleVoiture = new Voiture();
        nouvelleVoiture.setMarque("Renault");
        nouvelleVoiture.setModele("Twingo");
        nouvelleVoiture.setCouleur("rose");
        nouvelleVoiture.setNbrPortes(NombrePortes.CINQ);
        nouvelleVoiture.setNbrPlaces(7);
        nouvelleVoiture.setCarburant(Carburant.DIESEL);
        nouvelleVoiture.setType(Type.SUV);
        nouvelleVoiture.setTransmission(Transmission.AUTO);
        nouvelleVoiture.setClim(false);
        nouvelleVoiture.setBagages(11);
        nouvelleVoiture.setPermis(Permis.B);
        nouvelleVoiture.setTarifJournee(100);
        nouvelleVoiture.setKilometrage(4);
        nouvelleVoiture.setRetireDuParc(false);
        nouvelleVoiture.setActif(false);
        return nouvelleVoiture;
    }


    private static Voiture VoitureMethodeModifier() {
        Voiture voitureRemplace = creeVoiture();
        voitureRemplace.setModele("Twingo");
        voitureRemplace.setCouleur("rose");
        voitureRemplace.setNbrPortes(NombrePortes.CINQ);
        voitureRemplace.setNbrPlaces(7);
        voitureRemplace.setCarburant(Carburant.DIESEL);
        voitureRemplace.setType(Type.SUV);
        voitureRemplace.setTransmission(Transmission.AUTO);
        voitureRemplace.setClim(false);
        voitureRemplace.setBagages(11);
        voitureRemplace.setPermis(Permis.B1);
        voitureRemplace.setTarifJournee(100);
        voitureRemplace.setKilometrage(4);
        voitureRemplace.setRetireDuParc(true);
        voitureRemplace.setActif(false);
        return voitureRemplace;
    }


}
