package com.example.e_commerce.service;

import com.example.e_commerce.dto.CommentaireRequestDTO;
import com.example.e_commerce.dto.CommentaireResponseDTO;
import com.example.e_commerce.entity.Commentaire;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.entity.Produit;
import com.example.e_commerce.mapper.CommentaireMapper;
import com.example.e_commerce.repository.CommentaireRepository;
import com.example.e_commerce.repository.UserRepository;
import com.example.e_commerce.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final UserRepository userRepository;
    private final ProduitRepository produitRepository;
    private final CommentaireMapper commentaireMapper;

    public CommentaireResponseDTO createCommentaire(CommentaireRequestDTO dto) {
        // Valider que l'utilisateur existe
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + dto.getUserId()));
        
        // Valider que le produit existe
        Produit produit = produitRepository.findById(dto.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + dto.getProduitId()));
        
        // Convertir DTO -> Entity
        Commentaire commentaire = commentaireMapper.toEntity(dto);
        commentaire.setUser(user);
        commentaire.setProduit(produit);
        
        Commentaire savedCommentaire = commentaireRepository.save(commentaire);
        return commentaireMapper.toDTO(savedCommentaire);
    }

    @Transactional(readOnly = true)
    public CommentaireResponseDTO getCommentaireById(Long id) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé avec l'ID: " + id));
        return commentaireMapper.toDTO(commentaire);
    }

    @Transactional(readOnly = true)
    public List<CommentaireResponseDTO> getAllCommentaires() {
        return commentaireRepository.findAll().stream()
                .map(commentaireMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CommentaireResponseDTO updateCommentaire(Long id, CommentaireRequestDTO dto) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé avec l'ID: " + id));
        
        // Mettre à jour les champs
        commentaire.setContenu(dto.getContenu());
        commentaire.setNote(dto.getNote());
        
        // Mettre à jour les relations si nécessaire
        if (!commentaire.getUser().getId().equals(dto.getUserId())) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            commentaire.setUser(user);
        }
        
        if (!commentaire.getProduit().getId().equals(dto.getProduitId())) {
            Produit produit = produitRepository.findById(dto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
            commentaire.setProduit(produit);
        }
        
        Commentaire updatedCommentaire = commentaireRepository.save(commentaire);
        return commentaireMapper.toDTO(updatedCommentaire);
    }

    public void deleteCommentaire(Long id) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé avec l'ID: " + id));
        commentaireRepository.delete(commentaire);
    }

    // Méthodes supplémentaires
    @Transactional(readOnly = true)
    public List<CommentaireResponseDTO> getCommentairesByProduit(Long produitId) {
        return commentaireRepository.findByProduitId(produitId).stream()
                .map(commentaireMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentaireResponseDTO> getCommentairesByUser(Long userId) {
        return commentaireRepository.findByUserId(userId).stream()
                .map(commentaireMapper::toDTO)
                .collect(Collectors.toList());
    }
}