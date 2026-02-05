package com.example.e_commerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentaireRequestDTO {
    
    @NotBlank(message = "Le contenu ne peut pas être vide")
    private String contenu;
    
    private LocalDateTime dateCommentaire;
    
    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note doit être au moins 1")
    @Max(value = 5, message = "La note ne peut pas dépasser 5")
    private Integer note;
    
    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long userId;
    
    @NotNull(message = "L'ID produit est obligatoire")
    private Long produitId;
}