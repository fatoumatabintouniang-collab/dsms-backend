package com.example.dsms.controller;

import com.example.dsms.model.Vente;
import com.example.dsms.service.MultiVenteService;
import com.example.dsms.service.SyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:4200") // CORS pour Angular
@RestController
@RequestMapping("/api/ventes")
public class VenteRestController {

    private final MultiVenteService multiService;
    private final SyncService syncService;

    public VenteRestController(MultiVenteService m, SyncService s) {
        this.multiService = m;
        this.syncService = s;
    }

    @GetMapping
    public List<Vente> all() {
        return multiService.findAll();
    }

    @PostMapping("/{region}")
    public Vente save(@RequestBody Vente vente, @PathVariable String region) {
        return multiService.saveToRegion(vente, region);
    }

    // DELETE vente par UUID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        multiService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sync")
    public String manualSync() {
        syncService.synchronize();
        return "Synchronisation lancée";
    }
}


