package com.tallerwebi.dominio.interfaces;

import java.util.List;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.models.Deuda;

import org.springframework.stereotype.Service;

@Service
public interface ServicioDeuda {

    public void agregarDeuda(Deuda deuda);
    public void eliminarDeuda(Long id) throws RecursoNoEncontrado;
    public void marcarDeudaComoPagada(Long id) throws RecursoNoEncontrado ;
    public List<Deuda> obtenerDeudasQueMeDeben(Long userId);
    public List<Deuda> obtenerDeudasQueDebo(Long userId);

}
