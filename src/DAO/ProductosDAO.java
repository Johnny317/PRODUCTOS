/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import Conexion.Conexion;
import Modelo.Productos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {
    private Conexion conexion;
    
    public ProductosDAO() {
        this.conexion = new Conexion();
    }
    
    // Método para agregar un producto
    public boolean agregarProductos(Productos producto) {
        String sql = "INSERT INTO productos (nombre, precio, cantidad, categoria, proveedor) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getCantidad());
            ps.setString(4, producto.getCategoria());
            ps.setString(5, producto.getProveedor());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(conn, ps, null);
        }
    }
    
    // Método para obtener un producto por ID
    public Productos obtenerProductoPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Productos(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("categoria"),
                    rs.getString("proveedor")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, ps, rs);
        }
        
        return null;
    }
    
    // Método para obtener todos los productos
    public List<Productos> obtenerTodosLosProductos() {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Productos producto = new Productos(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("categoria"),
                    rs.getString("proveedor")
                );
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, ps, rs);
        }
        
        return productos;
    }
    
    // Método para actualizar un producto
    public boolean actualizarProducto(Productos producto) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, cantidad = ?, categoria = ?, proveedor = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getCantidad());
            ps.setString(4, producto.getCategoria());
            ps.setString(5, producto.getProveedor());
            ps.setInt(6, producto.getId());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(conn, ps, null);
        }
    }
    
    // Método para eliminar un producto
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(conn, ps, null);
        }
    }
    
    // Método para buscar productos por nombre (búsqueda parcial)
    public List<Productos> buscarProductosPorNombre(String nombre) {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? ORDER BY id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Productos producto = new Productos(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("categoria"),
                    rs.getString("proveedor")
                );
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al buscar productos: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, ps, rs);
        }
        
        return productos;
    }
    
    // Método para verificar si existe un producto con ese ID
    public boolean existeProducto(int id) {
        String sql = "SELECT COUNT(*) FROM productos WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia del producto: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, ps, rs);
        }
        
        return false;
    }
    
    // Método para cerrar recursos
    private void cerrarRecursos(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
