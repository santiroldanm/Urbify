package com.example.urbify.repository;

import com.example.urbify.models.Salones;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonesRepository extends JpaRepository<Salones, Long> {
    List<Salones> findByTipoEspacio(String tipoEspacio);
}