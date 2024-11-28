package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.models.*;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioDeuda;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import com.tallerwebi.dominio.models.Deuda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;


@Service
public class ServicioDeudaImpl implements ServicioDeuda {

    private final RepositorioDeuda repositorioDeuda;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioDeudaImpl(RepositorioDeuda repositorioDeuda, RepositorioUsuario repositorioUsuario) {
        this.repositorioDeuda = repositorioDeuda;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional
    public void agregarDeuda(Deuda deuda) {
        repositorioDeuda.guardar(deuda);
    }

    @Override
    @Transactional
    public void eliminarDeuda(Long id) throws RecursoNoEncontrado {
        Deuda deuda = repositorioDeuda.obtenerPorId(id);
        if (deuda == null) {
            throw new RecursoNoEncontrado("Deuda con ID " + id + " no encontrada.");
        }
        repositorioDeuda.eliminar(id);
    }

    @Override
    @Transactional
    public void marcarDeudaComoPagada(Long id) throws RecursoNoEncontrado {
        Deuda deuda = repositorioDeuda.obtenerPorId(id);
        if (deuda == null) {
            throw new RecursoNoEncontrado("Deuda con ID " + id + " no encontrada.");
        }
        deuda.setPagado(true);
        repositorioDeuda.guardar(deuda);
    }

    @Override
    @Transactional
    public List<Deuda> obtenerDeudasQueMeDeben(Long userId) {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario == null) {
            throw new RecursoNoEncontrado("Usuario con ID " + userId + " no encontrado.");
        }
        return repositorioDeuda.obtenerDeudasPorUsuario(usuario, TipoDeuda.ME_DEBEN);
    }

    @Override
    @Transactional
    public List<Deuda> obtenerDeudasQueDebo(Long userId) {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario == null) {
            throw new RecursoNoEncontrado("Usuario con ID " + userId + " no encontrado.");
        }
        return repositorioDeuda.obtenerDeudasPorUsuario(usuario, TipoDeuda.DEBO);
    }
}