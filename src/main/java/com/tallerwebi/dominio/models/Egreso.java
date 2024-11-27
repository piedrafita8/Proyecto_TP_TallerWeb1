package com.tallerwebi.dominio.models;

import com.tallerwebi.dominio.enums.TipoEgreso;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("EGRESO")
public class Egreso extends Transaccion {
    @Enumerated(EnumType.STRING)
    private TipoEgreso tipoEgreso;

    // Constructores
    public Egreso() {}

    public Egreso(Double monto, String descripcion, LocalDate fecha, Usuario usuario, TipoEgreso tipoEgreso) {
        super(monto, descripcion, fecha, usuario);
        this.tipoEgreso = tipoEgreso;
    }

    // Getters y setters
    public TipoEgreso getTipoEgreso() {
        return tipoEgreso;
    }

    public void setTipoEgreso(TipoEgreso tipoEgreso) {
        this.tipoEgreso = tipoEgreso;
    }
}

