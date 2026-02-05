package com.example.e_commerce.service;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.e_commerce.dto.ProduitCreateDTO;
import com.example.e_commerce.dto.ProduitResponseDTO;

public interface ProduitService {
    ProduitResponseDTO createProduit(ProduitCreateDTO dto, MultipartFile image)throws IOException;
    ProduitResponseDTO updateProduit(Long id, ProduitCreateDTO dto, MultipartFile image) throws IOException;
    void deleteProduit(Long id);
    ProduitResponseDTO getProduitById(Long id) ;
    List<ProduitResponseDTO> getAllProduits();
    String saveImage(MultipartFile image) throws IOException;

    List<ProduitResponseDTO> searchProduits(String keyword);
    List<ProduitResponseDTO> getProduitsByCategorieId(Long categorieId);   
    List<ProduitResponseDTO> getProduitsByPrixRange(
            @RequestParam BigDecimal minPrix,
            @RequestParam BigDecimal maxPrix);   
    List<ProduitResponseDTO> getProduitsEnStock() ;
    Integer getTotalStock();
   // List<ProduitResponseDTO> getProduitsEnPromotion();
    Long countProduits();
    Long countProduitsByCategorie(String categorie);
    List<ProduitResponseDTO> getProduitsByCategorieNom(String categorieNom);
    

    
    
}

