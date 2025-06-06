package com.example.urbify.service;

import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Observaciones;
import com.example.urbify.models.Vigilant;
import com.example.urbify.repository.ApartamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApartamentosService {

    @Autowired
    private ApartamentosRepository apartamentosRepository;

    public List<Apartamentos> listAllApartments() {
        return apartamentosRepository.findAll();
    }

    public List<Apartamentos> listActive(){
        return apartamentosRepository.findByActiveTrue();
    }

    public Page<Apartamentos> getPaginatedApartments(Pageable pageable) {
        return apartamentosRepository.findAll(pageable);
    }

    public Apartamentos findById(Long id) {
        return apartamentosRepository.findById(id).orElse(null);
    }

    public List<Apartamentos> getAvailable() {
        return apartamentosRepository.findByPropietarioIsNull();
    }

    public long countAvailable() {
        return apartamentosRepository.countByActiveFalse();
    }

    public Apartamentos save(Apartamentos apartamento) {
        return apartamentosRepository.save(apartamento);
    }

    public void delete(Long id) {
        apartamentosRepository.deleteById(id);
    }

    public List<Apartamentos> getByPropietarioId(Long propietarioId) {
        return apartamentosRepository.findByPropietarioId(propietarioId);
    }

    public List<Apartamentos> getByTorre(int torre) {
        return apartamentosRepository.findByTorre(torre);
    }
    

    public  Apartamentos  findByApartamento(int apartamento) {
        return apartamentosRepository.findByApartamento(apartamento);
    }

    // 🔍 Filtro por torre y/o piso
    public Page<Apartamentos> filterByTorreAndPiso(Integer torre, Integer piso, Pageable pageable) {
        if (torre != null && piso != null) {
            return apartamentosRepository.findByTorreAndPiso(torre, piso, pageable);
        } else if (torre != null) {
            return apartamentosRepository.findByTorre(torre, pageable);
        } else if (piso != null) {
            return apartamentosRepository.findByPiso(piso, pageable);
        } else {
            return getPaginatedApartments(pageable);
        }
    }

    public Page<Apartamentos> searchByNombreOrApartamento(String keyword, Pageable pageable) {
        return apartamentosRepository.searchByNombreOrApartamento(keyword.toLowerCase(), pageable);
    }

    public boolean existsByApartamento(int piso) {
        return apartamentosRepository.existsByApartamento(piso);
    }

    public List<Apartamentos> findByObservaciones(Observaciones observaciones) {
        return apartamentosRepository.findByObservaciones(observaciones);
    }

    
}
