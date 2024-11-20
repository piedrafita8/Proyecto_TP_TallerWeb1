package com.tallerwebi.dominio.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "objetivo")
public class Objetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double montoObjetivo;
    private Double montoActual;
    private String nombre;
    private Long userId;

    @Temporal(TemporalType.DATE)
    private Date fechaLimite;

    // Constructor vacío requerido por JPA
    public Objetivo() {
    }

    // Constructor con todos los campos excepto id
    public Objetivo(String nombre, Double montoObjetivo, Date fechaLimite, Long userId) {
        this.nombre = nombre;
        this.montoObjetivo = montoObjetivo;
        this.fechaLimite = fechaLimite;
        this.montoActual = 0.0;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMontoObjetivo() {
        return montoObjetivo;
    }

    public void setMontoObjetivo(Double metaMonto) {
        this.montoObjetivo = metaMonto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Double getMontoActual() {
        return montoActual;
    }

    public void setMontoActual(Double montoActual) {
        this.montoActual = montoActual;
    }
}

