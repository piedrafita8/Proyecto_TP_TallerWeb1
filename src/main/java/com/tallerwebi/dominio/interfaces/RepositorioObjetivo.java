package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Objetivo;

import java.util.List;

public interface RepositorioObjetivo {

    Objetivo buscarObjetivo(Integer id);
    void crearObjetivo(Objetivo objetivo);
    void actualizarObjetivo(Integer id, Double montoAAgregar);
    List<Objetivo> obtenerTodosLosObjetivos();
    List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long Id);
    void eliminarObjetivo(Integer id);
    public void guardar(Objetivo objetivo);
}
