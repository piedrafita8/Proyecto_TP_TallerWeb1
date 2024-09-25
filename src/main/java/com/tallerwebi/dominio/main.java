package com.tallerwebi.dominio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main {

    public static Connection conexionBD(String bd){
        Connection conexion;
        String host = "jdbc:mysql://127.0.0.1:3306";
        String user = "root";
        String pass = "";

        System.out.println("Cargando...");

        try {
            conexion = DriverManager.getConnection(host+bd, user, pass);
            System.out.println("Conexion exitosa");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return conexion;
    }

    public static void main(String[] args) {
        Connection bd = conexionBD("BaseDeDatosApiTallerWeb1");
    }

}
