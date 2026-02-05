package com.example.e_commerce.dto;

import com.example.e_commerce.entity.enums.StatutCommande;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandeResponseDTO {
    private Long id;
    private LocalDateTime dateCommande;
    private StatutCommande statut;
    private BigDecimal total;
    private Long userId;
    private Long factureId;
    private Long paiementId; // ← Nouveau champ
    private String referencePaiement; // ← Nouveau champ
    private AdresseLivraisonResponseDTO adresseLivraison;
    private List<LigneCommandeResponseDTO> lignes;
}