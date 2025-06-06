package com.example.urbify.models;


import  jakarta.persistence.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "vehicle", uniqueConstraints = {
        @UniqueConstraint(columnNames = "plate"),
        @UniqueConstraint(columnNames = "identification")
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50) 
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "identification", nullable = false, length = 20)
    private String identification;

    @ManyToOne
    @JoinColumn(name = "apartamento_id")
    private Apartamentos apartamento;

    @Column(name = "plate",unique = true, nullable = false, length = 120)
    private String plate;

    @Column(name = "tipo_propietario", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TipoPropietario tipo;
    
    @Column(name = "vehicle_type", nullable = false, length = 120)
    @Enumerated(EnumType.STRING)
    private TipoVehiculo type;
    
    @Column(name = "active")
    private boolean active = true;

    @Column(name = "Contador")
    private int contador;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")  
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "departure_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureTime;


    public Vehicle() {
    }
    public enum TipoVehiculo {
        Moto,
        Carro
    }
    public enum TipoPropietario {
        Propietario,
        Visitante
    }

    @ManyToOne
    @JoinColumn(name = "vigilant_id", nullable= false) // Ajusta el nombre de la columna
    private Vigilant vigilant;


    public Vigilant getVigilant() {
        return vigilant;
    }

    public void setVigilant(Vigilant vigilant) {
        this.vigilant = vigilant;
    }


    public void registerDeparture() {
        this.departureTime = new Date();
        this.updatedAt = new Date();
        this.active = false;
    }


    public String calculateStayDuration() {
        if (this.createdAt == null || this.active == false) {
            return "No se ha registrado la entrada del vehículo.";
        }

        Date endTime;
        if (this.departureTime != null) {
            endTime = this.departureTime;
        } else {
            endTime = new Date();
        }

        long durationInMillis = endTime.getTime() - this.createdAt.getTime();

        long hours = TimeUnit.MILLISECONDS.toHours(durationInMillis);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) -
                TimeUnit.HOURS.toMinutes(hours);

        return String.format("%d horas, %d minutos", hours, minutes);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

 

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
    public TipoPropietario getTipo() {
        return tipo;
    }

    public void setTipo(TipoPropietario tipo) {
        this.tipo = tipo;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }


    public TipoVehiculo getType() {
        return type;
    }

    public void setType(TipoVehiculo type) {
        this.type = type;
    }
 public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }

    public Apartamentos getApartamento() {
        return apartamento;
    }

    public void setApartamento(Apartamentos apartamento) {
        this.apartamento = apartamento;
    }

    @Override
    public String toString() {
        return "Vehicle [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", identification="
                + identification + ", apartamento=" + apartamento + ", plate=" + plate + ", tipo=" + tipo + ", type="
                + type + ", active=" + active + ", contador=" + contador + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + ", departureTime=" + departureTime + ", vigilant=" + vigilant + "]";
    }




}