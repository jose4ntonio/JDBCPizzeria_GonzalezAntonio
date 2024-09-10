package Presentacion;

import DAO.Conexion;
import DAO.ProductoDAO;
import Interfaces.IConexion;
import Interfaces.IProductoDAO;
import Objeto.Producto;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Dell (josé antonio) dell es la marca de mi compu y así :p
 */

public class ConsultaPizza extends javax.swing.JFrame {

    private boolean modoAgregar = true; // Variable para saber si estamos en modo agregar o actualizar

    public ConsultaPizza() {
        initComponents();
        cargarDatos(); // Cargar los datos de los productos en la tabla al iniciar
        limpiarCamposTexto(); // Limpiar los campos de texto al iniciar
        habilitarCamposTexto(); // Hacer que los campos de texto sean editables al iniciar

        // Agregar un ListSelectionListener a la tabla para manejar la selección de filas
        TablaPizzas.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && TablaPizzas.getSelectedRow() != -1) {
                llenarCamposDesdeSeleccion(); // Llenar los campos con la información del producto seleccionado
                habilitarCamposTexto(); // Habilitar los campos de texto cuando se selecciona un producto
                modoAgregar = false; // Cambiar a modo actualización
                JBAgregarActualizar.setText("Actualizar"); // Cambiar el texto del botón de agregar a "Actualizar"
                JBAgregarActualizar.setActionCommand("Actualizar"); // Establecer el comando del botón a "Actualizar"
                JBCancelarEliminar.setText("Eliminar"); // Cambiar el texto del botón cancelar a "Eliminar"
                JBCancelarEliminar.setActionCommand("Eliminar"); // Establecer el comando del botón cancelar a "Eliminar"
            } else {
                habilitarCamposTexto(); // Habilitar los campos de texto si no hay selección
                limpiarCamposTexto(); // Limpiar los campos de texto si no hay selección
                JBAgregarActualizar.setText("Agregar"); // Cambiar el texto del botón a "Agregar"
                JBAgregarActualizar.setActionCommand("Agregar"); // Establecer el comando del botón a "Agregar"
                JBCancelarEliminar.setText("Cancelar"); // Cambiar el texto del botón cancelar a "Cancelar"
                JBCancelarEliminar.setActionCommand("Cancelar"); // Establecer el comando del botón cancelar a "Cancelar"
            }
        });
    }

    private void llenarCamposDesdeSeleccion() {
        int filaSeleccionada = TablaPizzas.getSelectedRow(); // Obtener la fila seleccionada
        if (filaSeleccionada != -1) {
            // Obtener valores de la fila seleccionada y llenar los campos de texto
            String nombre = TablaPizzas.getValueAt(filaSeleccionada, 1).toString();
            String precio = TablaPizzas.getValueAt(filaSeleccionada, 2).toString();
            String descripcion = TablaPizzas.getValueAt(filaSeleccionada, 3).toString();

            TxtNombre.setText(nombre);
            TxtPrecio.setText(precio);
            TxtDescripcion.setText(descripcion);
        }
    }

    private void cargarDatos() {
        IConexion conexion = new Conexion();
        IProductoDAO productoDAO = new ProductoDAO(conexion);

        // Obtener la lista de productos desde la base de datos
        List<Producto> listaPizzas = productoDAO.consultar();

        // Crear un modelo para la tabla
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que las celdas no sean editables
            }
        };

        // Agregar columnas al modelo de la tabla
        modelo.addColumn("No.");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Descripción");

        // Agregar filas al modelo con los datos de los productos
        for (Producto p : listaPizzas) {
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getDescripcion()});
        }

        TablaPizzas.setModel(modelo); // Asignar el modelo a la tabla
    }

    private void habilitarCamposTexto() {
        TxtNombre.setEditable(true); 
        TxtPrecio.setEditable(true); 
        TxtDescripcion.setEditable(true); 
    }

    private void limpiarCamposTexto() {
        TxtNombre.setText(""); 
        TxtPrecio.setText(""); 
        TxtDescripcion.setText(""); 
    }

    private void agregarNuevoProducto() {
        // Obtener los datos de los campos de texto
        String nombre = TxtNombre.getText();
        String precioStr = TxtPrecio.getText();
        String descripcion = TxtDescripcion.getText();

        // Verificar si todos los campos están llenos
        if (nombre.isEmpty() || precioStr.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        float precio;
        try {
            precio = Float.parseFloat(precioStr); // Convertir el precio a float
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IConexion conexion = new Conexion();
        IProductoDAO productoDAO = new ProductoDAO(conexion);

        // Crear un nuevo objeto Producto
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setDescripcion(descripcion);

        // Agregar el nuevo producto a la base de datos
        boolean guardado = productoDAO.agregar(nuevoProducto);

        if (guardado) {
            JOptionPane.showMessageDialog(this, "Producto agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos(); // Recargar los datos en la tabla
            limpiarCamposTexto(); // Limpiar los campos de texto
            habilitarCamposTexto(); // Asegurarse de que los campos sean editables después de agregar
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        int filaSeleccionada = TablaPizzas.getSelectedRow(); // Obtener la fila seleccionada
        if (filaSeleccionada != -1) {
            // Obtener los datos de los campos de texto
            String nombre = TxtNombre.getText();
            String precioStr = TxtPrecio.getText();
            String descripcion = TxtDescripcion.getText();

            float precio;
            try {
                precio = Float.parseFloat(precioStr); // Convertir el precio a float
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idProducto = (int) TablaPizzas.getValueAt(filaSeleccionada, 0); // Obtener el ID del producto seleccionado

            IConexion conexion = new Conexion();
            IProductoDAO productoDAO = new ProductoDAO(conexion);

            // Crear un objeto Producto con los datos actualizados
            Producto productoActualizado = new Producto();
            productoActualizado.setId(idProducto);
            productoActualizado.setNombre(nombre);
            productoActualizado.setPrecio(precio);
            productoActualizado.setDescripcion(descripcion);

            // Actualizar el producto en la base de datos
            boolean actualizado = productoDAO.actualizar(productoActualizado);

            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos(); // Recargar los datos en la tabla
                habilitarCamposTexto(); // Asegurarse de que los campos sean editables después de actualizar
                limpiarCamposTexto(); // Limpiar los campos de texto
                modoAgregar = true; // Volver al modo agregar
                JBAgregarActualizar.setText("Agregar"); // Cambiar el texto del botón a "Agregar"
                JBAgregarActualizar.setActionCommand("Agregar"); // Establecer el comando del botón a "Agregar"
                JBCancelarEliminar.setText("Cancelar"); // Cambiar el texto del botón cancelar a "Cancelar"
                JBCancelarEliminar.setActionCommand("Cancelar"); // Establecer el comando del botón cancelar a "Cancelar"
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int filaSeleccionada = TablaPizzas.getSelectedRow(); // Obtener la fila seleccionada
        if (filaSeleccionada != -1) {
            int idProducto = (int) TablaPizzas.getValueAt(filaSeleccionada, 0); // Obtener el ID del producto seleccionado

            // Confirmar la eliminación
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                IConexion conexion = new Conexion();
                IProductoDAO productoDAO = new ProductoDAO(conexion);

                // Eliminar el producto de la base de datos
                boolean eliminado = productoDAO.eliminar(idProducto);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatos(); // Recargar los datos en la tabla
                    limpiarCamposTexto(); // Limpiar los campos de texto
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TxtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        TxtPrecio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TxtDescripcion = new javax.swing.JTextField();
        JBCancelarEliminar = new javax.swing.JButton();
        JBAgregarActualizar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaPizzas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 51, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nombre:");

        TxtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNombreActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Precio:");

        TxtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtPrecioActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Descripción:");

        TxtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtDescripcionActionPerformed(evt);
            }
        });

        JBCancelarEliminar.setText("Cancelar");
        JBCancelarEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBCancelarEliminarActionPerformed(evt);
            }
        });

        JBAgregarActualizar.setText("Agregar");
        JBAgregarActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAgregarActualizarActionPerformed(evt);
            }
        });

        TablaPizzas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, ""},
                {null, null, null, null},
                {null, null, null, ""},
                {null, null, null, null}
            },
            new String [] {
                "No.", "Nombre", "Precio", "Descripcion"
            }
        ));
        jScrollPane2.setViewportView(TablaPizzas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TxtDescripcion)
                            .addComponent(TxtPrecio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(TxtNombre, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(JBCancelarEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(JBAgregarActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JBCancelarEliminar)
                            .addComponent(JBAgregarActualizar))
                        .addGap(34, 34, 34))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtNombreActionPerformed

    private void TxtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtPrecioActionPerformed

    private void TxtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtDescripcionActionPerformed

    private void JBCancelarEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBCancelarEliminarActionPerformed
        // TODO add your handling code here:
        if (JBCancelarEliminar.getActionCommand().equals("Cancelar")) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres borrar el contenido de los campos?", "Confirmar cancelación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                limpiarCamposTexto();
                modoAgregar = true;
                JBAgregarActualizar.setText("Agregar");
                JBAgregarActualizar.setActionCommand("Agregar");
                JBCancelarEliminar.setText("Cancelar");
                JBCancelarEliminar.setActionCommand("Cancelar");
            }
        } else if (JBCancelarEliminar.getActionCommand().equals("Eliminar")) {
            eliminarProducto();

        }

    }//GEN-LAST:event_JBCancelarEliminarActionPerformed

    private void JBAgregarActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAgregarActualizarActionPerformed
        if (JBAgregarActualizar.getActionCommand().equals("Agregar")) {
            agregarNuevoProducto();
        } else if (JBAgregarActualizar.getActionCommand().equals("Actualizar")) {
            actualizarProducto();
        }
    }//GEN-LAST:event_JBAgregarActualizarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConsultaPizza.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsultaPizza.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsultaPizza.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsultaPizza.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsultaPizza().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBAgregarActualizar;
    private javax.swing.JButton JBCancelarEliminar;
    private javax.swing.JTable TablaPizzas;
    private javax.swing.JTextField TxtDescripcion;
    private javax.swing.JTextField TxtNombre;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
