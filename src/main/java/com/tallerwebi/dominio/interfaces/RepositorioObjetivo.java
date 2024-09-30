package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Objetivo;

import java.util.List;

public interface RepositorioObjetivo {

    Objetivo buscarObjetivo(Integer id);
    void crearObjetivo(Objetivo objetivo);
    void actualizarObjetivo(Integer id, Double montoAAgregar);
    List<Objetivo> obtenerTodosLosObjetivos();
    void eliminarObjetivo(Integer id);
}
