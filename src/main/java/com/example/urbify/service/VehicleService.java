package com.example.urbify.service;
import com.example.urbify.models.Vehicle;
import com.example.urbify.models.Vehicle.TipoPropietario;
import com.example.urbify.models.Vehicle.TipoVehiculo;
import com.example.urbify.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> listAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getById(Long id) {
        return vehicleRepository.findById(id);
    }

    public void delete(Long id) {
        vehicleRepository.deleteById(id);
    }

    public boolean existsByPlate(String plate) {
        return vehicleRepository.existsByPlate(plate);
    }
    public boolean existsByIdentification(String identification) {
        return vehicleRepository.existsByIdentification(identification);
    }
    

  public boolean existsByid(Long id) {
        return vehicleRepository.existsByid(id);
    }

    public Vehicle findVehicleById(Long id) {
         return vehicleRepository.findById(id).orElse(null);  
    }

    public List<Vehicle> findByApartamentoId(Long Id) {
        return vehicleRepository.findByApartamentoId(Id);
    }

    public long countActiveVehicle(){
        return vehicleRepository.countByActiveTrue();
    }
        @Scheduled(cron = "0 0 0 1 * ?") 
    public void resetearContadoresMensuales() {
        vehicleRepository.resetearContadores();
     }

        public List<Vehicle> findByApartamentoIdAndTipo(Long Id, TipoPropietario tipo) {
            return vehicleRepository.findByApartamentoIdAndTipo(Id, tipo);
        }
    
}