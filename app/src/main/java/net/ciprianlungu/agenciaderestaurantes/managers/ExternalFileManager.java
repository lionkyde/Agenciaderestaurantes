package net.ciprianlungu.agenciaderestaurantes.managers;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ciprian George Lungu on 15-Nov-17.
 * Gestionador de ficheros externos que se encarga de aniadir fotos/ficheros
 *
 */

public class ExternalFileManager {
    public File fichero;

    /**
     * Metodo para guardar el fichero dentro de almacenamiento externo.
     * Se guardara la foto en formato 'IMG_yyyyMMddss.png' para evitar el duplicado
     * @param imagen parametro para pasar el bitmap del imagen.
     * @throws Exception Error en almacenamiento externo(permisos, no disponible almacenamiento etc)
     */
    public void guardarImagen(Bitmap imagen) throws Exception{
        String ruta;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddss");
        String fechaActual = sdf.format(new Date());
        String formatoFichero = "/IMG_"+fechaActual+".png";
        FileOutputStream fileOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;


        if(isExternalStorageEnabled()){
            //almacenamiento externo publico
            ruta = getRutaDirectorioExterno();
            //Creacion de fichero(foto)
            //guardar el fichero con la fecha actual+IMG + formato
            fichero = new File(ruta+formatoFichero);
            Log.d("probando","foto guardado:"+formatoFichero+" ruta completa:"+fichero.getAbsolutePath());


            //ALMACENAMIENTO DEL FOTO
            fileOutputStream = new FileOutputStream(fichero);
            byteArrayOutputStream = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.PNG,60,byteArrayOutputStream);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
        }else{
            throw new Exception("Almacenamiento externo no disponible.");
        }
    }

    /**
     * Metodo para recibir el imagen guardada en almacenamiento externo
     * @param nombreFichero Se pasara una cadena de string de la ruta del imagen
     * @return devuelve el fichero del foto
     * @throws Exception Almacenamiento externo no existe, o no está montada
     */
    public File getImageFile(String nombreFichero) throws Exception{
        File fichero;
        String ruta;
        if(isExternalStorageEnabled()){
            ruta = getRutaDirectorioExterno();
            fichero = new File(ruta+"/"+nombreFichero);
        }else{
            throw new Exception("Almacenamiento externo no disponible");
        }
        return fichero;
    }

    /**
     * Metodo booleano para verificar que el almacenamiento externo está montada
     * @return devuelve true o false de confirmacion
     */
    public boolean isExternalStorageEnabled(){
        String estado = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(estado)){
            return true;
        }
        return false;
    }

    /**
     * Metodo para recibir una cadena de caracteres de ruta del almacenamiento externo publico
     * (DIRECTORIOS)
     * @return devuelve la ruta en String
     */
    public String getRutaDirectorioExterno(){
        //COGER LA RUTA PUBLICA DEL ALMACENAMIENTO EXTERNO
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return file.getAbsolutePath();
    }
}
