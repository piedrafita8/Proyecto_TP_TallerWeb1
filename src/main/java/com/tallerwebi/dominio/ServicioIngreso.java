package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioIngreso {
    List<Ingreso> getAllIngresos();
    Ingreso crearIngreso(Ingreso ingreso);
    // Otros métodos
}
