# 📊 DSMS - Distributed Sales Management System (Backend)

## 🧠 Introduction

Le projet **DSMS** (*Distributed Sales Management System*) est une application **Spring Boot** développée dans le cadre du module **Base de Données Distribuées**.
Il a pour objectif de gérer les **ventes réparties sur plusieurs régions** (Dakar, Thiès et Saint-Louis), chacune disposant de sa propre base de données PostgreSQL.

Grâce à un mécanisme de **synchronisation automatique et manuelle**, les ventes restent cohérentes entre les différentes bases.
L’application fournit également une **interface web intuitive** (Thymeleaf + Bootstrap) permettant d’ajouter, modifier, rechercher et synchroniser les ventes en temps réel.

---

## 🎯 Objectifs du projet

* Concevoir une **application distribuée** simulant plusieurs sites régionaux de vente.
* Implémenter la **réplication et la synchronisation** entre bases PostgreSQL.
* Permettre une **consultation et une gestion centralisée** des ventes.
* Fournir une **interface simple, dynamique et conviviale**.
* Illustrer les principes de **cohérence, tolérance aux pannes et synchronisation** dans un environnement distribué.

---

## 🧩 Technologies utilisées

| Côté              | Technologie                 | Description                             |
| ----------------- | --------------------------- | --------------------------------------- |
| Backend           | **Java 21**                 | Langage principal                       |
| Framework         | **Spring Boot 3.x**         | Cadre d’application                     |
| ORM               | **Spring Data JPA**         | Accès et gestion des données            |
| Base de données   | **PostgreSQL**              | Stockage régional des ventes            |
| Interface         | **Thymeleaf + Bootstrap 5** | Interface web intégrée                  |
| Tâches planifiées | **Spring Scheduler**        | Synchronisation automatique             |
| Build             | **Maven**                   | Gestion des dépendances et du packaging |

---

## 🏗️ Architecture du projet

```
src/
├── main/java/com/example/dsms/
│    ├── config/               → Configuration des 3 sources de données
│    ├── controller/           → Gestionnaires Web (MVC)
│    ├── dakar/repository/     → DAO pour la région de Dakar
│    ├── thies/repository/     → DAO pour la région de Thiès
│    ├── stl/repository/       → DAO pour Saint-Louis
│    ├── model/                → Classe entité Vente
│    ├── service/              → MultiVenteService & SyncService
│    └── DsmsApplication.java  → Classe principale
└── main/resources/
├── templates/            → Pages HTML (index.html)
├── static/               → Fichiers CSS / JS
└── application.yml       → Configuration multi-datasource
```

---

## 🗄️ Configuration des bases PostgreSQL

Créer trois bases de données locales :

```sql
CREATE DATABASE ventes_dakar;
CREATE DATABASE ventes_thies;
CREATE DATABASE ventes_stlouis;
```

Et (optionnellement) un utilisateur :

```sql
CREATE USER dsms_user WITH PASSWORD 'dsms_pass';
GRANT ALL PRIVILEGES ON DATABASE ventes_dakar TO dsms_user;
GRANT ALL PRIVILEGES ON DATABASE ventes_thies TO dsms_user;
GRANT ALL PRIVILEGES ON DATABASE ventes_stlouis TO dsms_user;
```

---

## ⚙️ Configuration Spring Boot (`application.yml`)

```yaml
server:
  port: 8080

spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  datasource:
    dakar:
      url: jdbc:postgresql://localhost:5432/ventes_dakar
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver

    thies:
      url: jdbc:postgresql://localhost:5432/ventes_thies
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver

    stl:
      url: jdbc:postgresql://localhost:5432/ventes_stlouis
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver
```

---

## ⚙️ Services principaux

### 🔹 MultiVenteService

* `saveToRegion(Vente v, String region)` → Ajout selon la région choisie
* `findAll()` → Liste toutes les ventes sans doublons
* `deleteById(UUID id)` → Suppression sur toutes les bases
* `search(String keyword)` → Recherche par produit, date ou région

### 🔹 SyncService

* Synchronisation **automatique toutes les 10 secondes** via `@Scheduled`
* Synchronisation **manuelle** via un bouton sur l’interface
* Stratégie **Last-Write-Wins** (basée sur `updatedAt`)

---

## 💻 Interface Web (Thymeleaf)

Fonctionnalités :

* ➕ Ajouter une vente
* 🗑️ Supprimer une vente
* ✏️ Modifier une vente
* 🔍 Rechercher par mot-clé ou région
* 🔄 Synchroniser manuellement les bases
* 💰 Consulter le total global des ventes

🖥️ Accès :
👉 [http://localhost:8080](http://localhost:8080)

---

## 🔁 Synchronisation des données

| Type            | Description                                         |
| --------------- | --------------------------------------------------- |
| **Automatique** | Toutes les 10 secondes grâce à `@Scheduled`         |
| **Manuelle**    | Bouton “🔄 Synchroniser maintenant” sur l’interface |
| **Stratégie**   | Dernière mise à jour prioritaire (`updatedAt`)      |
| **Objectif**    | Cohérence des ventes entre les 3 bases régionales   |

---

## 🚀 Lancement du projet

1️⃣ Cloner le dépôt :

```bash
git clone https://github.com/fatoumatabintouniang-collab/dsms-backend.git
cd dsms-backend
```

2️⃣ Créer les 3 bases PostgreSQL
3️⃣ Vérifier la configuration dans `application.yml`
4️⃣ Démarrer l’application :

```bash
mvn spring-boot:run
```

5️⃣ Ouvrir dans le navigateur :
👉 [http://localhost:8080](http://localhost:8080)

---

## 👥 Membres du groupe

| N°  | Nom et Prénom              | Email                                                                           | INE          |
| --- | -------------------------- | ------------------------------------------------------------------------------- | ------------ |
| 1️⃣ | **Fatoumata Bintou Niang** | [fatoumatabintou.niang@unchk.edu.sn](mailto:fatoumatabintou.niang@unchk.edu.sn) | N04005020202 |
| 2️⃣ | **Katy Touré**             | [katy.toure@unchk.edu.sn](mailto:katy.toure@unchk.edu.sn)                       | N03729320202 |

---

## 🔗 Lien GitHub du projet

🖥️ **Backend (Spring Boot)** : [https://github.com/fatoumatabintouniang-collab/dsms-backend](https://github.com/fatoumatabintouniang-collab/dsms-backend)

---

## 🧾 Critères d’évaluation

| Critère              | Détails attendus                                             |
| -------------------- | ------------------------------------------------------------ |
| **Architecture**     | 3 bases PostgreSQL correctement configurées                  |
| **Synchronisation**  | Fonctionnelle et cohérente entre toutes les bases            |
| **Code**             | Bien structuré, clair et commenté                            |
| **Interface Web**    | Ergonomique et responsive                                    |
| **Fonctionnalités**  | Ajout, modification, suppression, recherche, synchronisation |
| **Travail d’équipe** | Collaboration équilibrée et cohérente                        |

---

## 🏁 Conclusion

Le projet **DSMS** démontre les concepts clés des **bases de données distribuées** :
la **cohérence**, la **réplication** et la **synchronisation inter-régionale**.
Cette application illustre comment maintenir des données homogènes dans un système réparti tout en offrant une interface web fluide et simple d’utilisation.
