package com.example.urbify.models;
import java.util.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "Observaciones", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"apartamento_id"})
})
public class Observaciones {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "apartamento_id", nullable = false)
    private Apartamentos apartamento;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Enumerated(EnumType.STRING)        
    private Tipo tipo;
    
    @Column(name = "Correspondencia", nullable = false, length = 50)
    private String correspondencia;

    public Observaciones() {
    }

    public Observaciones(Apartamentos apartamento, boolean active, Date createdAt, Tipo tipo,
            String correspondencia) {
        this.apartamento = apartamento;
        this.active = active;
        this.createdAt = createdAt;
        this.tipo = tipo;
        this.correspondencia = correspondencia;
    }

     public enum Tipo {
        Encomienda,
        Recibo,
        Otro
    }

    public Apartamentos getApartamento() {
        return apartamento;
    }

    public void setApartamento(Apartamentos apartamento) {
        this.apartamento = apartamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getCorrespondencia() {
        return correspondencia;
    }

    public void setCorrespondencia(String correspondencia) {
        this.correspondencia = correspondencia;
    }

    @Override
    public String toString() {
        return "Observaciones [id=" + id + ", apartamento=" + apartamento + ", active=" + active + ", createdAt="
                + createdAt + ", tipo=" + tipo + ", correspondencia=" + correspondencia + ", getApartamento()="
                + getApartamento() + ", getId()=" + getId() + ", isActive()=" + isActive() + "]";
    }

    

}
