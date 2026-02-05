package com.example.e_commerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LigneCommandeResponseDTO {
    private Long produitId;
    private String produitNom;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;
}