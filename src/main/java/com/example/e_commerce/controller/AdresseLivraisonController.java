package com.example.e_commerce.controller;

import com.example.e_commerce.dto.AdresseLivraisonRequestDTO;
import com.example.e_commerce.dto.AdresseLivraisonResponseDTO;
import com.example.e_commerce.service.AdresseLivraisonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adresses-livraison")
@RequiredArgsConstructor
public class AdresseLivraisonController {

    private final AdresseLivraisonService adresseLivraisonService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<AdresseLivraisonResponseDTO> creerAdresse(
            @PathVariable Long userId,
            @RequestBody @Valid AdresseLivraisonRequestDTO requestDTO) {
        AdresseLivraisonResponseDTO adresse = adresseLivraisonService.creerAdresse(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(adresse);
    }

    @PutMapping("/{adresseId}")
    public ResponseEntity<AdresseLivraisonResponseDTO> modifierAdresse(
            @PathVariable Long adresseId,
            @RequestBody @Valid AdresseLivraisonRequestDTO requestDTO) {
        AdresseLivraisonResponseDTO adresse = adresseLivraisonService.modifierAdresse(adresseId, requestDTO);
        return ResponseEntity.ok(adresse);
    }

    @DeleteMapping("/{adresseId}")
    public ResponseEntity<Void> supprimerAdresse(@PathVariable Long adresseId) {
        adresseLivraisonService.supprimerAdresse(adresseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{adresseId}")
    public ResponseEntity<AdresseLivraisonResponseDTO> getAdresse(@PathVariable Long adresseId) {
        AdresseLivraisonResponseDTO adresse = adresseLivraisonService.getAdresseById(adresseId);
        return ResponseEntity.ok(adresse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AdresseLivraisonResponseDTO>> getAdressesByUser(@PathVariable Long userId) {
        List<AdresseLivraisonResponseDTO> adresses = adresseLivraisonService.getAdressesByUser(userId);
        return ResponseEntity.ok(adresses);
    }

    @GetMapping("/user/{userId}/par-defaut")
    public ResponseEntity<AdresseLivraisonResponseDTO> getAdresseParDefaut(@PathVariable Long userId) {
        AdresseLivraisonResponseDTO adresse = adresseLivraisonService.getAdresseParDefaut(userId);
        if (adresse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adresse);
    }

    @PatchMapping("/{adresseId}/par-defaut")
    public ResponseEntity<AdresseLivraisonResponseDTO> definirCommeParDefaut(@PathVariable Long adresseId) {
        AdresseLivraisonResponseDTO adresse = adresseLivraisonService.definirCommeParDefaut(adresseId);
        return ResponseEntity.ok(adresse);
    }
}
