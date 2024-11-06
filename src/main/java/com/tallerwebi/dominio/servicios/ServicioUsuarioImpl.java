package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioUsuario;
import com.tallerwebi.dominio.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        Usuario usuario = repositorioUsuario.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Usuario no encontrado para id: " + id);
        }
        return usuario;
    }


    @Override
    public void actualizarSaldo(Long id, Double nuevoSaldo) {
        Usuario usuario = obtenerUsuarioPorId(id);
        if (usuario != null) {
            usuario.setSaldo(nuevoSaldo);
            repositorioUsuario.modificar(usuario);
        }
    }
}
