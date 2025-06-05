/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package menuproductos;

import Conexion.Conexion;
import java.sql.Connection;

public class MenuProductos {

    public static void main(String[] args) {
        // Instanciar las vistas correctamente

        // Establecer la conexión
        Conexion conexion = new Conexion();
        Connection conn = conexion.conectar();

    }
}
