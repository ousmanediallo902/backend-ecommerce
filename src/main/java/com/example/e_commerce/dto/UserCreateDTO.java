package com.example.e_commerce.dto;

import com.example.e_commerce.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDTO {
    @NotBlank
    private String nom;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String motDePasse;

    
    
     @Pattern(regexp = "^(\\+221)?(70|76|77|78)[0-9]{7}$",
         message = "Numéro de téléphone sénégalais invalide (ex: 77XXXXXXX ou +22177XXXXXXX)")
    @Column(length = 13)
    private String telephone;

    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    @NotBlank(message = "La région est obligatoire")
    private String region;

    @NotBlank(message = "Le département est obligatoire")
    private String departement;

    private Role role;
}

