package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoMovimiento;

public class DatosIngreso {

    private Double monto;
    private String descripcion;
    private Integer fecha;

    // Constructor, getters y setters
    public DatosIngreso(Double monto, String descripcion, Integer fecha) {
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

    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }

}
