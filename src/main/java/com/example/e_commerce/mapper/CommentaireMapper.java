package com.example.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.e_commerce.dto.CommentaireRequestDTO;
import com.example.e_commerce.dto.CommentaireResponseDTO;
import com.example.e_commerce.entity.Commentaire;

@Mapper(componentModel = "spring")
public interface CommentaireMapper {

    CommentaireMapper INSTANCE = Mappers.getMapper(CommentaireMapper.class);

    // Entity -> Response DTO
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userNom", source = "user.nom")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "produitId", source = "produit.id")
    @Mapping(target = "produitNom", source = "produit.nom")
    @Mapping(target = "produitPrix", source = "produit.prix")
    CommentaireResponseDTO toDTO(Commentaire commentaire);

    // Request DTO -> Entity
    @Mapping(target = "id", ignore = true) // auto-généré
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "produit.id", source = "produitId")
    @Mapping(target = "dateCommentaire", expression = "java(dto.getDateCommentaire() != null ? dto.getDateCommentaire() : java.time.LocalDateTime.now())")
    Commentaire toEntity(CommentaireRequestDTO dto);
}