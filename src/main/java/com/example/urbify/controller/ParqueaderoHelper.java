package com.example.urbify.controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;
import com.example.urbify.models.Visitante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.urbify.service.ObservacionesService;
import com.example.urbify.service.VehicleService;
import com.example.urbify.service.VisitanteService;

import jakarta.persistence.EnumType;

import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Observaciones;
import com.example.urbify.models.Observaciones.Tipo;
import com.example.urbify.models.Vehicle.TipoPropietario;
import com.example.urbify.models.Vehicle.TipoVehiculo;
import com.example.urbify.models.Parqueadero;
import com.example.urbify.models.Vehicle;

@Component
public class ParqueaderoHelper {

    @Autowired
    private VisitanteService visitanteService;
    @Autowired
    private ObservacionesService observacionesService;
    @Autowired
    private VehicleService vehicleService;

    public boolean estaOcupado(List<Parqueadero> parqueaderos, int espacio, String tipo) {
        boolean ocupado = parqueaderos.stream()
                .anyMatch(p -> p.getEspacio() == espacio && tipo.equals(p.getVehicle().getType().name())
                        && p.getOcupado() == 1);
        System.out.println("Comprobando espacio " + espacio + " y tipo " + tipo + ": " + ocupado);
        return ocupado;
    }

    public boolean estaOcupadito(List<Parqueadero> parqueaderos, int espacio, String tipo) {
        boolean ocupado = parqueaderos.stream()
                .anyMatch(p -> p.getEspacio() == espacio && tipo.equals(p.getVehicle().getType().name())
                        && p.getOcupado() == 0);
        System.out.println("Comprobando espacio " + espacio + " y tipo " + tipo + ": " + ocupado);
        return ocupado;
    }

    public String porEspacio(List<Parqueadero> parqueaderos, int espacio, String tipo) {
        for (int i = 0; i < parqueaderos.size(); i++) {
            // Verifica si el espacio coincide con el parqueadero en la posición 'i'
            if (parqueaderos.get(i).getEspacio() == espacio
                    && tipo.equals(parqueaderos.get(i).getVehicle().getType().name())) {
                // Si el parqueadero está ocupado, devuelve el nombre completo del propietario
                if (parqueaderos.get(i).getVehicle() != null) {
                    Vehicle vehicle = parqueaderos.get(i).getVehicle();
                    return vehicle.getFirstName() + " " + vehicle.getLastName() + " " + vehicle.getPlate();
                } else {
                    // Si no hay vehículo en el parqueadero, devuelve un mensaje indicando que está libre
                    return "Sin vehículo";
                }
            }
        }
        // Si no se encuentra el parqueadero con el espacio indicado, devuelve un mensaje de error
        return "Espacio no encontrado";
    }

    public boolean esVisitante(List<Parqueadero> parqueaderos, int espacio, String tipo) {
        // Recorre todos los parqueaderos
        for (int i = 0; i < parqueaderos.size(); i++) {
            // Verifica si el espacio coincide con el parqueadero en la posición 'i'
            if (parqueaderos.get(i).getEspacio() == espacio
                    && tipo.equals(parqueaderos.get(i).getVehicle().getType().name())) {
                // Si el parqueadero está ocupado y el tipo de vehículo es "visitante"
                if (parqueaderos.get(i).getVehicle() != null) {
                    Vehicle vehicle = parqueaderos.get(i).getVehicle();
                    // Verifica si el tipo del vehículo es "visitante"
                    if ("Visitante".equalsIgnoreCase(vehicle.getTipo().name())) {
                        return true;
                    }
                }
            }
        }
        return false; // Si no es un visitante o si el espacio está vacío
    }

    public List<Vehicle> obtenerVehiculosDisponibles(List<Vehicle> vehiculos, List<Parqueadero> parqueaderos) {
        if (vehiculos == null || vehiculos.isEmpty()) {
            return Collections.emptyList();
        }

        if (parqueaderos == null || parqueaderos.isEmpty()) {
            return vehiculos;
        }

        Set<Long> idsOcupados = parqueaderos.stream()
                .filter(p -> Objects.nonNull(p.getVehicle()))
                .map(p -> p.getVehicle().getId())
                .collect(Collectors.toSet());

        System.out.println("IDs de vehículos ocupados: " + idsOcupados);

        List<Vehicle> disponibles = vehiculos.stream()
                .filter(v -> !idsOcupados.contains(v.getId()) && v.isActive())
                .collect(Collectors.toList());

        System.out.println("Vehículos disponibles:");
        disponibles.forEach(
                v -> System.out.println(" - " + v.getFirstName() + " " + v.getLastName() + " (" + v.getPlate() + ")"));

        return disponibles;
    }

    public List<Visitante> Encontrar(Apartamentos apartamento) {
        List<Visitante> visitante = visitanteService.findByApartamentoId(apartamento.getId());
        return visitante;
    }

    public List<Vehicle> EncontrarV(Apartamentos apartamento) {
       TipoPropietario tipo = TipoPropietario.Visitante;
        List<Vehicle> visitante = vehicleService.findByApartamentoIdAndTipo(apartamento.getId(), tipo);
        return visitante;
    }

    public List<Observaciones> EncontarObs(Tipo tipo, long id) {
        return observacionesService.findByTipoAndApartamentoId(tipo, id);
    }
}