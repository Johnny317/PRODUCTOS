
package VISTA;

import CONTROLADOR.ControladorProductos;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 * Vista principal del sistema de gestión de productos
 * Maneja la navegación entre las diferentes vistas del sistema
 * 
 * @author Jhony Espinoza
 */
public class VistaPrincipal extends javax.swing.JFrame {

    // Logger para el manejo de logs
    private static final Logger LOGGER = Logger.getLogger(VistaPrincipal.class.getName());
    
    // Vistas del sistema
    private VistaAgregarP vistaAgregar;
    private VistaEditarP vistaEditar;
    private VistaEliminarP vistaEliminar;
    private VistaListaP vistaLista;
    
    // Controlador principal
    private ControladorProductos controlador;

    /**
     * Constructor de la vista principal
     */
    public VistaPrincipal() {
        initComponents();
        inicializarSistema();
    }

    /**
     * Inicializa todo el sistema de vistas y controlador
     */
    private void inicializarSistema() {
        try {
            LOGGER.info("Iniciando sistema de gestión de productos");
            
            crearVistas();
            crearControlador();
            configurarControlador();
            configurarInterfaz();
            
            LOGGER.info("Sistema inicializado exitosamente");
            
        } catch (ExceptionInInitializerError e) {
            manejarErrorInicializacion(e);
        } catch (Exception e) {
            manejarErrorGeneral(e);
        }
    }

    /**
     * Crea todas las instancias de las vistas
     */
    private void crearVistas() {
        LOGGER.info("Creando vistas del sistema");
        
        vistaAgregar = new VistaAgregarP();
        vistaEditar = new VistaEditarP();
        vistaEliminar = new VistaEliminarP();
        vistaLista = new VistaListaP();
        
        LOGGER.info("Vistas creadas correctamente");
    }

    /**
     * Crea el controlador principal
     */
    private void crearControlador() {
        LOGGER.info("Creando controlador de productos");
        controlador = new ControladorProductos();
        LOGGER.info("Controlador creado correctamente");
    }

    /**
     * Configura el controlador con todas las vistas
     */
    private void configurarControlador() {
        LOGGER.info("Configurando controlador con vistas");
        controlador.configurarVistas(this, vistaAgregar, vistaEditar, vistaEliminar, vistaLista);
        LOGGER.info("Controlador configurado correctamente");
    }

    /**
     * Configura la interfaz de usuario
     */
    private void configurarInterfaz() {
        LOGGER.info("Configurando interfaz de usuario");
        
        configurarVisibilidadComponentes();
        actualizarInterfaz();
        
        LOGGER.info("Interfaz configurada correctamente");
    }

    /**
     * Configura la visibilidad de todos los componentes
     */
    private void configurarVisibilidadComponentes() {
        // Configurar botones si existen
        configurarComponente(jButtonVISTAAGREGARP, "Botón Agregar");
        configurarComponente(jButtonVISTADITARP, "Botón Editar");
        configurarComponente(jButtonVISTAELIMINARP, "Botón Eliminar");
        configurarComponente(jButtonVISTALISTAP, "Botón Listar");
        
        // Configurar panel contenedor
        if (jPanelCONTENEDOR != null) {
            jPanelCONTENEDOR.setVisible(true);
            LOGGER.fine("Panel contenedor configurado");
        }
    }

    /**
     * Configura un componente específico asegurando su visibilidad
     * 
     * @param componente El componente a configurar
     * @param nombre Nombre del componente para logging
     */
    private void configurarComponente(java.awt.Component componente, String nombre) {
        if (componente != null) {
            componente.setVisible(true);
            LOGGER.fine(nombre + " configurado y visible");
        } else {
            LOGGER.warning(nombre + " no encontrado (null)");
        }
    }

    /**
     * Actualiza la interfaz forzando el repintado
     */
    private void actualizarInterfaz() {
        this.revalidate();
        this.repaint();
        LOGGER.fine("Interfaz actualizada");
    }

    /**
     * Maneja errores de inicialización específicos
     * 
     * @param e Excepción de inicialización
     */
    private void manejarErrorInicializacion(ExceptionInInitializerError e) {
        String mensaje = "Error de inicialización del sistema";
        LOGGER.log(Level.SEVERE, mensaje, e);
        
        mostrarMensajeError(
            "Error de Inicialización",
            "No se pudo inicializar el sistema.\n" +
            "Posible causa: Clases duplicadas o conflictos de dependencias.\n" +
            "Revisa la configuración del proyecto."
        );
    }

    /**
     * Maneja errores generales del sistema
     * 
     * @param e Excepción general
     */
    private void manejarErrorGeneral(Exception e) {
        String mensaje = "Error general del sistema";
        LOGGER.log(Level.SEVERE, mensaje, e);
        
        mostrarMensajeError(
            "Error del Sistema",
            "Ha ocurrido un error inesperado:\n" + e.getMessage()
        );
    }

    /**
     * Muestra un mensaje de error al usuario
     * 
     * @param titulo Título del mensaje
     * @param mensaje Contenido del mensaje
     */
    private void mostrarMensajeError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            titulo,
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanelCONTENEDOR = new javax.swing.JPanel();
        jPanelMenu = new javax.swing.JPanel();
        jButtonVISTAAGREGARP = new javax.swing.JButton();
        jButtonVISTADITARP = new javax.swing.JButton();
        jButtonVISTAELIMINARP = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jButtonVISTALISTAP = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        jPanelNAVEGACION = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelCONTENEDOR.setBackground(new java.awt.Color(42, 42, 59));

        jPanelMenu.setBackground(new java.awt.Color(42, 42, 59));

        jButtonVISTAAGREGARP.setBackground(new java.awt.Color(42, 42, 59));
        jButtonVISTAAGREGARP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RECURSOS/AgregarVP.png"))); // NOI18N
        jButtonVISTAAGREGARP.setMaximumSize(new java.awt.Dimension(140, 130));
        jButtonVISTAAGREGARP.setMinimumSize(new java.awt.Dimension(140, 130));
        jButtonVISTAAGREGARP.setPreferredSize(new java.awt.Dimension(140, 130));
        jButtonVISTAAGREGARP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVISTAAGREGARPActionPerformed(evt);
            }
        });

        jButtonVISTADITARP.setBackground(new java.awt.Color(42, 42, 59));
        jButtonVISTADITARP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RECURSOS/editVP.png"))); // NOI18N
        jButtonVISTADITARP.setMaximumSize(new java.awt.Dimension(140, 130));
        jButtonVISTADITARP.setMinimumSize(new java.awt.Dimension(140, 130));
        jButtonVISTADITARP.setPreferredSize(new java.awt.Dimension(140, 130));

        jButtonVISTAELIMINARP.setBackground(new java.awt.Color(42, 42, 59));
        jButtonVISTAELIMINARP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RECURSOS/DeleteVP.png"))); // NOI18N
        jButtonVISTAELIMINARP.setMaximumSize(new java.awt.Dimension(140, 130));
        jButtonVISTAELIMINARP.setMinimumSize(new java.awt.Dimension(140, 130));
        jButtonVISTAELIMINARP.setPreferredSize(new java.awt.Dimension(140, 130));
        jButtonVISTAELIMINARP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVISTAELIMINARPActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Georgia", 0, 24)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(102, 255, 51));
        jLabel47.setText("NUEVO");

        jLabel48.setFont(new java.awt.Font("Georgia", 0, 24)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 153, 51));
        jLabel48.setText("EDITAR");

        jLabel49.setFont(new java.awt.Font("Georgia", 0, 24)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(204, 204, 204));
        jLabel49.setText("ELIMINAR");

        jButtonVISTALISTAP.setBackground(new java.awt.Color(42, 42, 59));
        jButtonVISTALISTAP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RECURSOS/ListVP.png"))); // NOI18N
        jButtonVISTALISTAP.setMaximumSize(new java.awt.Dimension(140, 130));
        jButtonVISTALISTAP.setMinimumSize(new java.awt.Dimension(140, 130));
        jButtonVISTALISTAP.setPreferredSize(new java.awt.Dimension(140, 130));

        jLabel50.setFont(new java.awt.Font("Georgia", 0, 24)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 153, 204));
        jLabel50.setText("PRODUCTOS");

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonVISTAAGREGARP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addGap(32, 32, 32)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonVISTADITARP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addGap(27, 27, 27)))
                .addGap(60, 60, 60)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel49))
                    .addComponent(jButtonVISTAELIMINARP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonVISTALISTAP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addGap(45, 45, 45))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addComponent(jButtonVISTAELIMINARP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel49))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanelMenuLayout.createSequentialGroup()
                            .addComponent(jButtonVISTALISTAP, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelMenuLayout.createSequentialGroup()
                            .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButtonVISTADITARP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonVISTAAGREGARP, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel48)
                                .addComponent(jLabel47)))))
                .addContainerGap(124, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelCONTENEDORLayout = new javax.swing.GroupLayout(jPanelCONTENEDOR);
        jPanelCONTENEDOR.setLayout(jPanelCONTENEDORLayout);
        jPanelCONTENEDORLayout.setHorizontalGroup(
            jPanelCONTENEDORLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
            .addGroup(jPanelCONTENEDORLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelCONTENEDORLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanelCONTENEDORLayout.setVerticalGroup(
            jPanelCONTENEDORLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
            .addGroup(jPanelCONTENEDORLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelCONTENEDORLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanelCONTENEDOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 830, 390));

        jPanelNAVEGACION.setBackground(new java.awt.Color(30, 30, 47));
        jPanelNAVEGACION.setPreferredSize(new java.awt.Dimension(830, 50));

        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GESTION  DE  PRODUCTOS");

        jSeparator1.setBackground(new java.awt.Color(255, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 204, 0));

        jSeparator2.setForeground(new java.awt.Color(255, 0, 0));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RECURSOS/boxVP.png"))); // NOI18N

        javax.swing.GroupLayout jPanelNAVEGACIONLayout = new javax.swing.GroupLayout(jPanelNAVEGACION);
        jPanelNAVEGACION.setLayout(jPanelNAVEGACIONLayout);
        jPanelNAVEGACIONLayout.setHorizontalGroup(
            jPanelNAVEGACIONLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNAVEGACIONLayout.createSequentialGroup()
                .addContainerGap(239, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(207, 207, 207))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNAVEGACIONLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNAVEGACIONLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        jPanelNAVEGACIONLayout.setVerticalGroup(
            jPanelNAVEGACIONLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNAVEGACIONLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanelNAVEGACIONLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addGap(12, 12, 12)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanelNAVEGACION, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 80));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVISTAAGREGARPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVISTAAGREGARPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonVISTAAGREGARPActionPerformed

    private void jButtonVISTAELIMINARPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVISTAELIMINARPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonVISTAELIMINARPActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButtonVISTAAGREGARP;
    public javax.swing.JButton jButtonVISTADITARP;
    public javax.swing.JButton jButtonVISTAELIMINARP;
    public javax.swing.JButton jButtonVISTALISTAP;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    public javax.swing.JPanel jPanelCONTENEDOR;
    public javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelNAVEGACION;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
