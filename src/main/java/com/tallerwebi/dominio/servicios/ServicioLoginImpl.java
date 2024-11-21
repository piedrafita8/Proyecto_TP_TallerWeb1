package com.tallerwebi.dominio.services;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioLogin;
import com.tallerwebi.dominio.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

 @Override
public Usuario consultarUsuario(String email, String password) {
    Usuario usuario = repositorioUsuario.buscarUsuario(email, password);

    if (usuario == null) {
        throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
    }

    return usuario;
}

   @Override
public void registrar(Usuario usuario) throws UsuarioExistente {
    System.out.println("Intentando registrar usuario: " + usuario);

    if (repositorioUsuario.buscar(usuario.getEmail()) != null) {
        throw new UsuarioExistente("El email ya está registrado.");
    }

    if (repositorioUsuario.buscarUsuario(usuario.getUsername(), usuario.getPassword()) != null) {
        throw new UsuarioExistente("El nombre de usuario ya está en uso.");
    }

    repositorioUsuario.guardar(usuario);
    System.out.println("Usuario registrado exitosamente: " + usuario);
}

}
