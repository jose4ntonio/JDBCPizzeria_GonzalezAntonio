package com.mycompany.jdbcpizzeria_gonzalezantonio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Dell (josé antonio) dell es la marca de mi compu y así :p
 */
public class JDBCPizzeria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String cadenaConexion = "jdbc:mysql://localhost:3306/pizzeria";
        String user = "root";
        String pwd = "235621";

        // Sentencia SQL para insertar productos
        String insertarProducto = "INSERT INTO producto (nombre, precio, descripcion) VALUES (?,?,?)";

        // Sentencia SQL para eliminar un producto por id_producto
        String eliminarProducto = "DELETE FROM producto WHERE id_producto = ?";

        try {
            // Crear conexión a la base de datos
            Connection c = DriverManager.getConnection(cadenaConexion, user, pwd);

            // Preparar la sentencia SQL para insertar productos
            PreparedStatement insert = c.prepareStatement(insertarProducto, Statement.RETURN_GENERATED_KEYS);

            // 1.- agregar 3 productos
            // Insertar la primera pizza
            insert.setString(1, "Pizza Cuatro Quesos");
            insert.setFloat(2, 150.0f);
            insert.setString(3, "Pizza con mozzarella, cheddar, gorgonzola y parmesano");
            insert.executeUpdate();
            System.out.println("Pizza Cuatro Quesos agregada.");

            // Insertar la segunda pizza
            insert.setString(1, "Pizza Mexicana");
            insert.setFloat(2, 160.0f);
            insert.setString(3, "Pizza con chorizo, jalapeños, y frijoles refritos");
            insert.executeUpdate();
            System.out.println("Pizza Mexicana agregada.");

            // Insertar la tercera pizza
            insert.setString(1, "Pizza Barbacoa");
            insert.setFloat(2, 170.0f);
            insert.setString(3, "Pizza con carne de res, cebolla, y salsa barbacoa");
            insert.executeUpdate();
            System.out.println("Pizza Barbacoa agregada.");

            // 2.- eliminar un producto
            // Preparar la sentencia SQL para eliminar el producto
            PreparedStatement delete = c.prepareStatement(eliminarProducto);

            // Asignar el id_producto del producto a eliminar (en este caso, supongamos que es el id 2)
            delete.setInt(1, 2);

            // Ejecutar la sentencia de eliminación
            int rowsAffected = delete.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Producto eliminado exitosamente.");
            } else {
                System.out.println("No se encontró ningún producto con el id proporcionado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
