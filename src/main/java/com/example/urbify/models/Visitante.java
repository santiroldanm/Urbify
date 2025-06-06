package com.example.urbify.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "visitante")
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "identificacion", nullable = false, unique = true, length = 50)
    private String identificacion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "apartamento_id")
    private Apartamentos apartamento;

    @Column(name = "motivo_visita", length = 255)
    private String motivo_visita;

    @Column(name = "fecha_ingreso", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_ingreso;

    @Column(name = "fecha_salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_salida;

    @ManyToOne
    @JoinColumn(name = "registrado_por")
    private Vigilant registradoPor; // Relación con el Vigilant que registra al visitante

    @Column(name = "activo", nullable = false)
    private boolean active;

    // Constructor vacío requerido por JPA
    public Visitante() {
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Apartamentos getApartamento() {
        return apartamento;
    }

    public void setApartamento(Apartamentos apartamento) {
        this.apartamento = apartamento;
    }

    public String getMotivoVisita() {
        return motivo_visita;
    }

    public void setMotivoVisita(String motivoVisita) {
        this.motivo_visita = motivoVisita;
    }

    public Date getFechaIngreso() {
        return fecha_ingreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fecha_ingreso = fechaIngreso;
    }

    public Date getFechaSalida() {
        return fecha_salida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fecha_salida = fechaSalida;
    }

    public Vigilant getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(Vigilant registradoPor) {
        this.registradoPor = registradoPor;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Visitante [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", identificacion="
                + identificacion + ", telefono=" + telefono + ", apartamento=" + apartamento + ", motivo_visita="
                + motivo_visita + ", fecha_ingreso=" + fecha_ingreso + ", fecha_salida=" + fecha_salida
                + ", registradoPor=" + registradoPor + ", active=" + active + "]";
    }

}