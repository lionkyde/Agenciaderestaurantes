package net.ciprianlungu.agenciaderestaurantes.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.ciprianlungu.agenciaderestaurantes.modelo.Restaurante;

/**
 * Created by Ciprian George Lungu on 15-Nov-17.
 * Clase que maneja la gestion de la base de datos
 * Haciendo querys o actualizacion/modificacion de los datos.
 */

public class GestorBBDDRestaurantes {
    DatabaseHelper dh;

    /**
     * Indicar al que estructurador utilizar.
     * @param context
     */
    public GestorBBDDRestaurantes(Context context){
        dh = new DatabaseHelper(context);
    }

    /**
     * Metodo para insercion de la base de datos
     * @param r Recibe un objeto Restaurante
     */
    public void insertarRestaurante(Restaurante r){
        SQLiteDatabase sqLiteDatabase = dh.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.COLUMNAS[1],r.getNombre());
        cv.put(DatabaseHelper.COLUMNAS[2],r.getImagen());
        cv.put(DatabaseHelper.COLUMNAS[3],r.getTelefono());
        cv.put(DatabaseHelper.COLUMNAS[4],r.getDireccion());
        cv.put(DatabaseHelper.COLUMNAS[5],r.getEmail());


        sqLiteDatabase.insert(DatabaseHelper.TABLA_RESTAURANTES,null,cv);
        sqLiteDatabase.close();
    }

    /**
     * Metodo que recibe la consulta de todos los restaurantes de la base de datos
     * @return devuelve un cursor de toda la consulta de BBDD
     */
    public Cursor getRestaurantes(){
        SQLiteDatabase sqLiteDatabase = dh.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.TABLA_RESTAURANTES, new String[] {});
        return c;
    }

    /**
     * Metodo para recibir un restaurante especifico con la consulta de un campo
     * @param id el id del restaurante
     * @return Devuelve un cursor de la consulta de la BBDD
     */
    public Cursor getRestaurante(long id){
        SQLiteDatabase sqLiteDatabase = dh.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseHelper.TABLA_RESTAURANTES + " WHERE "+DatabaseHelper.COLUMNAS[0] +" = "+id, new String[]{});
        return cursor;
    }

    /**
     * Metodo para borrar restaurante especifico.
     * @param id el id del restaurante
     */
    public void borrarRestaurante(int id){
        SQLiteDatabase sqLiteDatabase = dh.getWritableDatabase();
        sqLiteDatabase.delete(DatabaseHelper.TABLA_RESTAURANTES,"_id=?",new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    /**
     * Metodo para cerrar sesion de la BBDD
     */
    public void cerrar(){
        dh.close();
    }

}
