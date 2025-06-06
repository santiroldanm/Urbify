// src/main/java/com/example/urbify/repository/ObservacionesRepository.java
package com.example.urbify.repository;

import com.example.urbify.models.Observaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservacionesRepository extends JpaRepository<Observaciones, Long> {

    List<Observaciones> findByApartamentoId(Long apartamentoId);
    List<Observaciones> findByTipoAndApartamentoId(Observaciones.Tipo tipo, Long apartamentoId);
    
}