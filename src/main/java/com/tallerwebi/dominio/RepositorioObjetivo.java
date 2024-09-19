package com.tallerwebi.dominio;

public interface RepositorioObjetivo {

    Objetivo buscarObjetivo(Integer id, String nombre);
    void guardar(Objetivo objetivo);
    Egreso buscar(Double metaMonto);
    void modificar(Objetivo objetivo);
}
