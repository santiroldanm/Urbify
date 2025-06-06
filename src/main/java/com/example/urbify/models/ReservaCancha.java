package com.example.urbify.models;

import jakarta.persistence.*;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "reserva_cancha")
public class ReservaCancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ocupado", nullable = false, length = 50)
    private int ocupado;

    @Enumerated(EnumType.STRING) 
    @Column(name = "cancha", nullable = false, length = 20)
    private Cancha cancha;

    @Column(name = "nombre_reservador", nullable = false, length = 50)
    private String name;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date createdAt;

    @Column(name = "hora_fin")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "apartamento_id") 
    private Apartamentos apartamento;


    public ReservaCancha() {
    }

    public enum Cancha {
        PLACA,
        SINTETICA
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOcupado() {
        return ocupado;
    }

    public void setOcupado(int ocupado) {
        this.ocupado = ocupado;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Apartamentos getApartamento() {
        return apartamento;
    }

    public void setApartamento(Apartamentos apartamento) {
        this.apartamento = apartamento;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reserva [id=" + id + ", ocupado=" + ocupado + ", cancha=" + cancha + ", name=" + name + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", apartamento=" + apartamento + "]";
    }

}
