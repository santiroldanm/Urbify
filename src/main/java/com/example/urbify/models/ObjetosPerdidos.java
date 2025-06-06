package com.example.urbify.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date; 

@Entity
@Table(name = "ObjetosPerdidos") 
public class ObjetosPerdidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "ubicacion_encontrado", nullable = false, length = 100)
    private String ubicacionEncontrado;

    @Column(name = "fecha_encontrado", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) 
    private LocalDateTime fechaEncontrado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vigilante_id", nullable = true) 
    private Vigilant reportadoPorVigilante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoObjeto estado;

    @Column(name = "nombre_quien_reclamo", nullable = true, length = 255)
    private String nombreQuienReclamo;

    @Column(name = "fecha_reclamo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReclamo; 

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    
    public enum EstadoObjeto {
        ENCONTRADO, 
        RECLAMADO, 
        DEVUELTO, 
        ARCHIVADO
    }


    public ObjetosPerdidos() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.estado = EstadoObjeto.ENCONTRADO;
    }


    public ObjetosPerdidos(String descripcion, String ubicacionEncontrado, LocalDateTime fechaEncontrado) {
        this(); 
        this.descripcion = descripcion;
        this.ubicacionEncontrado = ubicacionEncontrado;
        this.fechaEncontrado = fechaEncontrado;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacionEncontrado() {
        return ubicacionEncontrado;
    }

    public void setUbicacionEncontrado(String ubicacionEncontrado) {
        this.ubicacionEncontrado = ubicacionEncontrado;
    }

    public LocalDateTime getFechaEncontrado() {
        return fechaEncontrado;
    }

    public void setFechaEncontrado(LocalDateTime fechaEncontrado) {
        this.fechaEncontrado = fechaEncontrado;
    }

    public Vigilant getReportadoPorVigilante() {
        return reportadoPorVigilante;
    }

    public void setReportadoPorVigilante(Vigilant reportadoPorVigilante) {
        this.reportadoPorVigilante = reportadoPorVigilante;
    }

    public EstadoObjeto getEstado() {
        return estado;
    }

    public void setEstado(EstadoObjeto estado) {
        this.estado = estado;
        this.updatedAt = new Date();
    }

    public String getNombreQuienReclamo() {
        return nombreQuienReclamo;
    }

    public void setNombreQuienReclamo(String nombreQuienReclamo) {
        this.nombreQuienReclamo = nombreQuienReclamo;
    }

    public Date getFechaReclamo() {
        return fechaReclamo;
    }

    public void setFechaReclamo(Date fechaReclamo) {
        this.fechaReclamo = fechaReclamo;
        this.updatedAt = new Date(); 
    }

    public Date getCreatedAt() {
        return createdAt;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Override
    public String toString() {
        return "ObjetosPerdidos [id=" + id + ", descripcion=" + descripcion + ", ubicacionEncontrado="
                + ubicacionEncontrado + ", fechaEncontrado=" + fechaEncontrado + ", reportadoPorVigilante="
                + reportadoPorVigilante + ", estado=" + estado + ", nombreQuienReclamo=" + nombreQuienReclamo
                + ", fechaReclamo=" + fechaReclamo + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}