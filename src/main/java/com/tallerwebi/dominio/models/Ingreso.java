package com.tallerwebi.dominio.models;

import com.tallerwebi.dominio.enums.TipoIngreso;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("INGRESO")
public class Ingreso extends Transaccion {
    @Enumerated(EnumType.STRING)
    private TipoIngreso tipoIngreso;

    // Constructores
    public Ingreso() {}

    public Ingreso(Double monto, String descripcion, LocalDate fecha, Usuario usuario, TipoIngreso tipoIngreso) {
        super(monto, descripcion, fecha, usuario);
        this.tipoIngreso = tipoIngreso;
    }

    // Getters y setters
    public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(TipoIngreso tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }
}

