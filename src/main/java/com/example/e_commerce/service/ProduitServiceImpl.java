package com.example.e_commerce.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.e_commerce.dto.ProduitCreateDTO;
import com.example.e_commerce.dto.ProduitResponseDTO;
import com.example.e_commerce.entity.Categorie;
import com.example.e_commerce.entity.Produit;
import com.example.e_commerce.exception.ResourceNotFoundException;
import com.example.e_commerce.mapper.ProduitMapper;
import com.example.e_commerce.repository.CategorieRepository;
import com.example.e_commerce.repository.ProduitRepository;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository  categorieRepository;
     private final ProduitMapper produitMapper;
    
     @Value("${upload.path}")
    private String uploadPath;

 public String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) return null;

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadPath + fileName);
        Files.createDirectories(path.getParent());
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Retourne le chemin relatif pour que le front puisse afficher l’image
        return "/uploads/" + fileName;
    }

    
      public ProduitResponseDTO createProduit(ProduitCreateDTO dto, MultipartFile image) throws IOException {
        Produit produit = produitMapper.toEntity(dto);

        // Charger la catégorie
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec id " + dto.getCategorieId()));
        produit.setCategorie(categorie);

        // Sauvegarder l’image si présente
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            produit.setImageUrl(imageUrl);
        }

        Produit saved = produitRepository.save(produit);
        return produitMapper.toResponseDTO(saved);
    }


    
   @Override
public ProduitResponseDTO updateProduit(Long id, ProduitCreateDTO dto, MultipartFile image) throws IOException {
    Produit existing = produitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec id: " + id));

    // Mettre à jour les champs depuis le DTO
    existing.setNom(dto.getNom());
    existing.setPrix(dto.getPrix());
    existing.setStock(dto.getStock());

    // Charger la catégorie depuis son id
    Categorie categorie = categorieRepository.findById(dto.getCategorieId())
            .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec id " + dto.getCategorieId()));
    existing.setCategorie(categorie);

    // Gérer l’image
    if (image != null && !image.isEmpty()) {
        String imageUrl = saveImage(image);
        existing.setImageUrl(imageUrl);
    }

    Produit updated = produitRepository.save(existing);
    return produitMapper.toResponseDTO(updated);
}


    @Override
    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec id: " + id));
        produitRepository.delete(produit);
    }

  @Override
public ProduitResponseDTO getProduitById(Long id) {
    Produit produit = produitRepository.findByIdWithCategorie(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
    return produitMapper.toResponseDTO(produit);
}



    @Override
     public List<ProduitResponseDTO> getAllProduits() {
    List<Produit> produits = produitRepository.findAllWithCategorie(); 
    return produitMapper.toResponseDTOs(produits);
}




@Transactional(readOnly = true)
public List<ProduitResponseDTO> getProduitsByCategorieId(Long categorieId) {
    return produitRepository.findByCategorieId(categorieId).stream()
            .map(produitMapper::toResponseDTO)
            .collect(Collectors.toList());
}


 // ✅ Méthode pour NOM de catégorie (optionnelle)
    @Transactional(readOnly = true)
    public List<ProduitResponseDTO> getProduitsByCategorieNom(String categorieNom) {
        return produitRepository.findByCategorieNom(categorieNom).stream()
                .map(produitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProduitResponseDTO> getProduitsByPrixRange(BigDecimal minPrix, BigDecimal maxPrix) {
        return produitRepository.findByPrixBetween(minPrix, maxPrix).stream()
                .map(produitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProduitResponseDTO> getProduitsEnStock() {
        return produitRepository.findByStockGreaterThan(0).stream()
                .map(produitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProduitResponseDTO> searchProduits(String keyword) {
        return produitRepository.findByNomContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword).stream()
                .map(produitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    

    // ================ STATISTIQUES ================

    public Long countProduits() {
        return produitRepository.count();
    }

     public Long countProduitsByCategorie(String categorieNom) {
        return produitRepository.countByCategorieNom(categorieNom);
    }

    public Integer getTotalStock() {
        return produitRepository.sumStock();
    }



    // public List<ProduitResponseDTO> getProduitsEnPromotion() {
    //     return produitRepository.findByPromotionTrue().stream()
    //             .map(produitMapper::toResponseDTO)
    //             .collect(Collectors.toList());
    // }
}

