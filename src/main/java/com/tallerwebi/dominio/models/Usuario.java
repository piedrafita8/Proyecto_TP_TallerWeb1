package com.tallerwebi.dominio.models;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String rol;
    private Boolean activo = false;
    private Double saldo = 0.0;
   
     @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Deuda> deudasPendientes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Deuda> deudasPorCobrar = new LinkedHashSet<>();


    public Usuario(String username, String email, String password, String rol) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public Usuario() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public Set<Deuda> getDeudasPendientes() {
        return deudasPendientes;
    }

    public void setDeudasPendientes(Set<Deuda> deudasPendientes) {
        this.deudasPendientes = deudasPendientes;
    }

    public Set<Deuda> getDeudasPorCobrar() {
        return deudasPorCobrar;
    }

    public void setDeudasPorCobrar(Set<Deuda> deudasPorCobrar) {
        this.deudasPorCobrar = deudasPorCobrar;
    }
    public void agregarDeudaPendiente(Deuda deuda){
        this.deudasPendientes.add(deuda);
    }
    public void agregarDeudaPorCobrar(Deuda deuda){
        this.deudasPorCobrar.add(deuda);
    }
}
