package net.ciprianlungu.agenciaderestaurantes.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ciprian George Lungu on 15-Nov-17.
 * Estructurador de la base de datos.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_DATABASE = "database_restaurantes"; //NOMBRE DE DATABASE
    private static final int VERSION = 1; //VERSION ACTUAL DEL BBDD
    public static final String TABLA_RESTAURANTES = "T_RESTAURANTE"; //NOMBRE DE TABLA DE RESTAURANTE
    public static final String[] COLUMNAS = new String[] {"_id","nombre","ruta_imagen","telefono","direccion","email"}; //NOMBRE DE LAS COLUMNAS

    /**
     * Constructor para database
     * @param context contexto actual del activity
     */
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
        //NO HAY ACTUALIZACION
    }
}
