package Modelo;

public class Productos {
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private String categoria;
    private String proveedor;
    
    // Constructor vacío
    public Productos() {
    }
    
    // Constructor con parámetros (sin ID para nuevos productos)
    public Productos(String nombre, double precio, int cantidad, String categoria, String proveedor) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.proveedor = proveedor;
    }
    
    // Constructor completo (con ID)
    public Productos(int id, String nombre, double precio, int cantidad, String categoria, String proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.proveedor = proveedor;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", categoria='" + categoria + '\'' +
                ", proveedor='" + proveedor + '\'' +
                '}';
    }
}