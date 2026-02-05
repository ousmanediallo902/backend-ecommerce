package com.example.e_commerce.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentaireResponseDTO {
    private Long id;
    private String contenu;
    private LocalDateTime dateCommentaire;
    private Integer note;
    
    // User info (flat structure)
    private Long userId;
    private String userNom;
    private String userEmail;
    
    // Produit info (flat structure)
    private Long produitId;
    private String produitNom;
    private Double produitPrix;
}