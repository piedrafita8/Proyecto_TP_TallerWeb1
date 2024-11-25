package com.tallerwebi.dominio.servicios;


import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioDeuda;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import com.tallerwebi.dominio.models.Deuda;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioDeudaImpl implements ServicioDeuda {

    private final RepositorioDeuda repositorioDeuda;

    public ServicioDeudaImpl(RepositorioDeuda repositorioDeuda) {
        this.repositorioDeuda = repositorioDeuda;
    }

    @Override
    public void agregarDeuda(Deuda deuda) {
        repositorioDeuda.guardar(deuda);
    }

    @Override
    public void eliminarDeuda(Long id) throws RecursoNoEncontrado {
        Deuda deuda = repositorioDeuda.obtenerDeudasPorUsuario(id, false)
                .stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (deuda == null) {
            throw new RecursoNoEncontrado("No se encontró la deuda con ID: " + id);
        }

        repositorioDeuda.eliminar(id);
    }

    @Override
    public void marcarDeudaComoPagada(Long id) throws RecursoNoEncontrado {
    
        List<Deuda> todasLasDeudas = repositorioDeuda.obtenerDeudasPorUsuario(id, true);
        todasLasDeudas.addAll(repositorioDeuda.obtenerDeudasPorUsuario(id, false));

        Deuda deuda = todasLasDeudas.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (deuda == null) {
            throw new RecursoNoEncontrado("No se encontró la deuda con ID: " + id);
        }

        repositorioDeuda.marcarComoPagada(id);
    }

    @Override
    public List<Deuda> obtenerDeudasQueMeDeben(Long userId) {
        return repositorioDeuda.obtenerDeudasPorUsuario(userId, false);
    }

    @Override
    public List<Deuda> obtenerDeudasQueDebo(Long userId) {
        return repositorioDeuda.obtenerDeudasPorUsuario(userId, true);
    }
}