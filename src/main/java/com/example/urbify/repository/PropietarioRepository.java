package com.example.urbify.repository;

import com.example.urbify.models.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
    Propietario findByEmail(String email);
    boolean existsByIdentification(String identification);
    Propietario findByIdentification(String identification);
}
