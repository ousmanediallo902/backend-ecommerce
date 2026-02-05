package com.example.e_commerce.controller;

import com.example.e_commerce.dto.CommentaireRequestDTO;
import com.example.e_commerce.dto.CommentaireResponseDTO;
import com.example.e_commerce.service.CommentaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commentaires")
@RequiredArgsConstructor
public class CommentaireController {

    private final CommentaireService commentaireService;

    /**
     * Créer un nouveau commentaire
     */
    @PostMapping
    public ResponseEntity<CommentaireResponseDTO> createCommentaire(@RequestBody CommentaireRequestDTO commentaireRequest) {
        CommentaireResponseDTO createdCommentaire = commentaireService.createCommentaire(commentaireRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentaire);
    }

    /**
     * Récupérer un commentaire par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentaireResponseDTO> getCommentaire(@PathVariable Long id) {
        CommentaireResponseDTO commentaire = commentaireService.getCommentaireById(id);
        return ResponseEntity.ok(commentaire);
    }

    /**
     * Récupérer tous les commentaires
     */
    @GetMapping
    public ResponseEntity<List<CommentaireResponseDTO>> getAllCommentaires() {
        List<CommentaireResponseDTO> commentaires = commentaireService.getAllCommentaires();
        return ResponseEntity.ok(commentaires);
    }

    /**
     * Mettre à jour un commentaire
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentaireResponseDTO> updateCommentaire(
            @PathVariable Long id, 
            @RequestBody CommentaireRequestDTO commentaireRequest) {
        CommentaireResponseDTO updatedCommentaire = commentaireService.updateCommentaire(id, commentaireRequest);
        return ResponseEntity.ok(updatedCommentaire);
    }

    /**
     * Supprimer un commentaire
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        commentaireService.deleteCommentaire(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer les commentaires d'un produit spécifique
     */
    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<CommentaireResponseDTO>> getCommentairesByProduit(@PathVariable Long produitId) {
        List<CommentaireResponseDTO> commentaires = commentaireService.getCommentairesByProduit(produitId);
        return ResponseEntity.ok(commentaires);
    }

    /**
     * Récupérer les commentaires d'un utilisateur spécifique
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentaireResponseDTO>> getCommentairesByUser(@PathVariable Long userId) {
        List<CommentaireResponseDTO> commentaires = commentaireService.getCommentairesByUser(userId);
        return ResponseEntity.ok(commentaires);
    }
}