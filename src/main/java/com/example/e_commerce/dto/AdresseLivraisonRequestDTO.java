package com.example.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdresseLivraisonRequestDTO {
    
    @NotBlank(message = "L'adresse complète est obligatoire")
    private String adresseComplete;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^(\\+221)?(70|76|77|78)[0-9]{7}$",
            message = "Numéro de téléphone sénégalais invalide")
    private String telephone;

    private String instructionsLivraison;

    private boolean parDefaut = false;
}
