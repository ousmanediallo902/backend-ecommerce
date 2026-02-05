package com.example.e_commerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.e_commerce.entity.Commande;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.entity.enums.StatutCommande;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByUser(User user);
     // Méthodes de recherche
    List<Commande> findByUserId(Long userId);
    List<Commande> findByStatut(StatutCommande statut);
    List<Commande> findByDateCommandeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Méthodes de statistiques
    @Query("SELECT COUNT(c) FROM Commande c WHERE c.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Commande c")
    BigDecimal calculateTotalRevenue();
    
    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Commande c WHERE c.user.id = :userId")
    BigDecimal calculateRevenueByUser(@Param("userId") Long userId);
    
    // Méthode avec jointure pour optimiser les performances
    @Query("SELECT c FROM Commande c LEFT JOIN FETCH c.lignes LEFT JOIN FETCH c.facture WHERE c.id = :id")
    Optional<Commande> findByIdWithDetails(@Param("id") Long id);
}
