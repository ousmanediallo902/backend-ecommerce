package com.example.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitResponseDTO {
    private Integer id;
    private String nom;
    private String description;
    private double prix;
    private int stock;
    private String imageUrl;
    private Integer categorieId;
    private String categorieNom;
}

