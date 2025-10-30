package com.example.dsms.thies.repository;



import com.example.dsms.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VenteRepositoryThies extends JpaRepository<Vente, UUID> {}
