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

    // Método para agregar un producto (CORREGIDO)
    public boolean agregarProductos(Productos producto) {
        String sql = "INSERT INTO productos (nombre, precio, cantidad, categoria, proveedor, codigo) VALUES (?, ?, ?, ?, ?, ?)";
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
            ps.setString(6, producto.getCodigo());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(conn, ps, null);
        }
    }

    // Método para obtener un producto por ID (CORREGIDO)
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
                        rs.getString("proveedor"),
                        rs.getString("codigo")
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
                        rs.getString("proveedor"),
                        rs.getString("codigo")
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
        String sql = "UPDATE productos SET nombre = ?, precio = ?, cantidad = ?, categoria = ?, proveedor = ?, codigo = ? WHERE id = ?";
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
            ps.setString(6, producto.getCodigo());
            ps.setInt(7, producto.getId());

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

    // CORREGIDO: Método de búsqueda interactiva 
    public List<Productos> buscarProductosInteractivo(String termino) {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE " +
                    "LOWER(nombre) LIKE LOWER(?) OR " +
                    "LOWER(codigo) LIKE LOWER(?) OR " +
                    "CAST(id AS CHAR) LIKE ? " +
                    "ORDER BY nombre " +
                    "LIMIT 10";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            String parametroBusqueda = "%" + termino + "%";

            ps.setString(1, parametroBusqueda); // nombre
            ps.setString(2, parametroBusqueda); // codigo
            ps.setString(3, parametroBusqueda); // id

            rs = ps.executeQuery();

            while (rs.next()) {
                Productos producto = new Productos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("categoria"),
                        rs.getString("proveedor"),
                        rs.getString("codigo")
                );
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error en búsqueda interactiva: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, ps, rs);
        }

        return productos;
    }

    // Método para buscar productos por nombre
    public List<Productos> buscarProductosPorNombre(String nombre) {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY id";
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
                        rs.getString("proveedor"),
                        rs.getString("codigo")
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

    // Búsqueda específica por código
    public List<Productos> buscarProductosPorCodigo(String codigo) {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE LOWER(codigo) LIKE LOWER(?) ORDER BY id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + codigo + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Productos producto = new Productos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("categoria"),
                        rs.getString("proveedor"),
                        rs.getString("codigo")
                );
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar productos por código: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, ps, rs);
        }

        return productos;
    }

    /**
     Lista todos los productos de la base de datos
     */
    public List<Productos> listarTodosLosProductos() throws SQLException {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, cantidad, categoria, proveedor, codigo FROM productos ORDER BY id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexion.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Productos producto = new Productos();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setProveedor(rs.getString("proveedor"));
                producto.setCodigo(rs.getString("codigo"));

                productos.add(producto);
            }
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

    // CORREGIDO: Método para cerrar recursos 
    private void cerrarRecursos(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close(); 
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}