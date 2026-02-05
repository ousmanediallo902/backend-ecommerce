package com.example.e_commerce.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LigneCommandeRequestDTO {
   @NotNull
    private Long produitId;
    
    @Min(1)
    private int quantite; 
}
