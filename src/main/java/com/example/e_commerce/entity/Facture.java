package com.example.e_commerce.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Facture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Column(nullable = false)
    private LocalDateTime dateFacture;

    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal montantTotal;

    // Optionnel : numéro de facture
    @Column(length = 100, unique = true)
    private String numero;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "commande_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_facture_commande"))
    private Commande commande;

    @OneToOne(mappedBy = "facture", cascade = CascadeType.ALL)
    private Paiement paiement;
}

