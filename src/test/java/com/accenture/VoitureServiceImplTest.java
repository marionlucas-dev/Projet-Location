package com.accenture;

import com.accenture.repository.VoitureDAO;
import com.accenture.service.VoitureServiceImpl;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testTrouverExistePas(){
        Mockito.when(daoMock.findByModele("Arona")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows (EntityNotFoundException.class, ()-> service.trouver("Arona"));
        assertEquals("Voiture non trouvé", ex.getMessage());
    }
}
