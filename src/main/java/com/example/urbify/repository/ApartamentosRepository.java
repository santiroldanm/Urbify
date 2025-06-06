package com.example.urbify.repository;

import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Observaciones;
import com.example.urbify.models.Propietario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartamentosRepository extends JpaRepository<Apartamentos, Long> {

    List<Apartamentos> findByPropietarioIsNull();
    List<Apartamentos> findByPropietarioId(Long propietarioId);
    List<Apartamentos> findByTorre(int torre);
    List<Apartamentos> findByPropietario(Propietario propietario);
     Apartamentos  findByApartamento(int apartamento);
    List<Apartamentos> findByObservaciones(Observaciones observaciones);
    List<Apartamentos> findByActiveTrue();

    long countByActiveFalse();
    
    boolean existsByApartamento(int piso);

    Page<Apartamentos> findByTorre(int torre, Pageable pageable);
    Page<Apartamentos> findByPiso(int piso, Pageable pageable);
    Page<Apartamentos> findByTorreAndPiso(int torre, int piso, Pageable pageable);
    
    @Query("SELECT a FROM Apartamentos a WHERE " +
           "(a.propietario IS NOT NULL AND " +
           "LOWER(CONCAT(a.propietario.firstName, ' ', a.propietario.lastName)) LIKE %:keyword%) " +
           "OR CAST(a.apartamento AS string) LIKE %:keyword%")
    Page<Apartamentos> searchByNombreOrApartamento(@Param("keyword") String keyword, Pageable pageable);
}
