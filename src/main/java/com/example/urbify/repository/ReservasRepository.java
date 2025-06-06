package com.example.urbify.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.ReservaCancha;
import com.example.urbify.models.ReservaCancha.Cancha;


public interface ReservasRepository extends JpaRepository<ReservaCancha,Long> {
    boolean existsByid(Long id);
    List<ReservaCancha> findByApartamento(Apartamentos apartamento);
    List<ReservaCancha> findByCancha(Cancha canchaType);

}