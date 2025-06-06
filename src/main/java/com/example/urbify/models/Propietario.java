package com.example.urbify.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Propietario", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "identification"),
})
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "identification", unique = true, nullable = false, length = 20)
    private String identification;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "telephone", nullable = false, length = 50)
    private long telephone;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "apartamento_id")
    private Apartamentos apartamento;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    public Propietario() {
    }


    public Propietario(String firstName, String lastName, String identification,
            String email, long telephone, Admin admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
        this.email = email;
        this.telephone = telephone;
        this.admin = admin;
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
        this.updatedAt = new Date();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.updatedAt = new Date();
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
        this.updatedAt = new Date();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = new Date();
    }

    public long getTelephone() {
        return telephone;
    }

    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        this.updatedAt = new Date();
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

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @Override
    public String toString() {
        return "Propietario [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", identification="
                + identification + ", email=" + email + ", telephone=" + telephone + ", admin=" + admin
                + ", apartamento=" + apartamento + ", active=" + active + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

}
