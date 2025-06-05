/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package menuproductos;
import VISTA.VistaPrincipal;
import Conexion.Conexion;
import java.sql.Connection;

public class MenuProductos {
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al configurar Look and Feel: " + ex.getMessage());
        }
        
        // Establecer la conexión (opcional, solo para verificar)
        Conexion conexion = new Conexion();
        Connection conn = conexion.conectar();
        
        java.awt.EventQueue.invokeLater(() -> {
    VistaPrincipal vista = new VistaPrincipal();
    vista.setVisible(true);
});
    }
}
