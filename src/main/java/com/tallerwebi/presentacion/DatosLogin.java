package com.tallerwebi.presentacion;

public class DatosLogin {

    private String email;  // Usamos email en lugar de username
    private String password;

    public DatosLogin() {
    }

    public DatosLogin(String email, String password) {
        this.email = email;
        this.password = password;
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
}
