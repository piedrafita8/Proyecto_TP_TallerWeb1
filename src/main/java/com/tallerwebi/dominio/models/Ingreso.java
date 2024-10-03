package com.tallerwebi.dominio.models;

import com.tallerwebi.dominio.enums.TipoMovimiento;

import javax.persistence.*;

@Entity
@Table(name = "Ingreso")
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double monto;
    private String descripcion;
    private TipoMovimiento tipo_movimiento;
    private Integer fecha;

    // Constructor, getters y setters
    public Ingreso(TipoMovimiento tipoMovimiento, Integer id, Double monto, String descripcion, Integer fecha) {
        this.tipo_movimiento = tipoMovimiento;
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
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

    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }

    public TipoMovimiento getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(TipoMovimiento tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }
}

