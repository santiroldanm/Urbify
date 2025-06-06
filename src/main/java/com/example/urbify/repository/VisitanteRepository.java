package com.example.urbify.repository;

import com.example.urbify.models.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    
     List<Visitante> findByApartamentoApartamento(int apartamento);
     List<Visitante> findByApartamentoId(Long Id);

     
    boolean existsByIdentificacion(String identificacion);
    
     Optional<Visitante> findByIdentificacion(String identificacion);
     long countByActiveTrue();
}