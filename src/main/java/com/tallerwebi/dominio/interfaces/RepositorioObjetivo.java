package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Objetivo;

import java.util.List;

public interface RepositorioObjetivo {

    public Objetivo buscarObjetivo(Integer id);
    public void crearObjetivo(Objetivo objetivo);
    public List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long userId);
    public List<Objetivo> obtenerTodosLosObjetivos();
    public void eliminarObjetivo(Integer id);
    public void guardar(Objetivo objetivo);
}
