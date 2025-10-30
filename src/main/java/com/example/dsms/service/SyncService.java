package com.example.dsms.service;

import com.example.dsms.model.Vente;
import com.example.dsms.dakar.repository.VenteRepositoryDakar;
import com.example.dsms.thies.repository.VenteRepositoryThies;
import com.example.dsms.stl.repository.VenteRepositoryStl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SyncService {

    private final VenteRepositoryDakar dakarRepo;
    private final VenteRepositoryThies thiesRepo;
    private final VenteRepositoryStl stlRepo;

    public SyncService(VenteRepositoryDakar dakarRepo,
                       VenteRepositoryThies thiesRepo,
                       VenteRepositoryStl stlRepo) {
        this.dakarRepo = dakarRepo;
        this.thiesRepo = thiesRepo;
        this.stlRepo = stlRepo;
    }

    @Scheduled(fixedDelayString = "${sync.interval:10000}")
    public void synchronize() {
        System.out.println("=== Synchronisation automatique ===");

        // Récupère toutes les ventes de toutes les bases
        List<Vente> allVentes = new ArrayList<>();
        allVentes.addAll(dakarRepo.findAll());
        allVentes.addAll(thiesRepo.findAll());
        allVentes.addAll(stlRepo.findAll());

        System.out.println("[SyncService] Total ventes trouvées : " + allVentes.size());

        // On garde uniquement la dernière version de chaque vente (last-write-wins)
        Map<UUID, Vente> lastWrites = new HashMap<>();
        for (Vente v : allVentes) {
            lastWrites.merge(v.getId(), v, (existing, incoming) ->
                    incoming.getUpdatedAt().isAfter(existing.getUpdatedAt()) ? incoming : existing
            );
        }

        System.out.println("[SyncService] Ventes uniques après déduplication : " + lastWrites.size());

        // ✅ CORRECTION : Utilisez saveAll() au lieu de boucler avec save()
        List<Vente> ventesToSync = new ArrayList<>(lastWrites.values());

        try {
            dakarRepo.saveAll(ventesToSync);
            thiesRepo.saveAll(ventesToSync);
            stlRepo.saveAll(ventesToSync);

            System.out.println("Synchronisation terminée : " + ventesToSync.size() + " ventes synchronisées dans les 3 bases.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la synchronisation : " + e.getMessage());
        }
    }
}