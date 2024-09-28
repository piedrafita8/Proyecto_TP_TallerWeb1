package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;

public interface ServicioObjetivo {

    Objetivo consultarObjetivo(Integer id, String nombre);
    void registrar(Objetivo objetivo) throws ObjetivoExistente;

}
