package DAO;

import Interfaces.IConexion;
import Interfaces.IProductoDAO;
import Objeto.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dell (José Antonio) 235621
 */
public class ProductoDAO implements IProductoDAO {

    private IConexion conexion;

    public ProductoDAO(IConexion conexion) {
        this.conexion = conexion; // Aquí se guarda la conexión que se va a usar
    }

    @Override
    public boolean agregar(Producto producto) {
        try {
            Connection bd = conexion.crearConexion(); // Se crea la conexión con la base de datos
            String insertar = "INSERT INTO producto (nombre, descripcion, precio) VALUES (?,?,?)";
            PreparedStatement i = bd.prepareStatement(insertar);
            i.setString(1, producto.getNombre()); // Aquí se pone el nombre del producto
            i.setString(2, producto.getDescripcion()); // Aquí se pone la descripción del producto
            i.setFloat(3, producto.getPrecio()); // Aquí se pone el precio del producto

            i.executeUpdate(); // Ejecuta la inserción en la base de datos
            return true; // Retorna true si todo sale bien

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error en caso de que ocurra
            return false; // Retorna false si hubo un error
        }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            Connection bd = conexion.crearConexion(); // Se crea la conexión con la base de datos
            String eliminar = "DELETE FROM producto WHERE id_producto = ?";
            PreparedStatement e = bd.prepareStatement(eliminar);
            e.setInt(1, id); // Aquí se pone el id del producto que se quiere eliminar

            int rowsAffected = e.executeUpdate(); // Ejecuta la eliminación en la base de datos
            return rowsAffected > 0; // Retorna true si se eliminó al menos un registro

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error en caso de que ocurra
            return false; // Retorna false si hubo un error
        }
    }

    @Override
    public boolean actualizar(Producto producto) {
        try {
            Connection bd = conexion.crearConexion(); // Se crea la conexión con la base de datos
            String actualizar = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ? WHERE id_producto = ?";
            PreparedStatement u = bd.prepareStatement(actualizar);
            u.setString(1, producto.getNombre()); // Aquí se pone el nuevo nombre del producto
            u.setString(2, producto.getDescripcion()); // Aquí se pone la nueva descripción del producto
            u.setFloat(3, producto.getPrecio()); // Aquí se pone el nuevo precio del producto
            u.setInt(4, producto.getId()); // Aquí se pone el id del producto que se quiere actualizar

            int rowsAffected = u.executeUpdate(); // Ejecuta la actualización en la base de datos
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error en caso de que ocurra
            return false; // Retorna false si hubo un error
        }
    }

    @Override
    public Producto consultar(int id) {
        try {
            Connection bd = conexion.crearConexion(); // Se crea la conexión con la base de datos
            String buscarProducto = "SELECT * FROM producto WHERE id_producto = ?";
            PreparedStatement busqueda = bd.prepareStatement(buscarProducto);
            busqueda.setInt(1, id); // Aquí se pone el id del producto que se quiere buscar

            ResultSet resultado = busqueda.executeQuery(); // Ejecuta la consulta en la base de datos

            if (resultado.next()) {
                Producto p = new Producto();
                p.setId(resultado.getInt("id_producto")); // Aquí se obtiene el id del producto
                p.setNombre(resultado.getString("nombre")); // Aquí se obtiene el nombre del producto
                p.setDescripcion(resultado.getString("descripcion")); // Aquí se obtiene la descripción del producto
                p.setPrecio(resultado.getFloat("precio")); // Aquí se obtiene el precio del producto

                return p; // Retorna el producto encontrado
            } else {
                return null; // Retorna null si no se encuentra el producto
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error en caso de que ocurra
            return null; // Retorna null si hubo un error
        }
    }

    @Override
    public List<Producto> consultar() {
        try {
            Connection bd = conexion.crearConexion(); // Se crea la conexión con la base de datos
            String consultarTodos = "SELECT * FROM producto";
            PreparedStatement consulta = bd.prepareStatement(consultarTodos);

            ResultSet resultado = consulta.executeQuery(); // Ejecuta la consulta para obtener todos los productos

            List<Producto> productos = new ArrayList<>();
            while (resultado.next()) {
                Producto p = new Producto();
                p.setId(resultado.getInt("id_producto")); // Aquí se obtiene el id del producto
                p.setNombre(resultado.getString("nombre")); // Aquí se obtiene el nombre del producto
                p.setDescripcion(resultado.getString("descripcion")); // Aquí se obtiene la descripción del producto
                p.setPrecio(resultado.getFloat("precio")); // Aquí se obtiene el precio del producto
                productos.add(p); // Agrega el producto a la lista
            }
            return productos; // Retorna la lista de productos

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error en caso de que ocurra
            return null; // Retorna null si hubo un error
        }
    }
}
