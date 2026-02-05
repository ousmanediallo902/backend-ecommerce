package com.example.e_commerce.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.e_commerce.entity.LigneCommande;
import com.example.e_commerce.service.LigneCommandeService;

import java.util.List;

@RestController
@RequestMapping("/api/lignes")
@RequiredArgsConstructor
public class LigneCommandeController {

    private final LigneCommandeService ligneCommandeService;

    @PostMapping
    public ResponseEntity<LigneCommande> createLigne(@RequestBody LigneCommande ligne) {
        return ResponseEntity.ok(ligneCommandeService.createLigne(ligne));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneCommande> getLigne(@PathVariable Long id) {
        return ResponseEntity.ok(ligneCommandeService.getLigneById(id));
    }

    @GetMapping
    public ResponseEntity<List<LigneCommande>> getAllLignes() {
        return ResponseEntity.ok(ligneCommandeService.getAllLignes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LigneCommande> updateLigne(@PathVariable Long id, @RequestBody LigneCommande ligne) {
        return ResponseEntity.ok(ligneCommandeService.updateLigne(id, ligne));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigne(@PathVariable Long id) {
        ligneCommandeService.deleteLigne(id);
        return ResponseEntity.noContent().build();
    }
}

