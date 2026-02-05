package com.example.e_commerce.entity;

import com.example.e_commerce.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_email", columnNames = "email")
})

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nom;

    @Email @NotBlank
    @Column(nullable = false, length = 180)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String motDePasse;

    @Pattern(regexp = "^(\\+221)?(70|76|77|78)[0-9]{7}$",
         message = "Numéro de téléphone sénégalais invalide (ex: 77XXXXXXX ou +22177XXXXXXX)")
    @Column(length = 13)
    private String telephone;

    @NotBlank(message = "Le pays est obligatoire")
    @Column(nullable = false, length = 100)
    private String pays;

    @NotBlank(message = "La région est obligatoire")
    @Column(nullable = false, length = 100)
    private String region;

    @NotBlank(message = "Le département est obligatoire")
    @Column(nullable = false, length = 100)
    private String departement;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    

    // Un utilisateur peut passer plusieurs commandes
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Commande> commandes = new ArrayList<>();

    // Un utilisateur peut avoir plusieurs adresses de livraison
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<AdresseLivraison> adressesLivraison = new ArrayList<>();
}