package com.example.e_commerce.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.example.e_commerce.dto.AdresseLivraisonResponseDTO;
import com.example.e_commerce.dto.CommandeRequestDTO;
import com.example.e_commerce.dto.CommandeResponseDTO;
import com.example.e_commerce.dto.LigneCommandeRequestDTO;
import com.example.e_commerce.dto.LigneCommandeResponseDTO;
import com.example.e_commerce.entity.AdresseLivraison;
import com.example.e_commerce.entity.Commande;
import com.example.e_commerce.entity.Facture;
import com.example.e_commerce.entity.LigneCommande;
import com.example.e_commerce.entity.Paiement;
import com.example.e_commerce.entity.Produit;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.entity.enums.StatutCommande;
import com.example.e_commerce.repository.AdresseLivraisonRepository;
import com.example.e_commerce.repository.CommandeRepository;
import com.example.e_commerce.repository.ProduitRepository;
import com.example.e_commerce.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;  // ✔️ Correct
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final UserRepository userRepository;
    private final ProduitRepository produitRepository;
    private final FactureService factureService;
    private final PaiementService paiementService;
    private final AdresseLivraisonRepository adresseLivraisonRepository;

    public CommandeResponseDTO creerCommandeComplete(CommandeRequestDTO commandeRequest) {
    // 1. Validation de l'utilisateur
    User user = userRepository.findById(commandeRequest.getUserId())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    
    // 2. Récupération de l'adresse de livraison
    AdresseLivraison adresseLivraison;
    if (commandeRequest.getAdresseLivraisonId() != null) {
        // Utiliser l'adresse spécifiée
        adresseLivraison = adresseLivraisonRepository.findById(commandeRequest.getAdresseLivraisonId())
                .orElseThrow(() -> new RuntimeException("Adresse de livraison non trouvée"));
        
        // Vérifier que l'adresse appartient bien à l'utilisateur
        if (!adresseLivraison.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cette adresse n'appartient pas à cet utilisateur");
        }
    } else {
        // Utiliser l'adresse par défaut
        adresseLivraison = adresseLivraisonRepository.findByUserIdAndParDefaut(user.getId(), true)
                .orElseThrow(() -> new RuntimeException("Aucune adresse de livraison trouvée. Veuillez créer une adresse d'abord."));
    }
    
    // 3. Création de la commande
    Commande commande = new Commande();
    commande.setDateCommande(LocalDateTime.now());
    commande.setStatut(StatutCommande.EN_COURS);
    commande.setUser(user);
    commande.setAdresseLivraison(adresseLivraison);
    commande.setTotal(BigDecimal.ZERO);
    
    // 4. Traitement des lignes de commande
    BigDecimal total = BigDecimal.ZERO;
    for (LigneCommandeRequestDTO ligneRequest : commandeRequest.getLignes()) {
        Produit produit = produitRepository.findById(ligneRequest.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        if (produit.getStock() < ligneRequest.getQuantite()) {
            throw new RuntimeException("Stock insuffisant pour: " + produit.getNom());
        }
        
        LigneCommande ligne = LigneCommande.builder()
                .commande(commande)
                .produit(produit)
                .quantite(ligneRequest.getQuantite())
                .prixUnitaire(produit.getPrix())
                .build();
        
        commande.getLignes().add(ligne);
        total = total.add(ligne.getSousTotal());
        
        produit.setStock(produit.getStock() - ligneRequest.getQuantite());
        produitRepository.save(produit);
    }
    
    // 4. Mise à jour du total
    commande.setTotal(total);
    
    // 5. Sauvegarde intermédiaire de la commande
    Commande savedCommande = commandeRepository.save(commande);
    
    // 6. Génération de la facture (avant le paiement)
    Facture facture = factureService.genererFacture(savedCommande);
    savedCommande.setFacture(facture);

    // 7. Traitement du paiement, lié à la facture
    try {
        Paiement paiement = paiementService.processerPaiement(savedCommande, facture, commandeRequest.getModePaiement());
        facture.setPaiement(paiement); // lie le paiement à la facture
        // Le statut reste EN_COURS et sera mis à jour via les endpoints dédiés
    } catch (Exception e) {
        savedCommande.setStatut(StatutCommande.ANNULEE);
        commandeRepository.save(savedCommande);
        throw new RuntimeException("Échec du paiement: " + e.getMessage());
    }
    
    // 8. Sauvegarde finale avec la facture et le paiement
    Commande finalCommande = commandeRepository.save(savedCommande);
    
    return mapToResponseDTO(finalCommande);
}


     private CommandeResponseDTO mapToResponseDTO(Commande commande) {
        CommandeResponseDTO response = new CommandeResponseDTO();
        response.setId(commande.getId());
        response.setDateCommande(commande.getDateCommande());
        response.setStatut(commande.getStatut());
        response.setTotal(commande.getTotal());
        response.setUserId(commande.getUser().getId());
        
        // Mapper l'adresse de livraison
        if (commande.getAdresseLivraison() != null) {
            AdresseLivraisonResponseDTO adresseDTO = new AdresseLivraisonResponseDTO();
            adresseDTO.setId(commande.getAdresseLivraison().getId());
            adresseDTO.setAdresseComplete(commande.getAdresseLivraison().getAdresseComplete());
            adresseDTO.setVille(commande.getAdresseLivraison().getVille());
            adresseDTO.setTelephone(commande.getAdresseLivraison().getTelephone());
            adresseDTO.setInstructionsLivraison(commande.getAdresseLivraison().getInstructionsLivraison());
            adresseDTO.setParDefaut(commande.getAdresseLivraison().isParDefaut());
            adresseDTO.setUserId(commande.getAdresseLivraison().getUser().getId());
            response.setAdresseLivraison(adresseDTO);
        }
        
        if (commande.getFacture() != null) {
            response.setFactureId(commande.getFacture().getId());

             if (commande.getFacture().getPaiement() != null) {
            response.setPaiementId(commande.getFacture().getPaiement().getId());
            response.setReferencePaiement(commande.getFacture().getPaiement().getReference());
        }
        }
        
        // Mapping des lignes de commande
        List<LigneCommandeResponseDTO> lignesDTO = commande.getLignes().stream()
                .map(this::mapLigneToDTO)
                .collect(Collectors.toList());
        response.setLignes(lignesDTO);
        
        return response;
    }

    // MÉTHODE HELPER POUR LES LIGNES
    private LigneCommandeResponseDTO mapLigneToDTO(LigneCommande ligne) {
        LigneCommandeResponseDTO dto = new LigneCommandeResponseDTO();
        dto.setProduitId(ligne.getProduit().getId());
        dto.setProduitNom(ligne.getProduit().getNom());
        dto.setQuantite(ligne.getQuantite());
        dto.setPrixUnitaire(ligne.getPrixUnitaire());
        dto.setSousTotal(ligne.getSousTotal());
        return dto;
    }


    // ==================== MÉTHODES GET ====================

    @Transactional(readOnly = true)
    public CommandeResponseDTO getCommandeById(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
        return mapToResponseDTO(commande);
    }

    @Transactional(readOnly = true)
    public List<CommandeResponseDTO> getAllCommandes() {
        return commandeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommandeResponseDTO> getCommandesByUser(Long userId) {
        return commandeRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommandeResponseDTO> getCommandesByStatut(StatutCommande statut) {
        return commandeRepository.findByStatut(statut).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommandeResponseDTO> getCommandesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return commandeRepository.findByDateCommandeBetween(startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getChiffreAffairesTotal() {
        return commandeRepository.calculateTotalRevenue();
    }

    @Transactional(readOnly = true)
    public BigDecimal getChiffreAffairesByUser(Long userId) {
        return commandeRepository.calculateRevenueByUser(userId);
    }

    @Transactional(readOnly = true)
    public Long countCommandesByUser(Long userId) {
        return commandeRepository.countByUserId(userId);
    }

    // ==================== MÉTHODE UPDATE STATUT ====================

    public CommandeResponseDTO updateStatut(Long commandeId, StatutCommande nouveauStatut) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + commandeId));
        
        // Validation des transitions de statut
        validateStatutTransition(commande.getStatut(), nouveauStatut);
        
        commande.setStatut(nouveauStatut);
        Commande updatedCommande = commandeRepository.save(commande);
        
        return mapToResponseDTO(updatedCommande);
    }

    private void validateStatutTransition(StatutCommande statutActuel, StatutCommande nouveauStatut) {
        // Règles de transition de statut
        if (statutActuel == StatutCommande.ANNULEE) {
            throw new RuntimeException("Impossible de modifier le statut d'une commande annulée");
        }
        
        if (statutActuel == StatutCommande.LIVREE && nouveauStatut != StatutCommande.ANNULEE) {
            throw new RuntimeException("Une commande livrée ne peut être que annulée");
        }
        
        // Vérifier la séquence logique
        if (statutActuel == StatutCommande.EN_COURS && nouveauStatut == StatutCommande.LIVREE) {
            throw new RuntimeException("Une commande EN_COURS doit d'abord être PAYEE puis EN_LIVRAISON avant d'être LIVREE");
        }
    }
}
