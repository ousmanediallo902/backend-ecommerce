package com.example.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.e_commerce.dto.UserCreateDTO;
import com.example.e_commerce.dto.UserDTO;
import com.example.e_commerce.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ===== Entity -> DTO =====
    @Mapping(target = "role", source = "role")
    UserDTO toDTO(User user);

    // ===== DTO -> Entity (UserDTO ne contient pas motDePasse) =====
   
    @Mapping(target = "commandes", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "id", ignore = true) // <-- on ignore l'id ici aussi
    User toEntity(UserDTO userDTO);

    // ===== DTO -> Entity (UserCreateDTO contient motDePasse) =====
   
    @Mapping(target = "commandes", ignore = true)
    @Mapping(target = "id", ignore = true) // <-- id généré par la DB
    User toEntity(UserCreateDTO createDTO);
}
