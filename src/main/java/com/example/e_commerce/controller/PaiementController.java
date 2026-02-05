// package com.example.e_commerce.controller;



// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.e_commerce.entity.Paiement;
// import com.example.e_commerce.service.PaiementService;

// import java.util.List;

// @RestController
// @RequestMapping("/api/paiements")
// @RequiredArgsConstructor
// public class PaiementController {

//     private final PaiementService paiementService;

//     @PostMapping
//     public ResponseEntity<Paiement> createPaiement(@RequestBody Paiement paiement) {
//         return ResponseEntity.ok(paiementService.createPaiement(paiement));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Paiement> getPaiement(@PathVariable Long id) {
//         return ResponseEntity.ok(paiementService.getPaiementById(id));
//     }

//     @GetMapping
//     public ResponseEntity<List<Paiement>> getAllPaiements() {
//         return ResponseEntity.ok(paiementService.getAllPaiements());
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Paiement> updatePaiement(@PathVariable Long id, @RequestBody Paiement paiement) {
//         return ResponseEntity.ok(paiementService.updatePaiement(id, paiement));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
//         paiementService.deletePaiement(id);
//         return ResponseEntity.noContent().build();
//     }
// }

