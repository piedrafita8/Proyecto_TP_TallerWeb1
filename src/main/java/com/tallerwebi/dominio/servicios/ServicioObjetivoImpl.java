package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.enums.CategoriaObjetivo;
import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioObjetivo;
import com.tallerwebi.dominio.interfaces.ServicioTransaccion;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
import com.tallerwebi.dominio.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("servicioObjetivo")
@Transactional
public class ServicioObjetivoImpl implements ServicioObjetivo {

    private final RepositorioObjetivo repositorioObjetivo;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioTransaccion servicioTransaccion;

    @Autowired
    public ServicioObjetivoImpl(RepositorioObjetivo repositorioObjetivo,
                                RepositorioUsuario repositorioUsuario,
                                ServicioTransaccion servicioTransaccion) {
        this.repositorioObjetivo = repositorioObjetivo;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioTransaccion = servicioTransaccion;
    }

    @Override
    public void crearObjetivo(String nombre, Double montoObjetivo, Date fechaLimite,
                              CategoriaObjetivo categoria, Long userId) throws ObjetivoExistente {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Objetivo objetivo = new Objetivo(nombre, montoObjetivo, fechaLimite, usuario);
        objetivo.setCategoria(categoria); // Establece la categoría
        usuario.addObjetivo(objetivo);
        repositorioObjetivo.crearObjetivo(objetivo);
    }

    @Override
    public List<Objetivo> buscarObjetivosPorFiltros(String emailUsuario, CategoriaObjetivo categoria) {
        Usuario usuario = null;
        if (emailUsuario != null && !emailUsuario.isEmpty()) {
            usuario = repositorioUsuario.buscar(emailUsuario);
            if (usuario == null) {
                return Collections.emptyList();
            }
        }

        return repositorioObjetivo.buscarObjetivosPorFiltros(usuario, categoria);
    }

    @Override
    @Transactional
    public void actualizarObjetivo(Integer id, Double montoAAgregar, Long userId) throws SaldoInsuficiente {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        if (objetivo == null || !objetivo.getUsuario().getId().equals(userId)) {
            throw new IllegalArgumentException("El objetivo no existe o no pertenece al usuario.");
        }

        Usuario usuario = objetivo.getUsuario();
        if (usuario.getSaldo() < montoAAgregar) {
            throw new SaldoInsuficiente("Saldo insuficiente para actualizar el objetivo.");
        }

        Egreso egreso = new Egreso();
        egreso.setMonto(montoAAgregar);
        egreso.setDescripcion("Aporte al objetivo: " + objetivo.getNombre());
        egreso.setFecha(LocalDate.now());
        egreso.setTipoEgreso(TipoEgreso.APORTE);
        egreso.setUserId(userId);

        objetivo.setMontoActual(objetivo.getMontoActual() + montoAAgregar);
        usuario.setSaldo(usuario.getSaldo() - montoAAgregar);
        usuario.addTransaccion(egreso);

        repositorioObjetivo.guardar(objetivo);
        repositorioUsuario.modificar(usuario);

        servicioTransaccion.registrarTransaccionSinActualizarSaldo(egreso);
    }

    @Override
    @Transactional
    public void aportarAObjetivo(Integer id, Double montoAportado, Long userIdAportante, String emailDeUsuarioAportado) throws SaldoInsuficiente {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        Usuario usuarioAportante = repositorioUsuario.buscarPorId(userIdAportante);

        // Si se proporciona un email, buscar el usuario aportado (opcional)
        Usuario usuarioAportado = emailDeUsuarioAportado != null
                ? repositorioUsuario.buscar(emailDeUsuarioAportado)
                : null;

        if (usuarioAportante == null || objetivo == null || montoAportado <= 0) {
            throw new IllegalArgumentException("Usuario, objetivo o monto inválidos");
        }

        if (usuarioAportante.getSaldo() < montoAportado) {
            throw new SaldoInsuficiente("Saldo insuficiente para realizar el aporte.");
        }

        Egreso egreso = new Egreso();
        egreso.setMonto(montoAportado);
        egreso.setDescripcion("Aporte al objetivo: " + objetivo.getNombre());
        egreso.setFecha(LocalDate.now());
        egreso.setTipoEgreso(TipoEgreso.APORTE);
        egreso.setUserId(userIdAportante);

        objetivo.setMontoActual(objetivo.getMontoActual() + montoAportado);
        usuarioAportante.setSaldo(usuarioAportante.getSaldo() - montoAportado);
        usuarioAportante.addTransaccion(egreso);
        usuarioAportante.agregarObjetivoAportado(objetivo);

        repositorioObjetivo.guardar(objetivo);
        repositorioUsuario.modificar(usuarioAportante);

        servicioTransaccion.registrarTransaccionSinActualizarSaldo(egreso);
    }

    public List<Objetivo> obtenerObjetivosAportados(Long userId) {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe.");
        }

        return new ArrayList<>(usuario.getObjetivosAportados());
    }

    @Override
    public Objetivo consultarObjetivo(Integer id) {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no existe.");
        }
        return objetivo;
    }

    @Override
    public void eliminarObjetivo(Integer id) {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no existe.");
        }

        Usuario usuario = objetivo.getUsuario();
        usuario.removeObjetivo(objetivo);

        repositorioObjetivo.eliminarObjetivo(id);
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivos() {
        return repositorioObjetivo.obtenerTodosLosObjetivos();
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long userId) {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe.");
        }

        return repositorioObjetivo.obtenerTodosLosObjetivosPorUsuario(userId);
    }
}