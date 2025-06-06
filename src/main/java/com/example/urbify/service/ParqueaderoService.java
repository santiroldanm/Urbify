package com.example.urbify.service;
import org.springframework.stereotype.Service;
import com.example.urbify.models.Parqueadero;
import com.example.urbify.models.Vehicle;
import com.example.urbify.models.Vehicle.TipoVehiculo;
import com.example.urbify.models.Vigilant;
import com.example.urbify.repository.ParqueaderoRepository;

 import java.util.List;
 
import java.util.Optional;

@Service
public class ParqueaderoService {

    private ParqueaderoRepository parqueaderoRepository;

    public ParqueaderoService(ParqueaderoRepository parqueaderoRepository) {
        this.parqueaderoRepository = parqueaderoRepository;
    }

    public boolean existByVehiclePlate(String Plate) {
        return parqueaderoRepository.existsByVehicle_Plate(Plate);
    }

    public List<Parqueadero> findAll() {
        return parqueaderoRepository.findAll();
    }

    public Parqueadero save(Parqueadero parqueadero) {
        return parqueaderoRepository.save(parqueadero);
    }

    public void delete(Long id) {
        parqueaderoRepository.deleteById(id);
    }

    public List<Parqueadero> listarPorTipo(TipoVehiculo type) {
        return parqueaderoRepository.findByTipo(type);
    }


    public Optional<Parqueadero> findById(Long id) {
        return parqueaderoRepository.findById(id);
    }

    public boolean existsByid(Long id) {
        return parqueaderoRepository.existsByid(id);
    }

    public boolean existsByespacio(int espacio) {
        return parqueaderoRepository.existsByespacio(espacio);
    }
    
    public Optional<Parqueadero> findByEspacio(int espacio) {
        return parqueaderoRepository.findByEspacio(espacio);
    }

    public Optional<Parqueadero> findByEspacioAndTipo(Integer espacio, TipoVehiculo tipo) {
         return parqueaderoRepository.findByEspacioAndTipo(espacio, tipo);
    }

    public boolean existsByEspacioAndTipo(Integer espacio, TipoVehiculo tipo) {
        return parqueaderoRepository.existsByEspacioAndTipo(espacio, tipo);
        
    }

    public boolean existsByOcupado(int ocupado) {
        return parqueaderoRepository.existsByOcupado(ocupado);
        
    }
    public Optional<Parqueadero> getById(Long id) {
        return parqueaderoRepository.findById(id);
    }

    public boolean existsByVehicle(Vehicle vehicle) {
        return parqueaderoRepository.existsByVehicle(vehicle);
    }

 

    
}