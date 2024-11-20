package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;

import java.util.List;

public interface ServicioObjetivo {

    Objetivo consultarObjetivo(Integer id);
    void crearObjetivo(Objetivo objetivo) throws ObjetivoExistente;
    void actualizarObjetivo(Integer id, Double montoAAgregar);
    void eliminarObjetivo(Integer id);

    List<Objetivo> obtenerTodosLosObjetivos();
    List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long Id);
}
