package com.example.e_commerce.entity;

import com.example.e_commerce.entity.enums.ModePaiement;
import com.example.e_commerce.entity.enums.StatutPaiement;
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
public class Paiement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Column(nullable = false)
    private LocalDateTime datePaiement;

    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ModePaiement mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutPaiement statut;

    // Référence transaction (PSP), ex: PayDunya/Flutterwave/Stripe
    @Column(length = 200)
    private String reference;

    @OneToOne
    @JoinColumn(name = "facture_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_paiement_facture"))
    private Facture facture;
}

