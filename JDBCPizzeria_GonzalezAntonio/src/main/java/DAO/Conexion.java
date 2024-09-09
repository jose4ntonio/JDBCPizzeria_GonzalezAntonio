/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Interfaces.IConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Dell (José Antonio) 235621
 */
public class Conexion implements IConexion {

    private String cadena = "jdbc:mysql://localhost:3306/pizzeria";
    private String usuario = "root";
    private String pwd = "235621";

    @Override
    public Connection crearConexion() {
        try {
            Connection c = DriverManager.getConnection(cadena, usuario, pwd);
            return c;
        } catch (SQLException e) {
            System.out.println("Hubo un error de conexión");
        }
        return null;
    }
}
