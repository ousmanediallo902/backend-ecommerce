package com.example.e_commerce.dto;

import com.example.e_commerce.entity.enums.Role;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String pays;
    private String region;
    private String departement;
    private Role role;
}
