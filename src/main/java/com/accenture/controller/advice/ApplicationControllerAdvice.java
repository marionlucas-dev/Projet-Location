package com.accenture.controller.advice;
import com.accenture.exception.MotoException;
import com.accenture.exception.VoitureException;
import com.accenture.shared.ErreurReponse;
import com.accenture.exception.AdministrateurException;
import com.accenture.exception.ClientException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErreurReponse> gestionClientException(ClientException ex) {
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Erreur fonctionnelle ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErreurReponse> entityNotFoundException(EntityNotFoundException ex) {
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Mauvaise requête ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }

    @ExceptionHandler(AdministrateurException.class)
    public ResponseEntity<ErreurReponse> gestionAdminException(AdministrateurException ex) {
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Erreur fonctionnelle ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(VoitureException.class)
    public ResponseEntity<ErreurReponse> gestionVoitureException(VoitureException ex) {
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Erreur fonctionnelle ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler (MotoException.class)
    public ResponseEntity<ErreurReponse> gestionMotoException(VoitureException ex) {
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Erreur fonctionnelle ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

}
