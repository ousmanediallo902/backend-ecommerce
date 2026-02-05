# 📦 Système de Gestion des Adresses de Livraison

## 🎯 Vue d'ensemble

Le système a été **refactorisé professionnellement** pour séparer les adresses de livraison dans leur propre entité, permettant une gestion plus flexible et réutilisable des informations de livraison.

## 🏗️ Architecture

### Entités créées

#### 1. **AdresseLivraison**
Représente une adresse de livraison appartenant à un utilisateur.

**Champs :**
- `id` : Identifiant unique
- `adresseComplete` : Adresse complète (ex: 123 Avenue de la République)
- `ville` : Ville (ex: Dakar)
- `codePostal` : Code postal (optionnel)
- `telephone` : Téléphone de contact (format sénégalais)
- `instructionsLivraison` : Instructions spéciales (optionnel)
- `parDefaut` : Indique si c'est l'adresse par défaut
- `user` : Relation ManyToOne vers User
- `commandes` : Relation OneToMany vers Commande

### Relations

```
User (1) ----< (N) AdresseLivraison (1) ----< (N) Commande
```

- Un **utilisateur** peut avoir **plusieurs adresses de livraison**
- Une **adresse de livraison** peut être utilisée pour **plusieurs commandes**
- Une **commande** a **une seule adresse de livraison**

## 🔌 API REST

### Endpoints disponibles

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/adresses-livraison/user/{userId}` | Créer une nouvelle adresse |
| `GET` | `/api/adresses-livraison/user/{userId}` | Lister toutes les adresses d'un user |
| `GET` | `/api/adresses-livraison/{adresseId}` | Obtenir une adresse spécifique |
| `GET` | `/api/adresses-livraison/user/{userId}/par-defaut` | Obtenir l'adresse par défaut |
| `PUT` | `/api/adresses-livraison/{adresseId}` | Modifier une adresse |
| `PATCH` | `/api/adresses-livraison/{adresseId}/par-defaut` | Définir comme adresse par défaut |
| `DELETE` | `/api/adresses-livraison/{adresseId}` | Supprimer une adresse |

### 📝 Exemples de requêtes

#### Créer une adresse de livraison

```http
POST /api/adresses-livraison/user/1
Content-Type: application/json

{
  "adresseComplete": "123 Avenue de la République, Appartement 5B",
  "ville": "Dakar",
  "codePostal": "10000",
  "telephone": "+221771234567",
  "instructionsLivraison": "Sonner deux fois, livrer entre 9h et 17h",
  "parDefaut": true
}
```

#### Créer une commande avec une adresse spécifique

```http
POST /api/commandes
Content-Type: application/json

{
  "userId": 1,
  "adresseLivraisonId": 3,
  "modePaiement": "MOBILE_MONEY",
  "lignes": [
    {
      "produitId": 10,
      "quantite": 2
    }
  ]
}
```

#### Créer une commande avec l'adresse par défaut

```http
POST /api/commandes
Content-Type: application/json

{
  "userId": 1,
  "adresseLivraisonId": null,
  "modePaiement": "CARTE_BANCAIRE",
  "lignes": [...]
}
```

**Note :** Si `adresseLivraisonId` est `null`, le système utilisera automatiquement l'adresse marquée par défaut de l'utilisateur.

## 🔄 Flux de création de commande

1. **Vérification utilisateur** : Validation de l'existence de l'utilisateur
2. **Récupération de l'adresse** :
   - Si `adresseLivraisonId` est fourni : utiliser cette adresse (après vérification qu'elle appartient à l'utilisateur)
   - Sinon : utiliser l'adresse par défaut de l'utilisateur
3. **Validation** : S'assurer qu'une adresse de livraison existe
4. **Création de la commande** avec référence à l'adresse
5. **Traitement** des lignes, facture et paiement

## ✨ Fonctionnalités clés

### Gestion des adresses par défaut

- Un utilisateur peut avoir **une seule adresse par défaut**
- Lors de la définition d'une adresse comme par défaut, toutes les autres perdent automatiquement ce statut
- Utile pour les commandes rapides sans avoir à choisir l'adresse

### Réutilisation des adresses

- Les adresses sont **conservées** même après utilisation dans une commande
- Un utilisateur peut gérer plusieurs adresses (domicile, bureau, parents, etc.)
- Facilite les commandes récurrentes vers la même adresse

### Validation des données

- **Téléphone** : Format sénégalais validé (ex: 771234567 ou +221771234567)
- **Adresse complète** : Obligatoire
- **Ville** : Obligatoire

## 📊 Avantages de cette architecture

### ✅ Professionnalisme
- Respect des principes de normalisation de base de données
- Séparation des responsabilités (SoC)
- Code plus maintenable et évolutif

### ✅ Expérience utilisateur
- Enregistrement une seule fois, utilisation multiple
- Gestion facile de plusieurs adresses
- Changement d'adresse sans ressaisie

### ✅ Évolutivité
- Facile d'ajouter de nouveaux champs (région, pays, coordonnées GPS, etc.)
- Possibilité d'ajouter des validations métier (zones de livraison, frais de port par zone, etc.)
- Statistiques de livraison par zone facilitées

## 🚀 Prochaines améliorations possibles

- [ ] Géolocalisation avec coordonnées GPS
- [ ] Validation des zones de livraison couvertes
- [ ] Calcul automatique des frais de port selon l'adresse
- [ ] Historique des livraisons par adresse
- [ ] Adresses suggérées (autocomplete)
- [ ] Validation d'adresse avec un service externe

## 🗄️ Structure de la base de données

### Table `adresse_livraison`

```sql
CREATE TABLE adresse_livraison (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    adresse_complete VARCHAR(300) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    code_postal VARCHAR(100),
    telephone VARCHAR(20) NOT NULL,
    instructions_livraison VARCHAR(500),
    par_defaut BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Modification de la table `commande`

```sql
ALTER TABLE commande
ADD COLUMN adresse_livraison_id BIGINT,
ADD FOREIGN KEY (adresse_livraison_id) REFERENCES adresse_livraison(id);

-- Supprimer l'ancien champ (si existant)
ALTER TABLE commande DROP COLUMN informations_livraison;
```

**Note :** Avec `spring.jpa.hibernate.ddl-auto=update`, ces modifications seront appliquées automatiquement au redémarrage de l'application.

---

## 📞 Support

Pour toute question ou amélioration, n'hésitez pas à consulter le code source ou à créer une issue.
