package com.example.e_commerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.e_commerce.entity.Commande;
import com.example.e_commerce.entity.Facture;

import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    Optional<Facture> findByCommande(Commande commande);

     @Query("SELECT f FROM Facture f " +
       "JOIN FETCH f.commande c " +
       "JOIN FETCH c.user " +
       "LEFT JOIN FETCH c.lignes l " +
       "LEFT JOIN FETCH l.produit " +
       "WHERE f.id = :id")
Optional<Facture> findByIdWithCommandeAndUser(@Param("id") Long id);

}