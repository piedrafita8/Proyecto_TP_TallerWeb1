package com.tallerwebi.dominio.servicios;

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
    public void crearObjetivo(String nombre, Double montoObjetivo, Date fechaLimite, Long userId) throws ObjetivoExistente {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Objetivo objetivo = new Objetivo(nombre, montoObjetivo, fechaLimite, usuario);
        usuario.addObjetivo(objetivo);
        repositorioObjetivo.crearObjetivo(objetivo);
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

        objetivo.setMontoActual(objetivo.getMontoActual() + montoAAgregar);
        usuario.setSaldo(usuario.getSaldo() - montoAAgregar);

        repositorioObjetivo.guardar(objetivo);

        Egreso egreso = new Egreso();
        egreso.setMonto(montoAAgregar);
        egreso.setDescripcion("Actualización del objetivo: " + objetivo.getNombre());
        egreso.setFecha(LocalDate.now());
        egreso.setTipoEgreso(TipoEgreso.APORTE);
        egreso.setUserId(userId);

        servicioTransaccion.registrarTransaccionSinActualizarSaldo(egreso);
    }

    @Override
    @Transactional
    public void aportarAObjetivo(Integer id, Double montoAportado, Long userIdAportante, String EmailDeusuarioAportado) throws SaldoInsuficiente {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        Usuario usuarioAportado = repositorioUsuario.buscar(EmailDeusuarioAportado);
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no existe.");
        }

        Usuario usuarioAportante = repositorioUsuario.buscarPorId(userIdAportante);
        if (usuarioAportante == null) {
            throw new IllegalArgumentException("Usuario aportante no encontrado.");
        }
        if (usuarioAportado == null) {
            throw new IllegalArgumentException("Usuario aportado no encontrado.");
        } else if (usuarioAportado.getObjetivos().isEmpty()) {
            throw new IllegalArgumentException("El usuario al que quiere aportar no tiene objetivos.");
        }
        if (montoAportado <= 0) {
            throw new IllegalArgumentException("No se puede aportar saldo negativo o igual a 0");
        }
        if (usuarioAportante.getSaldo() < montoAportado) {
            throw new SaldoInsuficiente("Saldo insuficiente para realizar el aporte.");
        }

        objetivo.setMontoActual(objetivo.getMontoActual() + montoAportado);
        usuarioAportante.setSaldo(usuarioAportante.getSaldo() - montoAportado);

        repositorioObjetivo.guardar(objetivo);

        Egreso egreso = new Egreso();
        egreso.setMonto(montoAportado);
        egreso.setDescripcion("Aporte al objetivo: " + objetivo.getNombre());
        egreso.setFecha(LocalDate.now());
        egreso.setTipoEgreso(TipoEgreso.APORTE);
        egreso.setUserId(userIdAportante);

        servicioTransaccion.registrarTransaccionSinActualizarSaldo(egreso);
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