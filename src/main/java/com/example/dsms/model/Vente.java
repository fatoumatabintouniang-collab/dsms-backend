package com.example.dsms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vente") // Assurez-vous que toutes les tables ont le même nom
public class Vente {

    @Id
    private UUID id;

    private LocalDate dateVente;
    private Double montant;
    private String produit;
    private String region;
    private LocalDateTime updatedAt;

    // SUPPRIMEZ @PrePersist pour éviter la régénération de l'ID
    // @PrePersist
    // public void onCreate() {
    //     id = UUID.randomUUID();  // ⛔ CAUSÉ DES DOUBLONS
    //     updatedAt = LocalDateTime.now();
    // }

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID(); // Génère UNIQUEMENT si l'ID est null
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters et Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDate getDateVente() { return dateVente; }
    public void setDateVente(LocalDate dateVente) { this.dateVente = dateVente; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public String getProduit() { return produit; }
    public void setProduit(String produit) { this.produit = produit; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}