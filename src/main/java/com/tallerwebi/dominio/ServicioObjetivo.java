package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ObjetivoNoEncontrado;

public interface ServicioObjetivo {

    Objetivo consultarObjetivo(Integer metaMonto, String nombre);
    void registrar(Objetivo objetivo) throws ObjetivoNoEncontrado;

}
