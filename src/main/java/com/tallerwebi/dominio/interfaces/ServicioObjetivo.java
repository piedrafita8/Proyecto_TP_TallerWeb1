package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;

import java.util.List;

public interface ServicioObjetivo {

    Objetivo consultarObjetivo(Integer id);
    void crearObjetivo(Objetivo objetivo) throws ObjetivoExistente;
    public void actualizarObjetivo(Integer id, Double montoAAgregar, Long userId);
    void eliminarObjetivo(Integer id);

    List<Objetivo> obtenerTodosLosObjetivos();
    List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long Id);

    public void aportarAObjetivo(Integer objetivoId, Long userIdAportante, Double montoAportado);
    public void guardarObjetivoConAportacion(Objetivo objetivo, Long userId, Double montoAportado);
}
