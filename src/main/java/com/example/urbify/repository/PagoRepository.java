package com.example.urbify.repository;

import com.example.urbify.models.Pago;
import com.example.urbify.models.Apartamentos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByEstado(String estado);


    @Query("SELECT SUM(p.valor) FROM Pago p WHERE UPPER(p.estado) = 'PAGADO'")
    Long getTotalRecaudado();


    @Query("SELECT SUM(p.valor) FROM Pago p WHERE UPPER(p.estado) IN ('MOROSO', 'PENDIENTE')")
    Long getDeudaTotal();


    @Query("SELECT COUNT(DISTINCT p.apartamento.id) FROM Pago p WHERE UPPER(p.estado) = 'MOROSO'")
    Long countApartamentosMorosos();


    @Query("SELECT COUNT(DISTINCT p.apartamento.id) FROM Pago p WHERE UPPER(p.estado) = 'PAGADO'")
    Long countPagosDelMes();

    @Query("SELECT p.mes FROM Pago p WHERE p.id = :id")
    String findMesById(@Param("id") Long id);


    @Query("SELECT p FROM Pago p WHERE " +
            "(:keyword IS NULL OR LOWER(p.apartamento.propietario.firstName) LIKE CONCAT('%', :keyword, '%') OR LOWER(p.apartamento.propietario.lastName) LIKE CONCAT('%', :keyword, '%') OR CAST(p.apartamento.apartamento AS string) LIKE CONCAT('%', :keyword, '%')) "
            +
            "AND (:mes IS NULL OR p.mes = :mes) " +
            "AND (:estado IS NULL OR p.estado = :estado) " +
            "AND (:torre IS NULL OR p.apartamento.torre = :torre)")
    Page<Pago> buscarPagosConFiltros(@Param("keyword") String keyword,
            @Param("mes") String mes,
            @Param("estado") String estado,
            @Param("torre") Integer torre,
            Pageable pageable);

    @Query("SELECT p FROM Pago p " +
            "WHERE (:keyword IS NULL OR " +
            "LOWER(p.apartamento.propietario.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.apartamento.propietario.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "STR(p.apartamento.apartamento) LIKE CONCAT('%', :keyword, '%'))")
    Page<Pago> buscarPorKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT p.apartamento FROM Pago p WHERE p.estado = 'MOROSO' OR p.estado = 'PENDIENTE'")
    List<Apartamentos> findApartamentosConPagosMorososOPendientes();

    Optional<Pago> findTopByApartamentoOrderByUpdatedAtDesc(Apartamentos apartamento);



    Page<Pago> findByApartamento_Apartamento(Integer apartamento, Pageable pageable);

    Page<Pago> findByApartamento_Propietario_FirstNameContainingIgnoreCaseOrApartamento_Propietario_LastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);

        List<Pago> findByApartamento(Apartamentos apartamento);
}
