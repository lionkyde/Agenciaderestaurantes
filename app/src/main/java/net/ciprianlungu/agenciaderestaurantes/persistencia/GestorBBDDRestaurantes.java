package net.ciprianlungu.agenciaderestaurantes.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.ciprianlungu.agenciaderestaurantes.modelo.Restaurante;

/**
 * Created by Lionkyde on 15-Nov-17.
 */

public class GestorBBDDRestaurantes {
    DatabaseHelper dh;

    public GestorBBDDRestaurantes(Context context){
        dh = new DatabaseHelper(context);
    }

    //PARA AÑADIR RESTAURANTES
    public void insertarRestaurante(Restaurante r){
        SQLiteDatabase sqLiteDatabase = dh.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.COLUMNAS[1],r.getNombre());
        cv.put(DatabaseHelper.COLUMNAS[2],r.getImagen());
        cv.put(DatabaseHelper.COLUMNAS[3],r.getTelefono());
        cv.put(DatabaseHelper.COLUMNAS[4],r.getPostal());
        cv.put(DatabaseHelper.COLUMNAS[5],r.getEmail());


        Log.d("gestorbbdd","recibiendo datos...");
        sqLiteDatabase.insert(DatabaseHelper.TABLA_RESTAURANTES,null,cv);
        Log.d("gestorbbdd","acabo de meter en base de datos");
        sqLiteDatabase.close();
    }
    //RECIBIR TODOS LOS RESTAURANTES
    public Cursor getRestaurantes(){
        SQLiteDatabase sqLiteDatabase = dh.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.TABLA_RESTAURANTES, new String[] {});
        return c;
    }
    public void cerrar(){
        dh.close();
    }

    //TODO HABRÁ QUE METER AL MENOS UN RESTAURANTE, PORQUE SI NO METEMOS SALE ERROR DE NULL DEL CURSOR AL HACER CONSULTA. Y SE APAGA EL PROGRAMA.
    //TODO FALTA RECOGER LOS DATOS Y MOSTRARLO EN CADA LISTA.
}
