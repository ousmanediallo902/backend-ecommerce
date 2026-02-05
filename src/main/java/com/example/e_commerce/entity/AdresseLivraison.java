package com.example.e_commerce.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdresseLivraison {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'adresse complète est obligatoire")
    @Column(nullable = false, length = 300)
    private String adresseComplete;

    @NotBlank(message = "La ville est obligatoire")
    @Column(nullable = false, length = 100)
    private String ville;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^(\\+221)?(70|76|77|78)[0-9]{7}$",
            message = "Numéro de téléphone sénégalais invalide (ex: 77XXXXXXX ou +22177XXXXXXX)")
    @Column(nullable = false, length = 20)
    private String telephone;

    @Column(length = 500)
    private String instructionsLivraison;

    @Column(nullable = false)
    @Builder.Default
    private boolean parDefaut = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_adresse_user"))
    private User user;

    @OneToMany(mappedBy = "adresseLivraison")
    @Builder.Default
    @JsonIgnore
    private List<Commande> commandes = new ArrayList<>();
}
