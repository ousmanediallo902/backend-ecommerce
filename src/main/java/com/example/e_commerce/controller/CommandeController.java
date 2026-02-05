package com.example.e_commerce.controller;


import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.e_commerce.dto.CommandeRequestDTO;
import com.example.e_commerce.dto.CommandeResponseDTO;
import com.example.e_commerce.entity.enums.StatutCommande;
import com.example.e_commerce.service.CommandeService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeResponseDTO> creerCommande(
            @RequestBody @Valid CommandeRequestDTO commandeRequest) {
        CommandeResponseDTO commande = commandeService.creerCommandeComplete(commandeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commande);
    }

    // ==================== ENDPOINTS GET ====================

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> getCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.getCommandeById(id));
    }

    @GetMapping
    public ResponseEntity<List<CommandeResponseDTO>> getAllCommandes() {
        return ResponseEntity.ok(commandeService.getAllCommandes());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommandeResponseDTO>> getCommandesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(commandeService.getCommandesByUser(userId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<CommandeResponseDTO>> getCommandesByStatut(@PathVariable StatutCommande statut) {
        return ResponseEntity.ok(commandeService.getCommandesByStatut(statut));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CommandeResponseDTO>> getCommandesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(commandeService.getCommandesByDateRange(startDate, endDate));
    }

    @GetMapping("/stats/chiffre-affaires")
    public ResponseEntity<BigDecimal> getChiffreAffairesTotal() {
        return ResponseEntity.ok(commandeService.getChiffreAffairesTotal());
    }

    @GetMapping("/stats/chiffre-affaires/user/{userId}")
    public ResponseEntity<BigDecimal> getChiffreAffairesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(commandeService.getChiffreAffairesByUser(userId));
    }

    @GetMapping("/stats/count/user/{userId}")
    public ResponseEntity<Long> countCommandesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(commandeService.countCommandesByUser(userId));
    }

    // ==================== ENDPOINTS UPDATE STATUT ====================

    @PatchMapping("/{id}/statut")
    public ResponseEntity<CommandeResponseDTO> updateStatut(
            @PathVariable Long id,
            @RequestParam StatutCommande statut) {
        return ResponseEntity.ok(commandeService.updateStatut(id, statut));
    }

    @PatchMapping("/{id}/statut/payee")
    public ResponseEntity<CommandeResponseDTO> marquerCommePayee(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.updateStatut(id, StatutCommande.PAYEE));
    }

    @PatchMapping("/{id}/statut/en-livraison")
    public ResponseEntity<CommandeResponseDTO> marquerEnLivraison(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.updateStatut(id, StatutCommande.EN_LIVRAISON));
    }

    @PatchMapping("/{id}/statut/livree")
    public ResponseEntity<CommandeResponseDTO> marquerCommeLivree(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.updateStatut(id, StatutCommande.LIVREE));
    }

    @PatchMapping("/{id}/statut/annulee")
    public ResponseEntity<CommandeResponseDTO> annulerCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.updateStatut(id, StatutCommande.ANNULEE));
    }
}