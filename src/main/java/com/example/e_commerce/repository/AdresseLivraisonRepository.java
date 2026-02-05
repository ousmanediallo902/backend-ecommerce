package com.example.e_commerce.repository;

import com.example.e_commerce.entity.AdresseLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdresseLivraisonRepository extends JpaRepository<AdresseLivraison, Long> {
    
    List<AdresseLivraison> findByUserId(Long userId);
    
    Optional<AdresseLivraison> findByUserIdAndParDefaut(Long userId, boolean parDefaut);
    
    List<AdresseLivraison> findByUserIdAndParDefautTrue(Long userId);
}
