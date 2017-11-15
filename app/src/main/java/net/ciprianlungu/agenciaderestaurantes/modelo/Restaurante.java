package net.ciprianlungu.agenciaderestaurantes.modelo;

/**
 * Created by Lionkyde on 15-Nov-17.
 */

public class Restaurante {
    public String nombre;
    public byte[] imagen;
    public int telefono;
    public int postal;
    public String email;

    public Restaurante(String nombre, byte[] imagen, int telefono, int postal, String email) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.telefono = telefono;
        this.postal = postal;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getPostal() {
        return postal;
    }

    public void setPostal(int postal) {
        this.postal = postal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}