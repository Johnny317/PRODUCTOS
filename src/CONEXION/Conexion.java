/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Productos";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "1234"; 

    public Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos:");
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
