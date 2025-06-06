package com.example.urbify.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pagos_administracion")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "apartamento_id")
    private Apartamentos apartamento;

    @Column(name = "mes", nullable = false)
    private String mes; 

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "estado", nullable = false)
    private String estado; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_pago")
    private Date fechaPago;

    @Column(name = "metodo_pago")
    private String metodoPago; 
    @Column(name = "recibo_url")
    private String reciboUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt = new Date();


    public Pago() {
    }


    public Pago(Apartamentos apto, String mes, Double valor, String estado) {
        this.apartamento = apto;
        this.mes = mes;
        this.valor = valor;
        this.estado = estado;
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

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        this.updatedAt = new Date();
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
        this.updatedAt = new Date();
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getReciboUrl() {
        return reciboUrl;
    }

    public void setReciboUrl(String reciboUrl) {
        this.reciboUrl = reciboUrl;
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
}
