package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoMovimiento;

import java.time.LocalDate;

public class DatosTransaccion {

    // Atributos
    private Double monto;
    private TipoMovimiento tipo;
    private String comentario;
    private LocalDate fecha;

    // Contructor por Default
    public DatosTransaccion() {

    }

    public DatosTransaccion(Double monto, TipoMovimiento tipo, String comentario, LocalDate fecha) {
        this.monto = monto;
        this.tipo = tipo;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    // Getters and Setters
    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
