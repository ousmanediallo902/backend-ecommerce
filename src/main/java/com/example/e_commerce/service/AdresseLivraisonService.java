package com.example.e_commerce.service;

import com.example.e_commerce.dto.AdresseLivraisonRequestDTO;
import com.example.e_commerce.dto.AdresseLivraisonResponseDTO;
import com.example.e_commerce.entity.AdresseLivraison;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.AdresseLivraisonRepository;
import com.example.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdresseLivraisonService {

    private final AdresseLivraisonRepository adresseLivraisonRepository;
    private final UserRepository userRepository;

    public AdresseLivraisonResponseDTO creerAdresse(Long userId, AdresseLivraisonRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        // Si l'adresse est marquée par défaut, retirer le flag des autres adresses de l'utilisateur
        if (requestDTO.isParDefaut()) {
            List<AdresseLivraison> adressesParDefaut = adresseLivraisonRepository.findByUserIdAndParDefautTrue(userId);
            adressesParDefaut.forEach(adresse -> adresse.setParDefaut(false));
            adresseLivraisonRepository.saveAll(adressesParDefaut);
        }

        AdresseLivraison adresse = AdresseLivraison.builder()
                .adresseComplete(requestDTO.getAdresseComplete())
                .ville(requestDTO.getVille())
                .telephone(requestDTO.getTelephone())
                .instructionsLivraison(requestDTO.getInstructionsLivraison())
                .parDefaut(requestDTO.isParDefaut())
                .user(user)
                .build();

        AdresseLivraison savedAdresse = adresseLivraisonRepository.save(adresse);
        return mapToResponseDTO(savedAdresse);
    }

    public AdresseLivraisonResponseDTO modifierAdresse(Long adresseId, AdresseLivraisonRequestDTO requestDTO) {
        AdresseLivraison adresse = adresseLivraisonRepository.findById(adresseId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée avec l'ID: " + adresseId));

        // Si l'adresse est marquée par défaut, retirer le flag des autres adresses de l'utilisateur
        if (requestDTO.isParDefaut() && !adresse.isParDefaut()) {
            List<AdresseLivraison> adressesParDefaut = adresseLivraisonRepository
                    .findByUserIdAndParDefautTrue(adresse.getUser().getId());
            adressesParDefaut.forEach(a -> a.setParDefaut(false));
            adresseLivraisonRepository.saveAll(adressesParDefaut);
        }

        adresse.setAdresseComplete(requestDTO.getAdresseComplete());
        adresse.setVille(requestDTO.getVille());
        adresse.setTelephone(requestDTO.getTelephone());
        adresse.setInstructionsLivraison(requestDTO.getInstructionsLivraison());
        adresse.setParDefaut(requestDTO.isParDefaut());

        AdresseLivraison updatedAdresse = adresseLivraisonRepository.save(adresse);
        return mapToResponseDTO(updatedAdresse);
    }

    public void supprimerAdresse(Long adresseId) {
        AdresseLivraison adresse = adresseLivraisonRepository.findById(adresseId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée avec l'ID: " + adresseId));
        adresseLivraisonRepository.delete(adresse);
    }

    @Transactional(readOnly = true)
    public AdresseLivraisonResponseDTO getAdresseById(Long adresseId) {
        AdresseLivraison adresse = adresseLivraisonRepository.findById(adresseId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée avec l'ID: " + adresseId));
        return mapToResponseDTO(adresse);
    }

    @Transactional(readOnly = true)
    public List<AdresseLivraisonResponseDTO> getAdressesByUser(Long userId) {
        return adresseLivraisonRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdresseLivraisonResponseDTO getAdresseParDefaut(Long userId) {
        return adresseLivraisonRepository.findByUserIdAndParDefaut(userId, true)
                .map(this::mapToResponseDTO)
                .orElse(null);
    }

    public AdresseLivraisonResponseDTO definirCommeParDefaut(Long adresseId) {
        AdresseLivraison adresse = adresseLivraisonRepository.findById(adresseId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée avec l'ID: " + adresseId));

        // Retirer le flag par défaut des autres adresses
        List<AdresseLivraison> adressesParDefaut = adresseLivraisonRepository
                .findByUserIdAndParDefautTrue(adresse.getUser().getId());
        adressesParDefaut.forEach(a -> a.setParDefaut(false));
        adresseLivraisonRepository.saveAll(adressesParDefaut);

        // Définir cette adresse comme par défaut
        adresse.setParDefaut(true);
        AdresseLivraison updatedAdresse = adresseLivraisonRepository.save(adresse);
        return mapToResponseDTO(updatedAdresse);
    }

    private AdresseLivraisonResponseDTO mapToResponseDTO(AdresseLivraison adresse) {
        AdresseLivraisonResponseDTO dto = new AdresseLivraisonResponseDTO();
        dto.setId(adresse.getId());
        dto.setAdresseComplete(adresse.getAdresseComplete());
        dto.setVille(adresse.getVille());
        dto.setTelephone(adresse.getTelephone());
        dto.setInstructionsLivraison(adresse.getInstructionsLivraison());
        dto.setParDefaut(adresse.isParDefaut());
        dto.setUserId(adresse.getUser().getId());
        return dto;
    }
}
