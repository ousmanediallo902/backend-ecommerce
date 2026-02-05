package com.example.e_commerce.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.e_commerce.entity.Produit;
import com.example.e_commerce.dto.*;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    // DTO -> Entity (Création)
    @Mapping(target = "id", ignore = true) // auto-généré
    @Mapping(target = "commentaires", ignore = true) // relation OneToMany à ignorer
    @Mapping(target = "categorie", ignore = true) // gérée dans le service
    @Mapping(target = "imageUrl", ignore = true) // peut être mis à jour ailleurs
    Produit toEntity(ProduitCreateDTO dto);

    // Entity -> DTO réponse
    @Mapping(source = "categorie.id", target = "categorieId")
    @Mapping(source = "categorie.nom", target = "categorieNom")
    ProduitResponseDTO toResponseDTO(Produit produit);

    // Liste
    List<ProduitResponseDTO> toResponseDTOs(List<Produit> produits);
}
