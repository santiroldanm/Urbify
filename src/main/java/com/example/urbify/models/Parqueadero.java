package com.example.urbify.models;
import jakarta.persistence.*;
import com.example.urbify.models.Vehicle.TipoVehiculo;

@Entity
@Table(name = "parqueadero")
public class Parqueadero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name = "ocupado", nullable = false, length = 50)
    private int ocupado;

    @Column(name = "espacio", nullable = false, length = 50)
    private Integer espacio;

    @Enumerated(EnumType.STRING)        
    private TipoVehiculo tipo;

    @OneToOne
    @JoinColumn(name = "vehicle_id")  
    private Vehicle vehicle;


    public Parqueadero() {
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

    public Integer getEspacio() {
        return espacio;
    }

    public void setEspacio(Integer espacio) {
        this.espacio = espacio;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Parqueadero [id=" + id + ", ocupado=" + ocupado + ", espacio=" + espacio
                + ", tipo=" + tipo + ", vehicle=" + vehicle + "]";
    }

 
 

}
