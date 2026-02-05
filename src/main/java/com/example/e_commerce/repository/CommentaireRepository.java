package com.example.e_commerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.e_commerce.entity.Commentaire;


import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByProduitId(Long produitId);
    
    // Trouver tous les commentaires d'un utilisateur
    List<Commentaire> findByUserId(Long userId);
}