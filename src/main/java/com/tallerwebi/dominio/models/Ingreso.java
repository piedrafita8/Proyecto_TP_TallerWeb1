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
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private TipoIngreso tipoIngreso;
    private TipoMovimiento tipoMovimiento;

    // Constructor, getters y setters
    public Ingreso(Integer id, Double monto, String descripcion, LocalDate fecha, TipoMovimiento tipoMovimiento, TipoIngreso tipoIngreso) {
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.tipoIngreso = tipoIngreso;
    }

    public Ingreso() {

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

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(TipoIngreso tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }
}

