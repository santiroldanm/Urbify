package com.example.urbify.repository;
import com.example.urbify.models.Parqueadero;
import com.example.urbify.models.Vehicle;
import com.example.urbify.models.Vehicle.TipoPropietario;
import com.example.urbify.models.Vehicle.TipoVehiculo;
import com.example.urbify.models.Visitante;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByPlate(String plate);
    boolean existsByIdentification(String identification);
    List<Vehicle> findByTipo(TipoVehiculo tipo);
    long countByActiveTrue();
    boolean existsByid(Long id);
    List<Vehicle> findByApartamentoId(Long Id);
    List<Vehicle> findByApartamentoIdAndTipo(Long Id, TipoPropietario tipo);

    @Query("SELECT v FROM Vehicle v " +
           "WHERE v.tipo = com.example.urbify.models.Vehicle.TipoPropietario.Visitante " +
           "AND ( (v.departureTime IS NULL AND v.createdAt <= :fechaLimite) OR " +
           "      (v.departureTime IS NOT NULL AND v.departureTime >= :fechaLimite) )")
    List<Vehicle> encontrarVisitantesMasDe72Horas(@Param("fechaLimite") Date fechaLimite);

    @Modifying
    @Transactional
    @Query("UPDATE Vehicle v SET v.contador = 0")
    void resetearContadores();


    @Query("SELECT v.plate, v.contador, v.firstName, v.lastName, v.apartamento.apartamento " + // <--- Aquí la selección corregida
           "FROM Vehicle v " +
           "WHERE FUNCTION('MONTH', v.createdAt) = :mes " +
           "AND FUNCTION('YEAR', v.createdAt) = :anio " +
           "AND v.tipo = com.example.urbify.models.Vehicle.TipoPropietario.Visitante " +
           "AND v.contador >= 3")
    List<Object[]> encontrarVisitantesFrecuentes(@Param("mes") int mes, @Param("anio") int anio);
}