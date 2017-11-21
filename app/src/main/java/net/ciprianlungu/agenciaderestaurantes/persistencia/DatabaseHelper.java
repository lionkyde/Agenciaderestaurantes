package net.ciprianlungu.agenciaderestaurantes.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Lionkyde on 15-Nov-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_DATABASE = "database_restaurantes";
    private static final int VERSION = 1;
    public static final String TABLA_RESTAURANTES = "T_RESTAURANTE";
    public static final String[] COLUMNAS = new String[] {"_id","nombre","ruta_imagen","telefono","direccion","email"};

    public DatabaseHelper(Context context) {

        super(context, NOMBRE_DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String crearTabla_restaurantes="CREATE TABLE "+TABLA_RESTAURANTES
                +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT,ruta_imagen TEXT , telefono INTEGER, direccion TEXT, email TEXT ) ";
        sqLiteDatabase.execSQL(crearTabla_restaurantes);

        Log.d("bbdd","BASE DE DATOS CREADA.TABLA "+TABLA_RESTAURANTES+" creada.");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
