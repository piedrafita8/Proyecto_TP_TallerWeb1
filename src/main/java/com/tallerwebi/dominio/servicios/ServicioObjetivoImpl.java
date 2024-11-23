package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.interfaces.ServicioObjetivo;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
import com.tallerwebi.dominio.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service("servicioObjetivo")
@Transactional
public class ServicioObjetivoImpl implements ServicioObjetivo {

    private final RepositorioObjetivo repositorioObjetivo;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioEgreso servicioEgreso;

    @Autowired
    public ServicioObjetivoImpl(RepositorioObjetivo repositorioObjetivo, RepositorioUsuario repositorioUsuario, ServicioEgreso servicioEgreso) {
        this.repositorioObjetivo = repositorioObjetivo;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioEgreso = servicioEgreso;
    }

    @Override
    public Objetivo consultarObjetivo(Integer id) {
        return repositorioObjetivo.buscarObjetivo(id);
    }

    @Override
    public void crearObjetivo(Objetivo objetivo) throws ObjetivoExistente {
        Objetivo objetivoExistente = repositorioObjetivo.buscarObjetivo(objetivo.getId());
        if (objetivoExistente != null) {
            throw new ObjetivoExistente("El objetivo ya existe");
        }
        repositorioObjetivo.crearObjetivo(objetivo);
    }

    // Refactorizar y usar un solo metodo que me sirva.
    @Override
    @Transactional
    public void actualizarObjetivo(Integer id, Double montoAAgregar, Long userId) throws SaldoInsuficiente {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        Usuario usuario = repositorioUsuario.buscarPorId(userId);

        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no existe.");
        }

        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe.");
        }

        if (usuario.getSaldo() < montoAAgregar) {
            throw new SaldoInsuficiente("Saldo insuficiente para actualizar el objetivo.");
        }

        // Actualizar el objetivo y el saldo del usuario
        objetivo.setMontoActual(objetivo.getMontoActual() + montoAAgregar);
        usuario.setSaldo(usuario.getSaldo() - montoAAgregar);

        // Guardar los cambios
        repositorioObjetivo.guardar(objetivo);
        repositorioUsuario.modificar(usuario);

        // Registrar el egreso sin volver a modificar el saldo
        Egreso egreso = new Egreso();
        egreso.setMonto(montoAAgregar);
        egreso.setDescripcion("Actualización del objetivo: " + objetivo.getNombre());
        egreso.setFecha(LocalDate.now());
        egreso.setTipoEgreso(TipoEgreso.APORTE);
        egreso.setUserId(userId);

        // Usar un método específico para solo registrar el egreso sin modificar el saldo
        servicioEgreso.registrarEgresoSinActualizarSaldo(egreso);
    }

    @Override
    @Transactional
    public void aportarAObjetivo(Integer objetivoId, Long userIdAportante, Double montoAportado) {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(objetivoId);
        Usuario usuarioAportante = repositorioUsuario.buscarPorId(userIdAportante);

        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no existe.");
        }

        if (usuarioAportante.getSaldo() < montoAportado) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el aporte.");
        }

        objetivo.setMontoActual(objetivo.getMontoActual() + montoAportado);
        usuarioAportante.setSaldo(usuarioAportante.getSaldo() - montoAportado);

        repositorioObjetivo.guardar(objetivo);
        // Se puede refactorizar
        repositorioUsuario.modificar(usuarioAportante);

        Egreso egreso = new Egreso();
        egreso.setMonto(montoAportado);
        egreso.setDescripcion("Actualización del objetivo: " + objetivo.getNombre());
        egreso.setFecha(LocalDate.now());
        egreso.setTipoEgreso(TipoEgreso.APORTE);
        egreso.setUserId(usuarioAportante.getId());

        servicioEgreso.registrarEgresoSinActualizarSaldo(egreso);
    }

    public void guardarObjetivoConAportacion(Objetivo objetivo, Long userId, Double montoAportado) {
        // Guardar el objetivo o actualizarlo
        repositorioObjetivo.guardar(objetivo);

        // Crear el egreso correspondiente
        Egreso egreso = new Egreso();
        egreso.setMonto(montoAportado);
        egreso.setDescripcion("Aportación al objetivo: " + objetivo.getNombre());
        egreso.setFecha(LocalDate.now());
        egreso.setTipoEgreso(TipoEgreso.APORTE); // Suponiendo que tienes un enum para los tipos de egresos
        egreso.setUserId(userId);

        // Guardar el egreso utilizando el servicio
        servicioEgreso.crearEgreso(egreso, userId);
    }


    @Override
    public void eliminarObjetivo(Integer id) {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        if (objetivo != null) {
            repositorioObjetivo.eliminarObjetivo(id);
        }
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivos() {
        return repositorioObjetivo.obtenerTodosLosObjetivos();
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long id) {
        return repositorioObjetivo.obtenerTodosLosObjetivosPorUsuario(id);
    }

}
