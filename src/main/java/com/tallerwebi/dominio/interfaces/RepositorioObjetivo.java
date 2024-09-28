package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Objetivo;

public interface RepositorioObjetivo {

    Objetivo buscarObjetivo(Integer id, String nombre);
    void guardar(Objetivo objetivo);
    Egreso buscar(Double metaMonto);
    void modificar(Objetivo objetivo);
}
