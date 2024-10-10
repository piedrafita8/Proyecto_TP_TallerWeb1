package com.tallerwebi.dominio.models;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.enums.TipoMovimiento;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Egreso")
public class Egreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double monto;
    private String descripcion;
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private TipoEgreso tipoEgreso;
    private TipoMovimiento tipoMovimiento;

    // Constructor
    public Egreso(Integer id, Double monto, String descripcion, LocalDate fecha, TipoEgreso tipoEgreso, TipoMovimiento tipoMovimiento) {
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipoEgreso = tipoEgreso;
        this.tipoMovimiento = tipoMovimiento;
    }

    public Egreso(Double monto, String descripcion, LocalDate fecha, TipoEgreso tipoEgreso, TipoMovimiento tipoMovimiento) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipoEgreso = tipoEgreso;
        this.tipoMovimiento = tipoMovimiento;
    }

    // Constructor por default
    public Egreso() {

    }

    // Getters y Setters
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

    public TipoEgreso getTipoEgreso() {return this.tipoEgreso;}

    public void setTipoEgreso(TipoEgreso tipoEgreso) {this.tipoEgreso = tipoEgreso;}

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
}

