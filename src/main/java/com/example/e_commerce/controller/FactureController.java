package com.example.e_commerce.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;

import com.example.e_commerce.entity.Facture;
import com.example.e_commerce.repository.FactureRepository;
import com.example.e_commerce.service.FactureService;


@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;
    private final FactureRepository factureRepository;

    @GetMapping("/factures/{id}/download")
public ResponseEntity<byte[]> telechargerFacture(@PathVariable Long id) {
    Facture facture = factureRepository.findByIdWithCommandeAndUser(id)
            .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

    byte[] pdfBytes = factureService.genererPdf(facture);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=facture-" + facture.getId() + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
}

}
