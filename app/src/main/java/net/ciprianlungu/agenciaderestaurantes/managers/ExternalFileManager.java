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
 * Created by Lionkyde on 15-Nov-17.
 */

public class ExternalFileManager {
    public File fichero;
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


            //ALMACENAMIENTO DEL FICHERO
            fileOutputStream = new FileOutputStream(fichero);
            byteArrayOutputStream = new ByteArrayOutputStream();

            imagen.compress(Bitmap.CompressFormat.PNG,60,byteArrayOutputStream);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();

        }else{
            throw new Exception("Almacenamiento externo no disponible.");
        }
    }
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

    public boolean isExternalStorageEnabled(){
        String estado = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(estado)){
            return true;
        }
        return false;
    }
    public String getRutaDirectorioExterno(){
        //COGER LA RUTA PUBLICA DEL ALMACENAMIENTO EXTERNO
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return file.getAbsolutePath();
    }
}
