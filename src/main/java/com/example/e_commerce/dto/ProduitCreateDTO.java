package com.example.e_commerce.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitCreateDTO {
    private String nom;
    private String description;
    private BigDecimal prix;
    private int stock;
    private Long categorieId; // seulement l'ID
}
