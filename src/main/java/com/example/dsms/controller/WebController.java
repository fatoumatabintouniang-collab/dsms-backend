package com.example.dsms.controller;

import com.example.dsms.model.Vente;
import com.example.dsms.service.MultiVenteService;
import com.example.dsms.service.SyncService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class WebController {
    private final MultiVenteService multiService;
    private final SyncService syncService;

    public WebController(MultiVenteService m, SyncService s) {
        this.multiService = m;
        this.syncService = s;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Vente> ventes = multiService.findAll();
        double total = ventes.stream()
                .mapToDouble(v -> v.getMontant() != null ? v.getMontant() : 0)
                .sum();

        model.addAttribute("ventes", ventes);
        model.addAttribute("vente", new Vente());
        model.addAttribute("total", total);
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Vente vente, RedirectAttributes redirectAttrs) {
        multiService.saveToRegion(vente, vente.getRegion());
        redirectAttrs.addFlashAttribute("message", "✅ Vente ajoutée avec succès !");
        return "redirect:/";
    }

    @PostMapping("/sync")
    public String sync(RedirectAttributes redirectAttrs) {
        syncService.synchronize();
        redirectAttrs.addFlashAttribute("message", "✅ Synchronisation terminée !");
        redirectAttrs.addFlashAttribute("lastSync", new Date());
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vente vente, RedirectAttributes redirectAttrs) {
        multiService.saveToRegion(vente, vente.getRegion());
        redirectAttrs.addFlashAttribute("message", "✅ Vente mise à jour !");
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttrs) {
        multiService.deleteById(id);
        redirectAttrs.addFlashAttribute("message", "🗑️ Vente supprimée !");
        return "redirect:/";
    }

    @GetMapping(params = "search")
    public String search(@RequestParam String search, Model model) {
        List<Vente> resultats = multiService.search(search);
        model.addAttribute("ventes", resultats);
        model.addAttribute("vente", new Vente());
        model.addAttribute("total", resultats.stream().mapToDouble(v -> v.getMontant() != null ? v.getMontant() : 0).sum());
        return "index";
    }
}