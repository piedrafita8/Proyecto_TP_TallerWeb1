package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.ServicioLogin;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario RepositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario RepositorioUsuario){
        this.RepositorioUsuario = RepositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return RepositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = RepositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente("Usuario ya existe");
        }
        RepositorioUsuario.guardar(usuario);
    }

}

