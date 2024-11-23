package com.tallerwebi.dominio.models;

import com.tallerwebi.dominio.enums.TipoIngreso;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Ingreso")
public class Ingreso extends Transaccion {

    // Atributos
    private TipoIngreso tipoIngreso;

    // Constructor, getters y setters
    public Ingreso(TipoIngreso tipoIngreso, Integer id, Double monto, String descripcion, LocalDate fecha, Long userId) {
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

