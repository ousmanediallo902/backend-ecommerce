package com.example.e_commerce.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.e_commerce.entity.Categorie;
import com.example.e_commerce.exception.ResourceNotFoundException;
import com.example.e_commerce.repository.CategorieRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Categorie updateCategorie(Long id, Categorie categorie) {
        Categorie existing = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable avec id: " + id));
        existing.setNom(categorie.getNom());
        existing.setDescription(categorie.getDescription());
        return categorieRepository.save(existing);
    }

    public void deleteCategorie(Long id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable avec id: " + id));
        categorieRepository.delete(categorie);
    }

    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable avec id: " + id));
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }
}

