package com.accenture.controller;

import com.accenture.service.VehiculeService;
import com.accenture.service.dto.vehicules.VehiculeDTO;
import com.accenture.shared.Filtre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/vehicules")
@Slf4j
public class VehiculesController {

private final VehiculeService vehiculeService;


    public VehiculesController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

 @GetMapping
 VehiculeDTO vehicules(){
        return vehiculeService.trouverTous();
 }

 @GetMapping("/{id}")
    ResponseEntity<VehiculeDTO> unVehicule(@PathVariable ("id") String modele){
        VehiculeDTO trouver = vehiculeService.trouver(modele);
        return ResponseEntity.ok(trouver);
 }

 @GetMapping("/filtre")
    ResponseEntity<List<VehiculeDTO>> filtrer (@RequestParam Filtre filtre){
        VehiculeDTO filtrer = vehiculeService.filtrer(filtre);
        return ResponseEntity.ok(Collections.singletonList(filtrer));
 }








}
