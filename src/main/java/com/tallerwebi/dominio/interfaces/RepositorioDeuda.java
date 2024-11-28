package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;

import java.util.List;

import org.springframework.stereotype.Repository;


public interface RepositorioDeuda {
    public void guardar(Deuda deuda);
    public void eliminar(Long id);
    public List<Deuda> obtenerDeudasPorUsuario(Usuario usuario,TipoDeuda tipoDeuda);
    public void marcarComoPagada(Long id);
    public Deuda obtenerPorId(Long id);
}
