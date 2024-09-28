package com.tallerwebi.dominio.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "objetivo")
public class Objetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double metaMonto;
    private String nombre;

    @Temporal(TemporalType.DATE)
    private Date fechaLimite;

    // Constructor, getters y setters
    public Objetivo(Integer id, Double metaMonto, String nombre, Date fechaLimite) {
        this.id = id;
        this.metaMonto = metaMonto;
        this.nombre = nombre;
        this.fechaLimite = fechaLimite;
    }

    public Objetivo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMetaMonto() {
        return metaMonto;
    }

    public void setMetaMonto(Double metaMonto) {
        this.metaMonto = metaMonto;
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
}

