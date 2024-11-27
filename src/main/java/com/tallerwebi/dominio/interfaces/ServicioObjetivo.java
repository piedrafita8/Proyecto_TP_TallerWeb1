package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.enums.CategoriaObjetivo;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;

import java.util.Date;
import java.util.List;

public interface ServicioObjetivo {

    public Objetivo consultarObjetivo(Integer id);
    public void eliminarObjetivo(Integer id);

    public List<Objetivo> obtenerTodosLosObjetivos();
    public List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long userId);

    public void crearObjetivo(String nombre, Double montoObjetivo, Date fechaLimite, CategoriaObjetivo categoria, Long userId) throws ObjetivoExistente;
    public void actualizarObjetivo(Integer id, Double montoAAgregar, Long userId) throws SaldoInsuficiente;
    public void aportarAObjetivo(Integer id, Double montoAportado, Long userIdAportante, String EmailDeusuarioAportado);

    public List<Objetivo> buscarObjetivosPorFiltros(String emailUsuario, CategoriaObjetivo categoria);
    public List<Objetivo> obtenerObjetivosAportados(Long userId);
}
