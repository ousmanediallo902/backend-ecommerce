package com.example.e_commerce.entity.enums;

public enum StatutCommande {
    EN_COURS,    // Commande créée mais paiement en cours
    PAYEE,       // Paiement validé, en attente de préparation
    EN_LIVRAISON, // Commande expédiée
    LIVREE,      // Commande livrée au client
    ANNULEE      // Commande annulée
}
