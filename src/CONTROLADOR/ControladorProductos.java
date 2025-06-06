package CONTROLADOR;

import DAO.ProductosDAO;
import Modelo.Productos;
import VISTA.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ControladorProductos {
    
    // Constantes para las vistas
    private static final String VISTA_MENU = "MENU";
    private static final String VISTA_AGREGAR = "AGREGAR";
    private static final String VISTA_EDITAR = "EDITAR";
    private static final String VISTA_ELIMINAR = "ELIMINAR";
    private static final String VISTA_LISTAR = "LISTAR";
    
    // Constantes para validación
    private static final int MAX_NOMBRE_LENGTH = 100;
    private static final int MAX_PROVEEDOR_LENGTH = 60;
    private static final int MAX_CODIGO_LENGTH = 20;
    private static final String[] CATEGORIAS = {
        "Electrónicos", "Ropa", "Comida", "Hogar", 
        "Deportes", "Libros", "Juguetes", "Salud"
    };
    private static final String[] COLUMNAS_TABLA = {
        "ID", "Nombre", "Código", "Precio", "Cantidad", "Categoría", "Proveedor"
    };

    private final ProductosDAO productoDAO;
    private JPanel panelContenedor;
    private CardLayout cardLayout;
    
    // Timers para búsqueda con delay
    private Timer timerBusquedaEdit;
    private Timer timerBusquedaList;
    private Timer timerBusquedaEli;

    // Referencias a las vistas
    private VistaAgregarP vistaAgregar;
    private VistaEditarP vistaEditar;
    private VistaEliminarP vistaEliminar;
    private VistaListaP vistaLista;
    private VistaPrincipal vistaPrincipal;

    public ControladorProductos() {
        this.productoDAO = new ProductosDAO();
    }

    // ==================== CONFIGURACIÓN INICIAL ====================
    
    public void configurarVistas(VistaPrincipal principal,
                                 VistaAgregarP agregar,
                                 VistaEditarP editar,
                                 VistaEliminarP eliminar,
                                 VistaListaP lista) {

        this.vistaPrincipal = principal;
        this.vistaAgregar = agregar;
        this.vistaEditar = editar;
        this.vistaEliminar = eliminar;
        this.vistaLista = lista;
        this.panelContenedor = principal.jPanelCONTENEDOR;

        configurarLayout();
        configurarEventos();
        configurarComboBoxes();
        inicializarTablas();
        configurarBusquedaInteractiva();
    }

    private void configurarLayout() {
        this.cardLayout = new CardLayout();
        panelContenedor.setLayout(cardLayout);
        panelContenedor.removeAll();

        // Agregar vistas al panel contenedor
        panelContenedor.add(vistaPrincipal.jPanelMenu, VISTA_MENU);
        panelContenedor.add(vistaAgregar, VISTA_AGREGAR);
        panelContenedor.add(vistaEditar, VISTA_EDITAR);
        panelContenedor.add(vistaEliminar, VISTA_ELIMINAR);
        panelContenedor.add(vistaLista, VISTA_LISTAR);

        cardLayout.show(panelContenedor, VISTA_MENU);
    }

    private void configurarEventos() {
        // Botones del menú principal
        vistaPrincipal.jButtonVISTAAGREGARP.addActionListener(e -> mostrarVista(VISTA_AGREGAR));
        vistaPrincipal.jButtonVISTADITARP.addActionListener(e -> {
            actualizarTabla(vistaEditar.jTableEDIT);
            mostrarVista(VISTA_EDITAR);
        });
        vistaPrincipal.jButtonVISTAELIMINARP.addActionListener(e -> {
            actualizarTabla(vistaEliminar.jTableELI);
            mostrarVista(VISTA_ELIMINAR);
        });
        vistaPrincipal.jButtonVISTALISTAP.addActionListener(e -> {
            actualizarTabla(vistaLista.jTableList);
            mostrarVista(VISTA_LISTAR);
        });

        // Botones BACK
        vistaAgregar.jButtonBACK.addActionListener(e -> mostrarVista(VISTA_MENU));
        vistaEditar.jButtonBACK.addActionListener(e -> mostrarVista(VISTA_MENU));
        vistaEliminar.jButtonBACK.addActionListener(e -> mostrarVista(VISTA_MENU));
        vistaLista.jButtonBACKLIST.addActionListener(e -> mostrarVista(VISTA_MENU));

        // Botones de acción
        vistaAgregar.jButtonAGREGAR.addActionListener(e -> agregarProducto());
        vistaEditar.jButtonBUSCAREDIT.addActionListener(e -> buscarProducto(TipoOperacion.EDITAR));
        vistaEditar.jButtonEDIT.addActionListener(e -> editarProducto());
        vistaEliminar.jButtonBUSCARELI.addActionListener(e -> buscarProducto(TipoOperacion.ELIMINAR));
        vistaEliminar.jButtonELIMINAR.addActionListener(e -> eliminarProducto());
        vistaLista.jButtonBUSCARLIST.addActionListener(e -> buscarProducto(TipoOperacion.LISTAR));
        
        // Eventos de clic en tablas para selección
        vistaEditar.jTableEDIT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    seleccionarProductoDeTabla(TipoOperacion.EDITAR);
                }
            }
        });
        
        vistaEliminar.jTableELI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    seleccionarProductoDeTabla(TipoOperacion.ELIMINAR);
                }
            }
        });
    }

    private void configurarComboBoxes() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(CATEGORIAS);
        vistaAgregar.jComboBoxCATEGORIA.setModel(model);
        vistaEditar.jComboBoxCATEGORIAEDIT.setModel(new DefaultComboBoxModel<>(CATEGORIAS));
        vistaEliminar.jComboBoxCATEGORIAELI.setModel(new DefaultComboBoxModel<>(CATEGORIAS));
    }

    private void inicializarTablas() {
        configurarTabla(vistaEditar.jTableEDIT);
        configurarTabla(vistaEliminar.jTableELI);
        configurarTabla(vistaLista.jTableList);
    }

    private void configurarTabla(JTable tabla) {
        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS_TABLA, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla.setModel(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // ==================== CONFIGURACIÓN BÚSQUEDA INTERACTIVA ====================
    
    private void configurarBusquedaInteractiva() {
        // Configurar placeholder text y eventos para cada campo de búsqueda
        configurarCampoBusqueda(vistaEditar.jTextFieldBUSCAREDIT, "Buscar por ID, nombre o código...", TipoOperacion.EDITAR);
        configurarCampoBusqueda(vistaEliminar.jTextFieldBUSCARELI, "Buscar por ID, nombre o código...", TipoOperacion.ELIMINAR);
        configurarCampoBusqueda(vistaLista.jTextFieldBUSCARLIST, "Buscar por ID, nombre o código...", TipoOperacion.LISTAR);
    }
    
    private void configurarCampoBusqueda(JTextField campoTexto, String placeholder, TipoOperacion operacion) {
        // Configurar placeholder
        campoTexto.setToolTipText(placeholder);
        
        // Listener para búsqueda en tiempo real
        campoTexto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                iniciarBusquedaConDelay(operacion);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                iniciarBusquedaConDelay(operacion);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                iniciarBusquedaConDelay(operacion);
            }
        });
        
        // Enter para seleccionar primer resultado
        campoTexto.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    seleccionarPrimerResultado(operacion);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }
    
    private void iniciarBusquedaConDelay(TipoOperacion operacion) {
        // Cancelar timer anterior si existe
        Timer timerActual = obtenerTimer(operacion);
        if (timerActual != null) {
            timerActual.cancel();
        }
        
        // Crear nuevo timer con delay de 300ms
        Timer nuevoTimer = new Timer();
        establecerTimer(operacion, nuevoTimer);
        
        nuevoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> realizarBusquedaInteractiva(operacion));
            }
        }, 300);
    }
    
    private Timer obtenerTimer(TipoOperacion operacion) {
        switch (operacion) {
            case EDITAR: return timerBusquedaEdit;
            case ELIMINAR: return timerBusquedaEli;
            case LISTAR: return timerBusquedaList;
            default: return null;
        }
    }
    
    private void establecerTimer(TipoOperacion operacion, Timer timer) {
        switch (operacion) {
            case EDITAR: timerBusquedaEdit = timer; break;
            case ELIMINAR: timerBusquedaEli = timer; break;
            case LISTAR: timerBusquedaList = timer; break;
        }
    }
    
    private void realizarBusquedaInteractiva(TipoOperacion operacion) {
        String termino = obtenerTerminoBusqueda(operacion).trim();
        
        if (termino.isEmpty()) {
            // Si no hay término, mostrar todos los productos
            actualizarTabla(obtenerTablaPorOperacion(operacion));
            return;
        }
        
        if (termino.length() < 2) {
            // Esperar al menos 2 caracteres para buscar
            return;
        }
        
        try {
            List<Productos> resultados = productoDAO.buscarProductosInteractivo(termino);
            actualizarTablaConResultados(obtenerTablaPorOperacion(operacion), resultados);
        } catch (Exception e) {
            System.err.println("Error en búsqueda interactiva: " + e.getMessage());
        }
    }
    
    private String obtenerTerminoBusqueda(TipoOperacion operacion) {
        switch (operacion) {
            case EDITAR: return vistaEditar.jTextFieldBUSCAREDIT.getText();
            case ELIMINAR: return vistaEliminar.jTextFieldBUSCARELI.getText();
            case LISTAR: return vistaLista.jTextFieldBUSCARLIST.getText();
            default: return "";
        }
    }
    
    private JTable obtenerTablaPorOperacion(TipoOperacion operacion) {
        switch (operacion) {
            case EDITAR: return vistaEditar.jTableEDIT;
            case ELIMINAR: return vistaEliminar.jTableELI;
            case LISTAR: return vistaLista.jTableList;
            default: return null;
        }
    }
    
    private void seleccionarPrimerResultado(TipoOperacion operacion) {
        JTable tabla = obtenerTablaPorOperacion(operacion);
        if (tabla.getRowCount() > 0) {
            tabla.setRowSelectionInterval(0, 0);
            if (operacion != TipoOperacion.LISTAR) {
                seleccionarProductoDeTabla(operacion);
            }
        }
    }
    
    private void seleccionarProductoDeTabla(TipoOperacion operacion) {
        JTable tabla = obtenerTablaPorOperacion(operacion);
        int filaSeleccionada = tabla.getSelectedRow();
        
        if (filaSeleccionada >= 0) {
            int id = (Integer) tabla.getValueAt(filaSeleccionada, 0);
            Productos producto = productoDAO.obtenerProductoPorId(id);
            
            if (producto != null) {
                llenarCamposConProducto(producto, operacion);
                if (operacion == TipoOperacion.ELIMINAR) {
                    deshabilitarCamposEliminar();
                }
            }
        }
    }

    // ==================== NAVEGACIÓN ====================
    
    public void mostrarVista(String vista) {
        if (!(panelContenedor.getLayout() instanceof CardLayout)) {
            System.err.println("ERROR: panelContenedor NO tiene CardLayout.");
            return;
        }
        cardLayout.show(panelContenedor, vista);
    }

    // ==================== OPERACIONES CRUD ====================
    
    private void agregarProducto() {
        ejecutarOperacion(() -> {
            if (!validarCampos(TipoOperacion.AGREGAR)) return;

            Productos producto = crearProductoDesdeVista(TipoOperacion.AGREGAR);
            
            if (productoDAO.agregarProductos(producto)) {
                mostrarMensaje(vistaAgregar, "Producto agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos(TipoOperacion.AGREGAR);
                actualizarTodasLasTablas();
            } else {
                mostrarMensaje(vistaAgregar, "Error al agregar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }, vistaAgregar, "Error al agregar producto");
    }

    private void editarProducto() {
        ejecutarOperacion(() -> {
            String idStr = vistaEditar.jTextFieldIDEDIT.getText().trim();
            
            if (idStr.isEmpty()) {
                mostrarMensaje(vistaEditar, "Primero busque un producto por ID", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!validarCampos(TipoOperacion.EDITAR)) return;

            int id = Integer.parseInt(idStr);
            Productos producto = crearProductoDesdeVista(TipoOperacion.EDITAR);
            producto.setId(id);

            if (productoDAO.actualizarProducto(producto)) {
                mostrarMensaje(vistaEditar, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos(TipoOperacion.EDITAR);
                actualizarTodasLasTablas();
            } else {
                mostrarMensaje(vistaEditar, "Error al actualizar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }, vistaEditar, "Error al editar producto");
    }

    private void eliminarProducto() {
        ejecutarOperacion(() -> {
            String idStr = vistaEliminar.jTextFieldIDELI.getText().trim();

            if (idStr.isEmpty()) {
                mostrarMensaje(vistaEliminar, "Primero busque un producto por ID", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);

            if (confirmarEliminacion()) {
                if (productoDAO.eliminarProducto(id)) {
                    mostrarMensaje(vistaEliminar, "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos(TipoOperacion.ELIMINAR);
                    actualizarTodasLasTablas();
                } else {
                    mostrarMensaje(vistaEliminar, "Error al eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }, vistaEliminar, "Error al eliminar producto");
    }

    // ==================== BÚSQUEDA ====================
    
    private void buscarProducto(TipoOperacion operacion) {
        ejecutarOperacion(() -> {
            String termino = obtenerTerminoBusqueda(operacion).trim();
            
            if (termino.isEmpty()) {
                mostrarMensaje(obtenerVistaPorOperacion(operacion), "Ingrese un término para buscar", 
                             "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Productos> productos = productoDAO.buscarProductosInteractivo(termino);
            
            if (!productos.isEmpty()) {
                actualizarTablaConResultados(obtenerTablaPorOperacion(operacion), productos);
                mostrarMensaje(obtenerVistaPorOperacion(operacion), 
                             "Se encontraron " + productos.size() + " producto(s)", 
                             "Resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarMensaje(obtenerVistaPorOperacion(operacion), 
                             "No se encontraron productos con ese término", 
                             "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla(obtenerTablaPorOperacion(operacion));
            }
        }, obtenerVistaPorOperacion(operacion), "Error en búsqueda");
    }

    // ==================== VALIDACIÓN ====================
    
    private boolean validarCampos(TipoOperacion operacion) {
        DatosFormulario datos = obtenerDatosFormulario(operacion);
        JComponent vista = obtenerVistaPorOperacion(operacion);

        if (!validarNombre(datos.nombre, vista)) return false;
        if (!validarPrecio(datos.precio, vista)) return false;
        if (!validarCantidad(datos.cantidad, vista)) return false;
        if (!validarProveedor(datos.proveedor, vista)) return false;
        if (!validarCodigo(datos.codigo, vista)) return false;

        return true;
    }

    private boolean validarNombre(String nombre, JComponent vista) {
        if (nombre.isEmpty()) {
            mostrarMensaje(vista, "El nombre no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (nombre.length() > MAX_NOMBRE_LENGTH) {
            mostrarMensaje(vista, "El nombre no puede tener más de " + MAX_NOMBRE_LENGTH + " caracteres", 
                         "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarPrecio(String precioStr, JComponent vista) {
        if (precioStr.isEmpty()) {
            mostrarMensaje(vista, "El precio no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                mostrarMensaje(vista, "El precio no puede ser negativo", "Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensaje(vista, "El precio debe ser un número válido", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCantidad(int cantidad, JComponent vista) {
        if (cantidad < 0) {
            mostrarMensaje(vista, "La cantidad no puede ser negativa", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarProveedor(String proveedor, JComponent vista) {
        if (proveedor.isEmpty()) {
            mostrarMensaje(vista, "El proveedor no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (proveedor.length() > MAX_PROVEEDOR_LENGTH) {
            mostrarMensaje(vista, "El proveedor no puede tener más de " + MAX_PROVEEDOR_LENGTH + " caracteres", 
                         "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private boolean validarCodigo(String codigo, JComponent vista) {
        if (codigo.length() > MAX_CODIGO_LENGTH) {
            mostrarMensaje(vista, "El código no puede tener más de " + MAX_CODIGO_LENGTH + " caracteres", 
                         "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // ==================== UTILIDADES ====================
    
    private void ejecutarOperacion(Runnable operacion, JComponent vista, String mensajeError) {
        try {
            operacion.run();
        } catch (NumberFormatException e) {
            mostrarMensaje(vista, "Verifique que los datos numéricos sean válidos", 
                         "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            mostrarMensaje(vista, mensajeError + ": " + e.getMessage(), 
                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Productos crearProductoDesdeVista(TipoOperacion operacion) {
        DatosFormulario datos = obtenerDatosFormulario(operacion);
        return new Productos(datos.nombre, Double.parseDouble(datos.precio), 
                           datos.cantidad, datos.categoria, datos.proveedor, datos.codigo);
    }

    private DatosFormulario obtenerDatosFormulario(TipoOperacion operacion) {
        switch (operacion) {
            case AGREGAR:
                return new DatosFormulario(
                    vistaAgregar.jTextFieldNOMBREAGREGAR.getText().trim(),
                    vistaAgregar.jTextFieldPRECIOAGREGAR.getText().trim(),
                    (Integer) vistaAgregar.jSpinnerCANTIAD.getValue(),
                    (String) vistaAgregar.jComboBoxCATEGORIA.getSelectedItem(),
                    vistaAgregar.jTextFieldPROVEEDORAGREGAR.getText().trim(),
                    obtenerCodigoDesdeVista(operacion)
                );
            case EDITAR:
                return new DatosFormulario(
                    vistaEditar.jTextFieldNOMBREEDIT.getText().trim(),
                    vistaEditar.jTextFieldPRECIOEDIT.getText().trim(),
                    (Integer) vistaEditar.jSpinnerCANTIDADEDIT.getValue(),
                    (String) vistaEditar.jComboBoxCATEGORIAEDIT.getSelectedItem(),
                    vistaEditar.jTextFieldPROVEEDOREDIT.getText().trim(),
                    obtenerCodigoDesdeVista(operacion)
                );
            default:
                throw new IllegalArgumentException("Operación no soportada: " + operacion);
        }
    }
    
    private String obtenerCodigoDesdeVista(TipoOperacion operacion) {
        switch (operacion) {
            case AGREGAR:
                 return vistaAgregar.jTextFieldCODIGOAGREGAR.getText().trim();
             
            case EDITAR:
                return vistaEditar.jTextFieldCODIGOEDIT.getText().trim();
                
            case ELIMINAR: 
               return vistaEliminar.jTextFieldCODIGOELIMINAR.getText().trim();

            default:
                return "";
        }
    }

    private String obtenerIdParaBusqueda(TipoOperacion operacion) {
        switch (operacion) {
            case EDITAR: return vistaEditar.jTextFieldIDEDIT.getText().trim();
            case ELIMINAR: return vistaEliminar.jTextFieldIDELI.getText().trim();
            default: return "";
        }
    }

    private JComponent obtenerVistaPorOperacion(TipoOperacion operacion) {
        switch (operacion) {
            case AGREGAR: return vistaAgregar;
            case EDITAR: return vistaEditar;
            case ELIMINAR: return vistaEliminar;
            case LISTAR: return vistaLista;
            default: throw new IllegalArgumentException("Operación no soportada: " + operacion);
        }
    }

    private void llenarCamposConProducto(Productos producto, TipoOperacion operacion) {
        switch (operacion) {
            case EDITAR:
                vistaEditar.jTextFieldIDEDIT.setText(String.valueOf(producto.getId()));
                vistaEditar.jTextFieldNOMBREEDIT.setText(producto.getNombre());
                vistaEditar.jTextFieldPRECIOEDIT.setText(String.valueOf(producto.getPrecio()));
                vistaEditar.jSpinnerCANTIDADEDIT.setValue(producto.getCantidad());
                vistaEditar.jComboBoxCATEGORIAEDIT.setSelectedItem(producto.getCategoria());
                vistaEditar.jTextFieldPROVEEDOREDIT.setText(producto.getProveedor());
                vistaEditar.jTextFieldCODIGOEDIT.setText(producto.getCodigo());
                break;
            case ELIMINAR:
                vistaEliminar.jTextFieldIDELI.setText(String.valueOf(producto.getId()));
                vistaEliminar.jTextFieldNOMBREELI.setText(producto.getNombre());
                vistaEliminar.jTextFieldPRECIOELI.setText(String.valueOf(producto.getPrecio()));
                vistaEliminar.jSpinnerCANTIDADELI.setValue(producto.getCantidad());
                vistaEliminar.jComboBoxCATEGORIAELI.setSelectedItem(producto.getCategoria());
                vistaEliminar.jTextFieldPROVEEDORELI.setText(producto.getProveedor());
                vistaEliminar.jTextFieldCODIGOELIMINAR.setText(producto.getCodigo());
                break;
        }
    }

    private void limpiarCampos(TipoOperacion operacion) {
        switch (operacion) {
            case AGREGAR:
                vistaAgregar.jTextFieldNOMBREAGREGAR.setText("");
                vistaAgregar.jTextFieldPRECIOAGREGAR.setText("");
                vistaAgregar.jTextFieldPROVEEDORAGREGAR.setText("");
                vistaAgregar.jSpinnerCANTIAD.setValue(0);
                vistaAgregar.jComboBoxCATEGORIA.setSelectedIndex(0);
                vistaAgregar.jTextFieldCODIGOAGREGAR.setText("");
                break;
            case EDITAR:
                vistaEditar.jTextFieldIDEDIT.setText("");
                vistaEditar.jTextFieldNOMBREEDIT.setText("");
                vistaEditar.jTextFieldPRECIOEDIT.setText("");
                vistaEditar.jTextFieldPROVEEDOREDIT.setText("");
                vistaEditar.jSpinnerCANTIDADEDIT.setValue(0);
                vistaEditar.jComboBoxCATEGORIAEDIT.setSelectedIndex(0);
                vistaEditar.jTextFieldBUSCAREDIT.setText("");
                 vistaEditar.jTextFieldCODIGOEDIT.setText("");
                break;
            case ELIMINAR:
                vistaEliminar.jTextFieldIDELI.setText("");
                vistaEliminar.jTextFieldNOMBREELI.setText("");
                vistaEliminar.jTextFieldPRECIOELI.setText("");
                vistaEliminar.jTextFieldPROVEEDORELI.setText("");
                vistaEliminar.jSpinnerCANTIDADELI.setValue(0);
                vistaEliminar.jComboBoxCATEGORIAELI.setSelectedIndex(0);
                vistaEliminar.jTextFieldBUSCARELI.setText("");
                vistaEliminar.jTextFieldCODIGOELIMINAR.setText("");
                habilitarCamposEliminar();
                break;
        }
    }

  private void deshabilitarCamposEliminar() {
        vistaEliminar.jTextFieldNOMBREELI.setEnabled(false);
        vistaEliminar.jTextFieldPRECIOELI.setEnabled(false);
        vistaEliminar.jTextFieldPROVEEDORELI.setEnabled(false);
        vistaEliminar.jSpinnerCANTIDADELI.setEnabled(false);
        vistaEliminar.jComboBoxCATEGORIAELI.setEnabled(false);
         vistaEliminar.jTextFieldCODIGOELIMINAR.setEnabled(false);
    }

    private void habilitarCamposEliminar() {
        vistaEliminar.jTextFieldNOMBREELI.setEnabled(true);
        vistaEliminar.jTextFieldPRECIOELI.setEnabled(true);
        vistaEliminar.jTextFieldPROVEEDORELI.setEnabled(true);
        vistaEliminar.jSpinnerCANTIDADELI.setEnabled(true);
        vistaEliminar.jComboBoxCATEGORIAELI.setEnabled(true);
        vistaEliminar.jTextFieldCODIGOELIMINAR.setEnabled(true);
    }

    private boolean confirmarEliminacion() {
        int opcion = JOptionPane.showConfirmDialog(
            vistaEliminar,
            "¿Está seguro de que desea eliminar este producto?\nEsta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        return opcion == JOptionPane.YES_OPTION;
    }

    private void mostrarMensaje(JComponent padre, String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, titulo, tipoMensaje);
    }

    // ==================== ACTUALIZACIÓN DE TABLAS ====================
    
    private void actualizarTabla(JTable tabla) {
        try {
            List<Productos> productos = productoDAO.listarTodosLosProductos();
            actualizarTablaConResultados(tabla, productos);
        } catch (Exception e) {
            System.err.println("Error al actualizar tabla: " + e.getMessage());
        }
    }

    private void actualizarTablaConResultados(JTable tabla, List<Productos> productos) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        for (Productos producto : productos) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getCodigo(),
                String.format("%.2f", producto.getPrecio()),
                producto.getCantidad(),
                producto.getCategoria(),
                producto.getProveedor()
            };
            modelo.addRow(fila);
        }
    }

    private void actualizarTodasLasTablas() {
        SwingUtilities.invokeLater(() -> {
            actualizarTabla(vistaEditar.jTableEDIT);
            actualizarTabla(vistaEliminar.jTableELI);
            actualizarTabla(vistaLista.jTableList);
        });
    }

    // ==================== CLASES AUXILIARES ====================
    
    /**
     * Enumeración para identificar el tipo de operación CRUD
     */
    private enum TipoOperacion {
        AGREGAR, EDITAR, ELIMINAR, LISTAR
    }

    /**
     * Clase para encapsular los datos del formulario
     */
    private static class DatosFormulario {
        final String nombre;
        final String precio;
        final int cantidad;
        final String categoria;
        final String proveedor;
        final String codigo;

        public DatosFormulario(String nombre, String precio, int cantidad, 
                              String categoria, String proveedor, String codigo) {
            this.nombre = nombre;
            this.precio = precio;
            this.cantidad = cantidad;
            this.categoria = categoria;
            this.proveedor = proveedor;
            this.codigo = codigo;
        }
    }

    // ==================== MÉTODOS PÚBLICOS PARA INICIALIZACIÓN ====================
    
    /**
     * Método para inicializar el controlador después de configurar las vistas
     */
    public void inicializar() {
        SwingUtilities.invokeLater(() -> {
            mostrarVista(VISTA_MENU);
            actualizarTodasLasTablas();
        });
    }

    /**
     * Método para limpiar recursos al cerrar la aplicación
     */
    public void limpiarRecursos() {
        if (timerBusquedaEdit != null) {
            timerBusquedaEdit.cancel();
        }
        if (timerBusquedaList != null) {
            timerBusquedaList.cancel();
        }
        if (timerBusquedaEli != null) {
            timerBusquedaEli.cancel();
        }
    }

    // ==================== MÉTODOS DE UTILIDAD ADICIONALES ====================
    
    /**
     * Refresca todas las tablas desde la base de datos
     */
    public void refrescarTablas() {
        actualizarTodasLasTablas();
    }

  
}