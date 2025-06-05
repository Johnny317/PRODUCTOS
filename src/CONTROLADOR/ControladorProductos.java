package Controlador;

import DAO.ProductosDAO;
import Modelo.Productos;
import VISTA.VistaAgregarP;
import VISTA.VistaEditarP;
import VISTA.VistaEliminarP;
import VISTA.VistaListaP;
import VISTA.VistaPrincipal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorProductos {
    private ProductosDAO productoDAO;
    private JPanel panelContenedor;
    private CardLayout cardLayout;
    
    // Referencias a las vistas
    private VistaAgregarP vistaAgregar;
    private VistaEditarP vistaEditar;
    private VistaEliminarP vistaEliminar;
    private VistaListaP vistaLista;
    private VistaPrincipal vistaPrincipal;
    
    public ControladorProductos() {
        this.productoDAO = new ProductosDAO();
    }
    
    // Método para configurar las vistas y el panel contenedor
    public void configurarVistas(VistaPrincipal principal, VistaAgregarP agregar, 
                                VistaEditarP editar, VistaEliminarP eliminar, VistaListaP lista) {
        this.vistaPrincipal = principal;
        this.vistaAgregar = agregar;
        this.vistaEditar = editar;
        this.vistaEliminar = eliminar;
        this.vistaLista = lista;
        this.panelContenedor = principal.jPanelCONTENEDOR;
        this.cardLayout = new CardLayout();
        this.panelContenedor.setLayout(cardLayout);
        
        // Agregar vistas al panel contenedor
        panelContenedor.add(agregar, "AGREGAR");
        panelContenedor.add(editar, "EDITAR");
        panelContenedor.add(eliminar, "ELIMINAR");
        panelContenedor.add(lista, "LISTAR");
        
        configurarEventos();
        configurarComboBoxes();
        inicializarTablas();
    }
    
    // Configurar eventos de todos los botones
    private void configurarEventos() {
        // Botones de navegación en vista principal
        vistaPrincipal.jButtonVISTAAGREGARP.addActionListener(e -> mostrarVista("AGREGAR"));
        vistaPrincipal.jButtonVISTADITARP.addActionListener(e -> {
            actualizarTablaEditar();
            mostrarVista("EDITAR");
        });
        vistaPrincipal.jButtonVISTAELIMINARP.addActionListener(e -> {
            actualizarTablaEliminar();
            mostrarVista("ELIMINAR");
        });
        vistaPrincipal.jButtonVISTALISTAP.addActionListener(e -> {
            actualizarTablaListar();
            mostrarVista("LISTAR");
        });
        
        // Botones BACK
        vistaAgregar.jButtonBACK.addActionListener(e -> ocultarVistas());
        vistaEditar.jButtonBACK.addActionListener(e -> ocultarVistas());
        vistaEliminar.jButtonBACK.addActionListener(e -> ocultarVistas());
        vistaLista.jButtonBACKLIST.addActionListener(e -> ocultarVistas());
        
        // Botones de acción
        vistaAgregar.jButtonAGREGAR.addActionListener(e -> agregarProducto());
        vistaEditar.jButtonBUSCAREDIT.addActionListener(e -> buscarProductoParaEditar());
        vistaEditar.jButtonEDIT.addActionListener(e -> editarProducto());
        vistaEliminar.jButtonBUSCARELI.addActionListener(e -> buscarProductoParaEliminar());
        vistaEliminar.jButtonELIMINAR.addActionListener(e -> eliminarProducto());
    }
    
    // Configurar ComboBoxes con categorías
    private void configurarComboBoxes() {
        String[] categorias = {"Electrónicos", "Ropa", "Comida", "Hogar", "Deportes", "Libros", "Juguetes", "Salud"};
        
        vistaAgregar.jComboBoxCATEGORIA.setModel(new DefaultComboBoxModel<>(categorias));
        vistaEditar.jComboBoxCATEGORIAEDIT.setModel(new DefaultComboBoxModel<>(categorias));
        vistaEliminar.jComboBoxCATEGORIAELI.setModel(new DefaultComboBoxModel<>(categorias));
    }
    
    // Inicializar tablas con columnas
    private void inicializarTablas() {
        String[] columnas = {"ID", "Nombre", "Precio", "Cantidad", "Categoría", "Proveedor"};
        
        DefaultTableModel modeloEditar = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vistaEditar.jTableEDIT.setModel(modeloEditar);
        
        DefaultTableModel modeloEliminar = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vistaEliminar.jTableELI.setModel(modeloEliminar);
        
        DefaultTableModel modeloListar = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vistaLista.jTableList.setModel(modeloListar);
    }
    
    // Mostrar una vista específica
    private void mostrarVista(String vista) {
        cardLayout.show(panelContenedor, vista);
        panelContenedor.setVisible(true);
    }
    
    // Ocultar todas las vistas y mostrar la principal
    private void ocultarVistas() {
        panelContenedor.setVisible(false);
    }
    
    // MÉTODO PARA AGREGAR PRODUCTO
    private void agregarProducto() {
        try {
            // Validar campos
            if (!validarCamposAgregar()) {
                return;
            }
            
            // Crear objeto producto
            String nombre = vistaAgregar.jTextFieldNOMBREAGREGAR.getText().trim();
            double precio = Double.parseDouble(vistaAgregar.jTextFieldPRECIOAGREGAR.getText().trim());
            int cantidad = (Integer) vistaAgregar.jSpinnerCANTIAD.getValue();
            String categoria = (String) vistaAgregar.jComboBoxCATEGORIA.getSelectedItem();
            String proveedor = vistaAgregar.jTextFieldPROVEEDORAGREGAR.getText().trim();
            
            Productos producto = new Productos(nombre, precio, cantidad, categoria, proveedor);
            
            // Guardar en base de datos
            if (productoDAO.agregarProductos(producto)) {
                JOptionPane.showMessageDialog(vistaAgregar, "Producto agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposAgregar();
                actualizarTodasLasTablas();
            } else {
                JOptionPane.showMessageDialog(vistaAgregar, "Error al agregar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaAgregar, "El precio debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaAgregar, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Validar campos de agregar
    private boolean validarCamposAgregar() {
        String nombre = vistaAgregar.jTextFieldNOMBREAGREGAR.getText().trim();
        String precioStr = vistaAgregar.jTextFieldPRECIOAGREGAR.getText().trim();
        String proveedor = vistaAgregar.jTextFieldPROVEEDORAGREGAR.getText().trim();
        int cantidad = (Integer) vistaAgregar.jSpinnerCANTIAD.getValue();
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vistaAgregar, "El nombre no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            vistaAgregar.jTextFieldNOMBREAGREGAR.requestFocus();
            return false;
        }
        
        if (nombre.length() > 100) {
            JOptionPane.showMessageDialog(vistaAgregar, "El nombre no puede tener más de 100 caracteres", "Validación", JOptionPane.WARNING_MESSAGE);
            vistaAgregar.jTextFieldNOMBREAGREGAR.requestFocus();
            return false;
        }
        
        if (precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(vistaAgregar, "El precio no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            vistaAgregar.jTextFieldPRECIOAGREGAR.requestFocus();
            return false;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                JOptionPane.showMessageDialog(vistaAgregar, "El precio no puede ser negativo", "Validación", JOptionPane.WARNING_MESSAGE);
                vistaAgregar.jTextFieldPRECIOAGREGAR.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaAgregar, "El precio debe ser un número válido", "Validación", JOptionPane.WARNING_MESSAGE);
            vistaAgregar.jTextFieldPRECIOAGREGAR.requestFocus();
            return false;
        }
        
        if (cantidad < 0) {
            JOptionPane.showMessageDialog(vistaAgregar, "La cantidad no puede ser negativa", "Validación", JOptionPane.WARNING_MESSAGE);
            vistaAgregar.jSpinnerCANTIAD.requestFocus();
            return false;
        }
        
        if (proveedor.isEmpty()) {
            JOptionPane.showMessageDialog(vistaAgregar, "El proveedor no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            vistaAgregar.jTextFieldPROVEEDORAGREGAR.requestFocus();
            return false;
        }
        
        if (proveedor.length() > 60) {
            JOptionPane.showMessageDialog(vistaAgregar, "El proveedor no puede tener más de 60 caracteres", "Validación", JOptionPane.WARNING_MESSAGE);
            vistaAgregar.jTextFieldPROVEEDORAGREGAR.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // Limpiar campos de agregar
    private void limpiarCamposAgregar() {
        vistaAgregar.jTextFieldNOMBREAGREGAR.setText("");
        vistaAgregar.jTextFieldPRECIOAGREGAR.setText("");
        vistaAgregar.jTextFieldPROVEEDORAGREGAR.setText("");
        vistaAgregar.jSpinnerCANTIAD.setValue(0);
        vistaAgregar.jComboBoxCATEGORIA.setSelectedIndex(0);
    }
    
    // MÉTODO PARA BUSCAR PRODUCTO PARA EDITAR
    private void buscarProductoParaEditar() {
        try {
            String idStr = vistaEditar.jTextFieldIDEDIT.getText().trim();
            
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(vistaEditar, "Ingrese un ID para buscar", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(idStr);
            Productos producto = productoDAO.obtenerProductoPorId(id);
            
            if (producto != null) {
                // Llenar campos con datos del producto
                vistaEditar.jTextFieldNOMBREEDIT.setText(producto.getNombre());
                vistaEditar.jTextFieldPRECIOEDIT.setText(String.valueOf(producto.getPrecio()));
                vistaEditar.jSpinnerCANTIDADEDIT.setValue(producto.getCantidad());
                vistaEditar.jComboBoxCATEGORIAEDIT.setSelectedItem(producto.getCategoria());
                vistaEditar.jTextFieldPROVEEDOREDIT.setText(producto.getProveedor());
                
                JOptionPane.showMessageDialog(vistaEditar, "Producto encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vistaEditar, "No se encontró un producto con ese ID", "No encontrado", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposEditar();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaEditar, "El ID debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // MÉTODO PARA EDITAR PRODUCTO
    private void editarProducto() {
        try {
            String idStr = vistaEditar.jTextFieldIDEDIT.getText().trim();
            
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(vistaEditar, "Primero busque un producto por ID", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarCamposEditar()) {
                return;
            }
            
            int id = Integer.parseInt(idStr);
            String nombre = vistaEditar.jTextFieldNOMBREEDIT.getText().trim();
            double precio = Double.parseDouble(vistaEditar.jTextFieldPRECIOEDIT.getText().trim());
            int cantidad = (Integer) vistaEditar.jSpinnerCANTIDADEDIT.getValue();
            String categoria = (String) vistaEditar.jComboBoxCATEGORIAEDIT.getSelectedItem();
            String proveedor = vistaEditar.jTextFieldPROVEEDOREDIT.getText().trim();
            
            Productos producto = new Productos(id, nombre, precio, cantidad, categoria, proveedor);
            
            if (productoDAO.actualizarProducto(producto)) {
                JOptionPane.showMessageDialog(vistaEditar, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposEditar();
                actualizarTodasLasTablas();
            } else {
                JOptionPane.showMessageDialog(vistaEditar, "Error al actualizar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaEditar, "Verifique que los datos numéricos sean válidos", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Validar campos de editar
    private boolean validarCamposEditar() {
        String nombre = vistaEditar.jTextFieldNOMBREEDIT.getText().trim();
        String precioStr = vistaEditar.jTextFieldPRECIOEDIT.getText().trim();
        String proveedor = vistaEditar.jTextFieldPROVEEDOREDIT.getText().trim();
        int cantidad = (Integer) vistaEditar.jSpinnerCANTIDADEDIT.getValue();
        
        if (nombre.isEmpty() || nombre.length() > 100) {
            JOptionPane.showMessageDialog(vistaEditar, "El nombre debe tener entre 1 y 100 caracteres", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                JOptionPane.showMessageDialog(vistaEditar, "El precio no puede ser negativo", "Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaEditar, "El precio debe ser un número válido", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (cantidad < 0) {
            JOptionPane.showMessageDialog(vistaEditar, "La cantidad no puede ser negativa", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (proveedor.isEmpty() || proveedor.length() > 60) {
            JOptionPane.showMessageDialog(vistaEditar, "El proveedor debe tener entre 1 y 60 caracteres", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    // Limpiar campos de editar
    private void limpiarCamposEditar() {
        vistaEditar.jTextFieldIDEDIT.setText("");
        vistaEditar.jTextFieldNOMBREEDIT.setText("");
        vistaEditar.jTextFieldPRECIOEDIT.setText("");
        vistaEditar.jTextFieldPROVEEDOREDIT.setText("");
        vistaEditar.jSpinnerCANTIDADEDIT.setValue(0);
        vistaEditar.jComboBoxCATEGORIAEDIT.setSelectedIndex(0);
    }
    
    // MÉTODO PARA BUSCAR PRODUCTO PARA ELIMINAR
    private void buscarProductoParaEliminar() {
        try {
            String idStr = vistaEliminar.jTextFieldIDELI.getText().trim();
            
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(vistaEliminar, "Ingrese un ID para buscar", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(idStr);
            Productos producto = productoDAO.obtenerProductoPorId(id);
            
            if (producto != null) {
                // Llenar campos con datos del producto (solo lectura)
                vistaEliminar.jTextFieldNOMBREELI.setText(producto.getNombre());
                vistaEliminar.jTextFieldPRECIOELI.setText(String.valueOf(producto.getPrecio()));
                vistaEliminar.jSpinnerCANTIDADELI.setValue(producto.getCantidad());
                vistaEliminar.jComboBoxCATEGORIAELI.setSelectedItem(producto.getCategoria());
                vistaEliminar.jTextFieldPROVEEDORELI.setText(producto.getProveedor());
                
                // Deshabilitar campos para que no se puedan editar
                vistaEliminar.jTextFieldNOMBREELI.setEnabled(false);
                vistaEliminar.jTextFieldPRECIOELI.setEnabled(false);
                vistaEliminar.jSpinnerCANTIDADELI.setEnabled(false);
                vistaEliminar.jComboBoxCATEGORIAELI.setEnabled(false);
                vistaEliminar.jTextFieldPROVEEDORELI.setEnabled(false);
                
                JOptionPane.showMessageDialog(vistaEliminar, "Producto encontrado. Verifique los datos antes de eliminar.", "Producto encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vistaEliminar, "No se encontró un producto con ese ID", "No encontrado", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposEliminar();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaEliminar, "El ID debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // MÉTODO PARA ELIMINAR PRODUCTO
    private void eliminarProducto() {
        try {
            String idStr = vistaEliminar.jTextFieldIDELI.getText().trim();
            
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(vistaEliminar, "Primero busque un producto por ID", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(idStr);
            
            // Confirmar eliminación
            int opcion = JOptionPane.showConfirmDialog(vistaEliminar, 
                "¿Está seguro de que desea eliminar este producto?\nEsta acción no se puede deshacer.", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                if (productoDAO.eliminarProducto(id)) {
                    JOptionPane.showMessageDialog(vistaEliminar, "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCamposEliminar();
                    actualizarTodasLasTablas();
                } else {
                    JOptionPane.showMessageDialog(vistaEliminar, "Error al eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaEliminar, "El ID debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Limpiar campos de eliminar
    private void limpiarCamposEliminar() {
        vistaEliminar.jTextFieldIDELI.setText("");
        vistaEliminar.jTextFieldNOMBREELI.setText("");
        vistaEliminar.jTextFieldPRECIOELI.setText("");
        vistaEliminar.jTextFieldPROVEEDORELI.setText("");
        vistaEliminar.jSpinnerCANTIDADELI.setValue(0);
        vistaEliminar.jComboBoxCATEGORIAELI.setSelectedIndex(0);
        
        // Rehabilitar campos
        vistaEliminar.jTextFieldNOMBREELI.setEnabled(true);
        vistaEliminar.jTextFieldPRECIOELI.setEnabled(true);
        vistaEliminar.jSpinnerCANTIDADELI.setEnabled(true);
        vistaEliminar.jComboBoxCATEGORIAELI.setEnabled(true);
        vistaEliminar.jTextFieldPROVEEDORELI.setEnabled(true);
    }
    
    // MÉTODOS PARA ACTUALIZAR TABLAS
    private void actualizarTablaEditar() {
        List<Productos> productos = productoDAO.obtenerTodosLosProductos();
        DefaultTableModel modelo = (DefaultTableModel) vistaEditar.jTableEDIT.getModel();
        modelo.setRowCount(0);
        
        for (Productos producto : productos) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.getCategoria(),
                producto.getProveedor()
            };
            modelo.addRow(fila);
        }
    }
    
    private void actualizarTablaEliminar() {
        List<Productos> productos = productoDAO.obtenerTodosLosProductos();
        DefaultTableModel modelo = (DefaultTableModel) vistaEliminar.jTableELI.getModel();
        modelo.setRowCount(0);
        
        for (Productos producto : productos) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.getCategoria(),
                producto.getProveedor()
            };
            modelo.addRow(fila);
        }
    }
    
    private void actualizarTablaListar() {
        List<Productos> productos = productoDAO.obtenerTodosLosProductos();
        DefaultTableModel modelo = (DefaultTableModel) vistaLista.jTableList.getModel();
        modelo.setRowCount(0);
        
        for (Productos producto : productos) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.getCategoria(),
                producto.getProveedor()
            };
            modelo.addRow(fila);
        }
    }
    
    // Actualizar todas las tablas al mismo tiempo
    private void actualizarTodasLasTablas() {
        actualizarTablaEditar();
        actualizarTablaEliminar();
        actualizarTablaListar();
    }
    
    // Métodos públicos para actualizar tablas desde fuera del controlador
    public void actualizarTablas() {
        actualizarTodasLasTablas();
    }
    
    // Método para obtener productos (útil para reportes o exportaciones)
    public List<Productos> obtenerTodosLosProductos() {
        return productoDAO.obtenerTodosLosProductos();
    }
    
    // Método para buscar productos por nombre
    public List<Productos> buscarProductosPorNombre(String nombre) {
        return productoDAO.buscarProductosPorNombre(nombre);
    }
}