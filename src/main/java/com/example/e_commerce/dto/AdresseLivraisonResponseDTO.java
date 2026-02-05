package com.example.e_commerce.dto;

import lombok.Data;

@Data
public class AdresseLivraisonResponseDTO {
    
    private Long id;
    private String adresseComplete;
    private String ville;
    private String telephone;
    private String instructionsLivraison;
    private boolean parDefaut;
    private Long userId;
}
