package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario {

    Usuario buscarUsuario(String username, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    public Usuario buscarPorId(Long id);
    void modificar(Usuario usuario);
}

