package com.example.urbify.service;

import com.example.urbify.models.Visitante;
import com.example.urbify.repository.VisitanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VisitanteService {

    private final VisitanteRepository visitanteRepository; 

    public VisitanteService(VisitanteRepository visitanteRepository) { 
        this.visitanteRepository = visitanteRepository;
    }
 

    public List<Visitante> findByApartamentoApartamento(int apartamento) {
        return visitanteRepository.findByApartamentoApartamento(apartamento);
    }


    public Optional<Visitante> getById(Long id) {
        return visitanteRepository.findById(id);
    }

    public List<Visitante> findAll() {
        return visitanteRepository.findAll();
    }

    public Visitante save(Visitante visitante) { 
        return visitanteRepository.save(visitante);
    }

    public void delete(Long id) {
        visitanteRepository.deleteById(id);
    }

    public boolean existsByIdentificacion(String identificacion) {
        return visitanteRepository.existsByIdentificacion(identificacion);
    }

    public long countActiveVisitors() {
        return visitanteRepository.countByActiveTrue();
    }


    public List<Visitante> findByApartamentoId(Long Id) {
        return visitanteRepository.findByApartamentoId(Id);
    } 
    
}