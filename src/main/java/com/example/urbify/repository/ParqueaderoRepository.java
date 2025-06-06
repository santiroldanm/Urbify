package com.example.urbify.repository;

import com.example.urbify.models.Parqueadero;
import com.example.urbify.models.Vehicle;
import com.example.urbify.models.Vehicle.TipoVehiculo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParqueaderoRepository extends JpaRepository<Parqueadero, Long> {

    List<Parqueadero> findByTipo(TipoVehiculo tipo);

    @SuppressWarnings("null")
    List<Parqueadero> findAll();

    boolean existsByespacio(int espacio);

    boolean existsByid(Long id);

    Optional<Parqueadero> findByEspacio(int espacio);

    Optional<Parqueadero> findByEspacioAndTipo(Integer espacio, TipoVehiculo tipo);

    boolean existsByEspacioAndTipo(int espacio, TipoVehiculo tipo);

    boolean existsByOcupado(int ocupado);

    boolean existsByVehicle(Vehicle vehicle);

    boolean existsByVehicle_Plate(String plate);

 }