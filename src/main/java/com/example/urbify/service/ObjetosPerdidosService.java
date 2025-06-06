package com.example.urbify.service;

import com.example.urbify.models.ObjetosPerdidos;
import com.example.urbify.models.Residente; // Para marcar como reclamado
import com.example.urbify.repository.ObjetosPerdidosRepository;
import com.example.urbify.repository.ResidenteRepository; // Si vas a asociar residentes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ObjetosPerdidosService {

    @Autowired
    private ObjetosPerdidosRepository objetosPerdidosRepository;

    @Autowired(required = false) 
    private ResidenteRepository residenteRepository; 

    public List<ObjetosPerdidos> findAll() {
        return objetosPerdidosRepository.findAll();
    }

    public Optional<ObjetosPerdidos> findById(Long id) {
        return objetosPerdidosRepository.findById(id);
    }

    public ObjetosPerdidos save(ObjetosPerdidos objeto) {
        return objetosPerdidosRepository.save(objeto);
    }

    public void deleteById(Long id) {
        objetosPerdidosRepository.deleteById(id);
    }


    public void markAsReclamado(Long id, String nombreQuienReclama) {
    Optional<ObjetosPerdidos> objetoOptional = objetosPerdidosRepository.findById(id);
    if (objetoOptional.isPresent()) {
        ObjetosPerdidos objeto = objetoOptional.get();
        objeto.setEstado(ObjetosPerdidos.EstadoObjeto.RECLAMADO);

        objeto.setNombreQuienReclamo(nombreQuienReclama);

        objeto.setFechaReclamo(new Date()); 

        objetosPerdidosRepository.save(objeto);
    } else {
        throw new RuntimeException("Objeto Perdido con ID " + id + " no encontrado.");
    }
}

} 
    

