package com.example.urbify.service;


import com.example.urbify.models.Salones;
import com.example.urbify.repository.SalonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalonesService {

    @Autowired
    private SalonesRepository salonesRepository;

    public Salones save(Salones reserva) {
        return salonesRepository.save(reserva);
    }

    public List<Salones> findAll() {
        return salonesRepository.findAll();
    }

    public Salones findById(Long id) {
    return salonesRepository.findById(id).orElse(null);
}

public List<Salones> findByTipoEspacio(String tipoEspacio) {
    return salonesRepository.findByTipoEspacio(tipoEspacio);
}

    public void eliminarReserva(Long id) {
        salonesRepository.deleteById(id);
    }

    public void deleteById(Long id) {
        salonesRepository.deleteById(id);
    }
}
