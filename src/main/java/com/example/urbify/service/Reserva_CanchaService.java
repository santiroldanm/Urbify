package com.example.urbify.service;

import com.example.urbify.models.Propietario;
import com.example.urbify.models.ReservaCancha;
import com.example.urbify.models.ReservaCancha.Cancha; // Importa el enum Cancha
import com.example.urbify.repository.ReservasRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator; // Para encontrar la fecha más tardía
import java.util.Date; // Para trabajar con java.util.Date
import java.util.List;
import java.util.Optional; // Para manejar el resultado de Optional en el stream

@Service
public class Reserva_CanchaService {

    private final ReservasRepository reservasRepository;

    @Autowired
    public Reserva_CanchaService(ReservasRepository reservasRepository) {
        this.reservasRepository = reservasRepository;
    }

    public boolean existsById(Long id) {
        return reservasRepository.existsById(id);
    }

    public Optional<ReservaCancha> findById(Long id) {
        return reservasRepository.findById(id);
    }

    public List<ReservaCancha> findAll() {
        return reservasRepository.findAll();
    }

    public void delete(Long id) {
        reservasRepository.deleteById(id);
    }
    /**
     * Verifica si la hora de inicio de una nueva reserva (newReservationStartTime)
     * es posterior a la hora de fin más tardía de cualquier reserva existente
     * para la misma cancha.
     *
     * @param newReservationStartTime La hora de inicio de la nueva reserva que se
     *                                intenta guardar.
     * @param canchaType              El tipo de cancha para el cual se realiza la
     *                                verificación (PLACA, SINTETICA).
     * @return {@code true} si la hora de inicio de la nueva reserva es
     *         estrictamente posterior a la hora de fin
     *         más tardía de las reservas existentes para esa cancha.
     *         Retorna {@code true} si no hay reservas existentes para la cancha, ya
     *         que no hay
     *         una hora de fin anterior con la que comparar.
     *         Retorna {@code false} si la nueva hora de inicio es igual o anterior
     *         a la hora de fin
     *         más tardía existente.
     */

    public boolean VerificarSiEstaenelRango(Date newReservationStartTime, Cancha canchaType) {
        List<ReservaCancha> existingReservas = reservasRepository.findByCancha(canchaType);

        for (ReservaCancha existingReserva : existingReservas) {
            Date existingStartTime = existingReserva.getCreatedAt();
            Date existingEndTime = existingReserva.getUpdatedAt();

            if (existingStartTime != null && existingEndTime != null) {
                
                if ((newReservationStartTime.after(existingStartTime)
                        || newReservationStartTime.equals(existingStartTime)) &&
                        newReservationStartTime.before(existingEndTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void BorrarReservasPasadas() {
        Date now = new Date(); 
        List<ReservaCancha> allReservas = reservasRepository.findAll();

        for (ReservaCancha reserva : allReservas) {

            if (reserva.getUpdatedAt() != null && (reserva.getUpdatedAt().before(now) || reserva.getUpdatedAt().equals(now))) {
                reservasRepository.delete(reserva); 
                System.out.println("Reserva eliminada: " + reserva.getId() + " - Hora Fin: " + reserva.getUpdatedAt()); 
            }
        }
    }

    public ReservaCancha save(ReservaCancha reserva) {
        return reservasRepository.save(reserva);
    }
}