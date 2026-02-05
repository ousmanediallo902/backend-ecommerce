package com.example.e_commerce.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.e_commerce.entity.Commande;
import com.example.e_commerce.entity.Facture;
import com.example.e_commerce.repository.FactureRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import jakarta.transaction.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;






@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;

    @Transactional
    public Facture genererFacture(Commande commande) {
        // Vérifiez que la commande est déjà persistée
        if (commande.getId() == null) {
            throw new RuntimeException("La commande doit être sauvegardée avant de générer une facture");
        }
        
        Facture facture = Facture.builder()
                .commande(commande)
                .dateFacture(LocalDateTime.now())
                .montantTotal(commande.getTotal())
                .numero(genererNumeroFacture())
                .build();
        
        return factureRepository.save(facture);
    }
    
    private String genererNumeroFacture() {
        return "FACT-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }


    public byte[] genererPdf(Facture facture) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Facture #" + facture.getId()));
        document.add(new Paragraph("Date: " + facture.getDateFacture()));

        // Vérification que l'utilisateur existe
        if (facture.getCommande() != null && facture.getCommande().getUser() != null) {
            document.add(new Paragraph("Client: " + facture.getCommande().getUser().getNom()));
        } else {
            document.add(new Paragraph("Client: N/A"));
        }

        // Total
        document.add(new Paragraph("Total: " + (facture.getMontantTotal() != null ? facture.getMontantTotal() : "0")));

        // Lignes de commande
        document.add(new Paragraph("---- Lignes de commande ----"));
        if (facture.getCommande() != null && facture.getCommande().getLignes() != null) {
            facture.getCommande().getLignes().forEach(ligne -> {
                String nomProduit = ligne.getProduit() != null ? ligne.getProduit().getNom() : "Produit inconnu";
                document.add(new Paragraph(
                        nomProduit + " x " + ligne.getQuantite() +
                        " = " + ligne.getSousTotal()
                ));
            });
        }

        document.close();
        return baos.toByteArray();
    } catch (Exception e) {
        e.printStackTrace(); // affiche le vrai problème dans la console
        throw new RuntimeException("Erreur lors de la génération du PDF", e);
    }
}

}