package com.example.e_commerce.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.e_commerce.entity.Categorie;
import com.example.e_commerce.entity.Produit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // 🔍 Recherche par nom
    List<Produit> findByNomContainingIgnoreCase(String nom);

    // 🔍 Trouver un produit avec sa catégorie (évite LazyInitializationException)
    @Query("SELECT p FROM Produit p LEFT JOIN FETCH p.categorie WHERE p.id = :id")
    Optional<Produit> findByIdWithCategorie(@Param("id") Long id);

    // 🔍 Récupérer tous les produits avec leur catégorie
    @Query("SELECT p FROM Produit p LEFT JOIN FETCH p.categorie")
    List<Produit> findAllWithCategorie();

    // 🔍 Produits par id de catégorie (avec EntityGraph)
    @EntityGraph(attributePaths = {"categorie"})
    List<Produit> findByCategorieId(Long categorieId);

    // 🔍 Produits par objet Categorie
    List<Produit> findByCategorie(Categorie categorie);

    // 🔍 Produits par nom de catégorie
    @EntityGraph(attributePaths = {"categorie"})
    @Query("SELECT p FROM Produit p WHERE p.categorie.nom = :categorieNom")
    List<Produit> findByCategorieNom(@Param("categorieNom") String categorieNom);

    // 🔍 Produits dans une plage de prix
    List<Produit> findByPrixBetween(BigDecimal minPrix, BigDecimal maxPrix);

    // 🔍 Produits en stock
    List<Produit> findByStockGreaterThan(int stock);

    // 🔍 Recherche par nom ou description
    List<Produit> findByNomContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nom, String description);

    // 📊 Compter produits par catégorie (objet)
    Long countByCategorie(Categorie categorie);

    // 📊 Compter produits par nom de catégorie
    Long countByCategorieNom(String nom);

    // 📊 Stock total
    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM Produit p")
    Integer sumStock();
}
