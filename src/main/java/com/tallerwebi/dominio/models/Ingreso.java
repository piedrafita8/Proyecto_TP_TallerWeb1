package com.tallerwebi.dominio.models;


import javax.persistence.*;

import com.tallerwebi.dominio.enums.TipoIngreso;

import java.time.LocalDate;

@Entity
@Table(name = "Ingreso")
public class Ingreso extends Transaccion {

    @Enumerated(EnumType.STRING)
private TipoIngreso tipoIngreso;

    public Ingreso(Integer id, Double monto, String descripcion, LocalDate fecha, Long userId, TipoIngreso tipoIngreso) {
        super(id, monto, descripcion, fecha, userId);
        this.tipoIngreso = tipoIngreso;
    }

    public Ingreso() {

    }

public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(TipoIngreso tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }
}

