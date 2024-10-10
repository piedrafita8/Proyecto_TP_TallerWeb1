package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoMovimiento;

import java.time.LocalDate;

public class DatosIngreso {

    private Integer id;
    private Double monto;
    private String descripcion;
    private LocalDate fecha;

    // Constructor, getters y setters
    public DatosIngreso(Integer id, Double monto, String descripcion, LocalDate fecha) {
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public DatosIngreso() {

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
