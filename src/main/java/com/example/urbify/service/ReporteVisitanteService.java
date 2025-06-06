package com.example.urbify.service;

import com.example.urbify.models.ReporteVisitanteDTO;
import com.example.urbify.models.Vehicle;
import com.example.urbify.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteVisitanteService {

    private final VehicleRepository vehicleRepository;

    public ReporteVisitanteService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<ReporteVisitanteDTO> generarReporteVisitantes() {
        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();

        List<Object[]> visitasFrecuentes = vehicleRepository.encontrarVisitantesFrecuentes(mesActual, anioActual);


        Date fechaLimite = Date.from(
                LocalDate.now()
                        .minusDays(3)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());
        List<Vehicle> visitantesMasDe72Horas = vehicleRepository.encontrarVisitantesMasDe72Horas(fechaLimite);
        Set<String> placasLargas = visitantesMasDe72Horas.stream()
                .map(Vehicle::getPlate)
                .collect(Collectors.toSet());

        List<ReporteVisitanteDTO> reporte = new ArrayList<>();

        for (Object[] row : visitasFrecuentes) {
            String placa = (String) row[0];
            Number countRaw = (Number) row[1];
            String firstName = (String) row[2];
            String lastName = (String) row[3];
            Number aptoRaw = (Number) row[4];

            int count = countRaw.intValue();
            int apartamento = aptoRaw.intValue();
            boolean largo = placasLargas.contains(placa);
            String nombrePropietario = firstName + " " + lastName;

            reporte.add(new ReporteVisitanteDTO(
                    placa, largo, nombrePropietario, apartamento, count));

        }

        placasLargas.removeAll(
                reporte.stream()
                        .map(ReporteVisitanteDTO::getPlaca)
                        .collect(Collectors.toSet()));
        for (String placa : placasLargas) {
            Vehicle vehiculo = visitantesMasDe72Horas.stream()
                    .filter(v -> v.getPlate().equals(placa))
                    .findFirst()
                    .orElse(null);

            if (vehiculo != null) {
                String nombrePropietario = vehiculo.getFirstName() + " " + vehiculo.getLastName();
                int apartamento = vehiculo.getApartamento().getApartamento();

                reporte.add(new ReporteVisitanteDTO(
                        placa, true, nombrePropietario, apartamento, 1));

            }
        }

        return reporte;
    }
}