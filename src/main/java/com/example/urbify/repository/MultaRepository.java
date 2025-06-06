package com.example.urbify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.urbify.models.Multa;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

}

