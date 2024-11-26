package com.tallerwebi.dominio.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Objetivo> objetivos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaccion> transacciones = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_objetivos_aportados",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "objetivo_id")
    )
    private List<Objetivo> objetivosAportados = new ArrayList<>();




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

    public void addObjetivo(Objetivo objetivo) {
        objetivos.add(objetivo);
        objetivo.setUsuario(this);
    }

    public void removeObjetivo(Objetivo objetivo) {
        objetivos.remove(objetivo);
        objetivo.setUsuario(null);
    }

    // Getters y setters para objetivos
    public List<Objetivo> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<Objetivo> objetivos) {
        this.objetivos = objetivos;
    }

    public List<Objetivo> getObjetivosAportados() {
        return objetivosAportados;
    }

    public void setObjetivosAportados(List<Objetivo> objetivosAportados) {
        this.objetivosAportados = objetivosAportados;
    }
    
    public void agregarObjetivoAportado(Objetivo objetivo) {
        if (!this.objetivosAportados.contains(objetivo)) {
            this.objetivosAportados.add(objetivo);
        }
    }

    public void addTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
        transaccion.setUsuario(this);
    }

    // Getters y setters para transacciones
    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }
}
