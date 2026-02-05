package com.example.e_commerce.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.e_commerce.entity.LigneCommande;
import com.example.e_commerce.exception.ResourceNotFoundException;
import com.example.e_commerce.repository.LigneCommandeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LigneCommandeService {

    private final LigneCommandeRepository ligneCommandeRepository;

    public LigneCommande createLigne(LigneCommande ligneCommande) {
        return ligneCommandeRepository.save(ligneCommande);
    }

    public LigneCommande updateLigne(Long id, LigneCommande ligneCommande) {
        LigneCommande existing = ligneCommandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LigneCommande introuvable avec id: " + id));
        existing.setProduit(ligneCommande.getProduit());
        existing.setQuantite(ligneCommande.getQuantite());
        return ligneCommandeRepository.save(existing);
    }

    public void deleteLigne(Long id) {
        LigneCommande ligneCommande = ligneCommandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LigneCommande introuvable avec id: " + id));
        ligneCommandeRepository.delete(ligneCommande);
    }

    public LigneCommande getLigneById(Long id) {
        return ligneCommandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LigneCommande introuvable avec id: " + id));
    }

    public List<LigneCommande> getAllLignes() {
        return ligneCommandeRepository.findAll();
    }
}

