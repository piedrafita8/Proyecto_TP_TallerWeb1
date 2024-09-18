package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;

public class ServicioIngresoImpl<List> {

    @Autowired
    private RepositorioIngresoImpl ingresoRepository;

    @Override
    public List<Ingreso> getAllIngresos() {
        return ingresoRepository.findAll();
    }

    @Override
    public Ingreso crearIngreso(Ingreso ingreso) {
        return ingresoRepository.save(ingreso);
    }

    // Otros métodos

}
