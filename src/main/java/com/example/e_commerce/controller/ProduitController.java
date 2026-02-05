package com.example.e_commerce.controller;



import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.e_commerce.dto.ProduitCreateDTO;
import com.example.e_commerce.dto.ProduitResponseDTO;
import com.example.e_commerce.mapper.ProduitMapper;
import com.example.e_commerce.service.ProduitService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;
    private final ObjectMapper mapper;
    private final ProduitMapper produitMapper;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProduitResponseDTO> createProduit(
            @RequestPart("produit") String produitJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {

        ProduitCreateDTO dto = mapper.readValue(produitJson, ProduitCreateDTO.class);
        ProduitResponseDTO saved = produitService.createProduit(dto, image);
        return ResponseEntity.ok(saved);
    }


// Récupérer un produit par ID
@GetMapping("/{id}")
public ResponseEntity<ProduitResponseDTO> getProduitById(@PathVariable Long id) {
    ProduitResponseDTO produit = produitService.getProduitById(id);
    return ResponseEntity.ok(produit);
}





@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ProduitResponseDTO> updateProduit(
        @PathVariable Long id,
        @RequestPart("produit") ProduitCreateDTO dto,
        @RequestPart(value = "image", required = false) MultipartFile image
) throws IOException {
    ProduitResponseDTO updated = produitService.updateProduit(id, dto, image);
    return ResponseEntity.ok(updated);
}






     @GetMapping
    public List<ProduitResponseDTO> getProduits() {
        return produitService.getAllProduits();
    }

   

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }




     // ✅ BONNE MÉTHODE - Par ID de catégorie
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<ProduitResponseDTO>> getProduitsByCategorieId(@PathVariable Long categorieId) {
        List<ProduitResponseDTO> produits = produitService.getProduitsByCategorieId(categorieId);
        return ResponseEntity.ok(produits);
    }

    // ============ OPTIONNEL : Par NOM de catégorie ============
    
    @GetMapping("/categorie/nom/{categorieNom}")
    public ResponseEntity<List<ProduitResponseDTO>> getProduitsByCategorieNom(@PathVariable String categorieNom) {
        List<ProduitResponseDTO> produits = produitService.getProduitsByCategorieNom(categorieNom);
        return ResponseEntity.ok(produits);
    }


    // GET produits par prix range
    // @GetMapping("/prix-range")
    // public ResponseEntity<List<ProduitResponseDTO>> getProduitsByPrixRange(
    //         @RequestParam BigDecimal minPrix,
    //         @RequestParam BigDecimal maxPrix) {
    //     return ResponseEntity.ok(produitService.getProduitsByPrixRange(minPrix, maxPrix));
    // }


    // // GET recherche de produits
    // @GetMapping("/recherche")
    // public ResponseEntity<List<ProduitResponseDTO>> searchProduits(@RequestParam String keyword) {
    // return ResponseEntity.ok(produitService.searchProduits(keyword));
       //}


    

  

    // ================ STATISTIQUES ================
    
    // @GetMapping("/stats/count")
    // public ResponseEntity<Long> countProduits() {
    //     return ResponseEntity.ok(produitService.countProduits());
    // }

    // @GetMapping("/stats/count/categorie/{nom}")
    // public ResponseEntity<Long> countProduitsByCategorie(@PathVariable String nom) {
    //     return ResponseEntity.ok(produitService.countProduitsByCategorie(nom));
    // }

    // @GetMapping("/stats/stock-total")
    // public ResponseEntity<Integer> getTotalStock() {
    //     return ResponseEntity.ok(produitService.getTotalStock());
    // }
}

