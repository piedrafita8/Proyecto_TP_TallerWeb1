package com.tallerwebi.presentacion;

public class DatosEgreso {

    private Double monto;
    private String descripcion;
    private Integer fecha;

    public DatosEgreso() {

    }

    public DatosEgreso(Double monto, String descripcion, Integer fecha) {
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

    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }
}
