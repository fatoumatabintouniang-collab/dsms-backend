package com.example.dsms.service;

import com.example.dsms.model.Vente;
import com.example.dsms.dakar.repository.VenteRepositoryDakar;
import com.example.dsms.thies.repository.VenteRepositoryThies;
import com.example.dsms.stl.repository.VenteRepositoryStl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MultiVenteService {
    private final VenteRepositoryDakar dakarRepo;
    private final VenteRepositoryThies thiesRepo;
    private final VenteRepositoryStl stlRepo;

    public MultiVenteService(VenteRepositoryDakar d, VenteRepositoryThies t, VenteRepositoryStl s) {
        this.dakarRepo = d;
        this.thiesRepo = t;
        this.stlRepo = s;
    }

    public Vente saveToRegion(Vente v, String region) {
        v.setRegion(region);
        return switch (region.toLowerCase()) {
            case "dakar" -> dakarRepo.save(v);
            case "thies" -> thiesRepo.save(v);
            case "saint-louis" -> stlRepo.save(v);
            default -> throw new IllegalArgumentException("Région inconnue: " + region);
        };
    }

    public List<Vente> findAll() {
        // Utilise un Map pour éviter les doublons (par ID)
        Map<UUID, Vente> uniqueVentes = new HashMap<>();

        dakarRepo.findAll().forEach(v -> uniqueVentes.put(v.getId(), v));
        thiesRepo.findAll().forEach(v -> uniqueVentes.put(v.getId(), v));
        stlRepo.findAll().forEach(v -> uniqueVentes.put(v.getId(), v));

        return new ArrayList<>(uniqueVentes.values());
    }

    public List<Vente> findByRegion(String region) {
        return switch (region.toLowerCase()) {
            case "dakar" -> dakarRepo.findAll();
            case "thies" -> thiesRepo.findAll();
            case "saint-louis" -> stlRepo.findAll();
            default -> List.of();
        };
    }

    public void deleteById(UUID id) {
        // Supprime de toutes les bases
        try {
            dakarRepo.deleteById(id);
        } catch (Exception ignored) {}

        try {
            thiesRepo.deleteById(id);
        } catch (Exception ignored) {}

        try {
            stlRepo.deleteById(id);
        } catch (Exception ignored) {}
    }

    public List<Vente> search(String keyword) {
        return findAll().stream()
                .filter(v -> v.getProduit().toLowerCase().contains(keyword.toLowerCase())
                        || v.getRegion().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}
