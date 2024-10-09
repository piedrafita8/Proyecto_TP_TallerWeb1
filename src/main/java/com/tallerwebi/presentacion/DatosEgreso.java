package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoEgreso;

import java.time.LocalDate;

public class DatosEgreso {

    private Double monto;
    private String descripcion;
    private LocalDate fecha;


    public DatosEgreso() {

    }

    public DatosEgreso(Double monto, String descripcion, LocalDate fecha) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;

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
}
