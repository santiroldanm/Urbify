package com.example.urbify.repository;

import com.example.urbify.models.ObjetosPerdidos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObjetosPerdidosRepository extends JpaRepository<ObjetosPerdidos, Long> {
       List<ObjetosPerdidos> findByEstado(ObjetosPerdidos.EstadoObjeto estado);
}