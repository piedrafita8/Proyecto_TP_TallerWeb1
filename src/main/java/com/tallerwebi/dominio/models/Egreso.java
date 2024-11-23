package com.tallerwebi.dominio.models;

import com.tallerwebi.dominio.enums.TipoEgreso;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "egresos")
public class Egreso extends Transaccion{

    // Atributos
    private TipoEgreso tipoEgreso;

    // Constructor por default
    public Egreso(Integer id, Double monto, String descripcion, LocalDate fecha, Long userId, TipoEgreso tipoEgreso) {
        super(id, monto, descripcion, fecha, userId);
        this.tipoEgreso = tipoEgreso;
    }

    public Egreso() {}

    // Getters y Setters
    public TipoEgreso getTipoEgreso() {
        return tipoEgreso;
    }

    public void setTipoEgreso(TipoEgreso tipoEgreso) {
        this.tipoEgreso = tipoEgreso;
    }

}

