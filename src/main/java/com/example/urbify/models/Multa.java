package com.example.urbify.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "multas")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartamento_id", nullable = false)
    private Apartamentos apartamento;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(length = 255)
    private String descripcion;

    private LocalDateTime fecha;

    public Multa() {
    }

    public Multa(Apartamentos apartamento, BigDecimal valor, String estado, String descripcion) {
        this.apartamento = apartamento;
        this.valor = valor;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public Apartamentos getApartamento() {
        return apartamento;
    }

    public void setApartamento(Apartamentos apartamento) {
        this.apartamento = apartamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Multa [id=" + id + ", apartamento=" + apartamento + ", valor=" + valor + ", estado=" + estado
                + ", descripcion=" + descripcion + ", fecha=" + fecha + "]";
    }

    

    
}

