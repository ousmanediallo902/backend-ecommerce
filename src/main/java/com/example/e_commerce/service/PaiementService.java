package com.example.e_commerce.service;


import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.e_commerce.entity.Commande;
import com.example.e_commerce.entity.Facture;
import com.example.e_commerce.entity.Paiement;
import com.example.e_commerce.entity.enums.ModePaiement;
import com.example.e_commerce.entity.enums.StatutPaiement;
import com.example.e_commerce.repository.PaiementRepository;


@Service
@RequiredArgsConstructor
public class PaiementService {
    private final PaiementRepository paiementRepository;

   // PaiementService
public Paiement processerPaiement(Commande commande, Facture facture, ModePaiement modePaiement) {
    Paiement paiement = Paiement.builder()
            .facture(facture)
            .montant(commande.getTotal())
            .datePaiement(LocalDateTime.now())
            .mode(modePaiement)      // pas besoin de conversion
            .statut(StatutPaiement.REUSSI)
            .reference("PAY-" + System.currentTimeMillis())
            .build();

    return paiementRepository.save(paiement);
}

}
