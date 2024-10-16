package com.tallerwebi.dominio.models;

import com.tallerwebi.dominio.enums.TipoIngreso;
import com.tallerwebi.dominio.enums.TipoMovimiento;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Ingreso")
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double monto;
    private String descripcion;
    @Enumerated(EnumType.STRING)
    private TipoIngreso tipoIngreso;
    private LocalDate fecha;

    // Constructor, getters y setters
    public Ingreso(TipoIngreso tipoIngreso, Integer id, Double monto, String descripcion, LocalDate fecha) {
        this.tipoIngreso = tipoIngreso;
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Ingreso() {

    }

    public Ingreso(Double monto, String descripcion, LocalDate fecha) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


    public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(TipoIngreso tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }
}

