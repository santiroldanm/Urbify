package com.example.urbify.service;

import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Propietario;
import com.example.urbify.repository.ApartamentosRepository;
import com.example.urbify.repository.PropietarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropietarioService {

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private ApartamentosRepository apartamentosRepository;

    public List<Propietario> getAll() {
        return propietarioRepository.findAll();
    }

    public Propietario findById(Long id) {
        return propietarioRepository.findById(id).orElse(null);
    }

    public Propietario save(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }

    @Transactional
    public void delete(Long id) {
        Propietario propietario = propietarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
        List<Apartamentos> apartamentos = apartamentosRepository.findByPropietario(propietario);

        for (Apartamentos apt : apartamentos) {
            apt.setPropietario(null);
            apt.setActive(false); 
            apartamentosRepository.save(apt);
        }

        propietarioRepository.delete(propietario);
    }

    public Propietario getByEmail(String email) {
        return propietarioRepository.findByEmail(email);
    }

    public Propietario findByIdentification(String id){
        return propietarioRepository.findByIdentification(id);
    }

    public boolean existsByIdentification(String id) {
        return propietarioRepository.existsByIdentification(id);
    }
    

}
