package com.example.urbify.service;

import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Observaciones;
import com.example.urbify.models.Observaciones.Tipo; 
import com.example.urbify.repository.ApartamentosRepository;
import com.example.urbify.repository.ObservacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ObservacionesService {

    @Autowired
    private ObservacionesRepository observacionesRepository;

    @Autowired
    private ApartamentosRepository apartamentosRepository; 

    public List<Observaciones> findAllObservaciones() {
        return observacionesRepository.findAll();
    }

   
    public List<Observaciones> findByTipoAndApartamentoId(Tipo tipo, Long apartamentoId) {
        return observacionesRepository.findByTipoAndApartamentoId(tipo, apartamentoId);
    }

    public Optional<Observaciones> findObservacionesById(Long id) {
        return observacionesRepository.findById(id);
    }

    @Transactional
    public Observaciones saveObservacion(Observaciones observacion) {
        if (observacion.getApartamento() == null || observacion.getApartamento().getId() == null) {
            throw new IllegalArgumentException("La observación debe estar asociada a un apartamento.");
        }
        Apartamentos apartamento = apartamentosRepository.findById(observacion.getApartamento().getId())
                .orElseThrow(() -> new RuntimeException("Apartamento con ID " + observacion.getApartamento().getId() + " no encontrado."));

        observacion.setApartamento(apartamento);
        return observacionesRepository.save(observacion);
    }

    public void deleteObservacion(Long id) {
        if (!observacionesRepository.existsById(id)) {
            throw new RuntimeException("Observación con ID " + id + " no encontrada.");
        }
        observacionesRepository.deleteById(id);
    }

    @Transactional
    public Observaciones createPredefinedObservacion(Long apartamentoId, Tipo tipo) {
        Apartamentos apartamento = apartamentosRepository.findById(apartamentoId)
                .orElseThrow(() -> new RuntimeException("Apartamento con ID " + apartamentoId + " no encontrado."));

        Observaciones nuevaObservacion = new Observaciones();
        nuevaObservacion.setApartamento(apartamento);
        nuevaObservacion.setTipo(tipo);
        nuevaObservacion.setActive(true);
        nuevaObservacion.setCreatedAt(new Date()); 

        return nuevaObservacion; 
    }

}