package com.example.urbify.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Apartamentos", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "torre", "piso", "apartamento" })
})
public class Apartamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "piso", nullable = false)
    private int piso;

    @Column(name = "torre", nullable = false)
    private int torre;

    @Column(name = "apartamento", nullable = false)
    private int apartamento;

    @OneToMany(mappedBy = "apartamento", cascade = CascadeType.ALL)
    private List<Residente> residentes;

    @Column(name = "paquete")
    private String paquete;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "propietario_id", nullable = true)
    private Propietario propietario;

    @OneToMany(mappedBy = "apartamento", cascade = CascadeType.ALL)
    private List<Observaciones> observaciones;

    public Apartamentos() {
    }

    public Apartamentos(int piso, int torre, int apartamento) {
        this.piso = piso;
        this.torre = torre;
        this.apartamento = apartamento;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public int getTorre() {
        return torre;
    }

    public void setTorre(int torre) {
        this.torre = torre;
    }

    public int getApartamento() {
        return apartamento;
    }

    public void setApartamento(int apartamento) {
        this.apartamento = apartamento;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
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

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
        this.active = propietario != null;
        this.updatedAt = new Date();
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public List<Residente> getResidentes() {
        return residentes;
    }

    public void setResidentes(List<Residente> residentes) {
        this.residentes = residentes;
    }

    @Override
    public String toString() {
        return "Apartamentos [id=" + id + ", piso=" + piso + ", torre=" + torre + ", apartamento=" + apartamento
                + ", residentesCount=" + (residentes != null ? residentes.size() : 0)
                + ", paquete=" + paquete + ", active=" + active + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt
                + ", propietarioId=" + (propietario != null ? propietario.getId() : null)
                + ", observacionesCount=" + (observaciones != null ? observaciones.size() : 0) + "]";
    }

}
