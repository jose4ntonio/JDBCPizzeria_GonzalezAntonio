/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Objeto.Producto;
import java.util.List;

/**
 *
 * @author Dell
 */
public interface IProductoDAO {

    public boolean agregar(Producto producto);

    public boolean eliminar(int id);

    public boolean actualizar(Producto producto);

    public Producto consultar(int id);

    public List<Producto> consultar();
}
