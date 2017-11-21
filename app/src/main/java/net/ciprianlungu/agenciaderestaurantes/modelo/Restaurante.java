package net.ciprianlungu.agenciaderestaurantes.modelo;

import android.util.Log;

/**
 * Created by Lionkyde on 15-Nov-17.
 */

public class Restaurante {
    public String nombre;
    public String imagen;
    public int telefono;
    public String direccion;
    public String email;

    public Restaurante(String nombre, String imagen, int telefono, String direccion, String email) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}













