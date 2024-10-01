package com.tallerwebi.dominio;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/BaseDeDatosApiTallerWeb1?serverTimezone=UTC";
        String userName = "root";
        String password = "fghdqr";

        System.out.println("Cargando...");

        try {
            Connection conexion = DriverManager.getConnection(url, userName, password);
            Statement statement = conexion.createStatement();

            ResultSet resultado = statement.executeQuery("SELECT * FROM Egreso");

            while (resultado.next()) {
                System.out.println(resultado.getString("id_egreso") + " | " +
                        resultado.getString("monto") + " | " +
                        resultado.getString("descripcion") + " | " +
                        resultado.getString("fecha"));
            }

            System.out.println("Conexion exitosa");

            conexion.close();
            statement.close();
            resultado.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
