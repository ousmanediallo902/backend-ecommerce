package com.example.e_commerce.dto;

import java.util.List;

import com.example.e_commerce.entity.enums.ModePaiement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CommandeRequestDTO {
   @NotNull
    private Long userId;
    
    @NotNull
    private ModePaiement modePaiement;
    
    private Long adresseLivraisonId; // Peut être null, dans ce cas on utilise l'adresse par défaut
    
    @Valid
    @NotNull
    private List<LigneCommandeRequestDTO> lignes;
}
