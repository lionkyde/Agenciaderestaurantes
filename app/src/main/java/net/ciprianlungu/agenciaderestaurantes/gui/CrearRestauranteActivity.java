package net.ciprianlungu.agenciaderestaurantes.gui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import net.ciprianlungu.agenciaderestaurantes.R;
import net.ciprianlungu.agenciaderestaurantes.adaptadores.RestaurantesAdapter;
import net.ciprianlungu.agenciaderestaurantes.managers.ExternalFileManager;
import net.ciprianlungu.agenciaderestaurantes.modelo.Restaurante;
import net.ciprianlungu.agenciaderestaurantes.persistencia.GestorBBDDRestaurantes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * ACTIVIDAD DE CREACION DE RESTAURANTE
 * @author Ciprian George Lungu
 */
public class CrearRestauranteActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1; //CAMARA
    private static final int REQUEST_PERMISOS = 2; //PERMISOS
    ExternalFileManager efm;
    RestaurantesAdapter ra; //ADAPTADOR RESTAURANTE
    GestorBBDDRestaurantes gr; //BASE DE DATOS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_restaurante);
        efm = new ExternalFileManager();
        getSupportActionBar().setHomeButtonEnabled(true); //ACTIVACION DE BOTON DE ATRAS
    }

    /**
     * CLICK Metodo que verifica que realmente el usuario ha permitido todos los permisos de camera,
     * de escritura almacenamiento y lectura de almacenamiento externo
     * que despues llama al metodo takephoto para tomar foto o pedir permiso en caso contrario
     * @param view Coje el objeto desde el layout.
     */
    public void addFoto(View view) {
        if (hasPermisoAccesoCamara() && hasPermisoEscrituraAlmacenamientoExterno() && hasPermisoLecturaAlmacenamientoExterno()) {
            //SI TIENE TODOS LOS PERMISOS
            takePhoto();
        } else {
            //NO ESTA LOS PERMISOS ACTIVADOS
            //PREGUNTAMOS AL USUARIO PARA EL PERMISO
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISOS);
        }
    }

    /**
     * Metodo para arrancar el intent de camera para captura fotografica.
     */
    public void takePhoto(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camera, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras(); //COGE LOS DATOS EXTRAS DEL INTENT
            Bitmap imageBitmap = (Bitmap) extras.get("data"); //COGE EXTRA DE FOTO
            ImageView imagenview = (ImageView)findViewById(R.id.view_imagen);
            imagenview.setImageBitmap(imageBitmap);
            try {
                //GUARDAMOS EL FICHERO DENTRO DEL MOVIL
                efm.guardarImagen(imageBitmap);
                Toasty.info(this,"Imagen almacenada con exito.",Toast.LENGTH_SHORT,true).show();
            } catch (Exception e){
                Toasty.error(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * CLICK Metodo que añade a la base de datos los datos del restaurante
     * @param view Coger el objeto del layout
     */
    public void addRestaurante(View view) {
        EditText etNombre = (EditText)findViewById(R.id.etNombre);
        EditText etTelefono = (EditText)findViewById(R.id.etTelefono);
        EditText etEmail = (EditText)findViewById(R.id.etEmail);
        EditText etDireccion = (EditText)findViewById(R.id.etDireccion);
        ImageView iv_imagen = (ImageView)findViewById(R.id.view_imagen);
        String nombre = etNombre.getText().toString();
        String direccion = etDireccion.getText().toString();
        String email = etEmail.getText().toString();

        //VERIFICACION DE CAMPOS VACIOS.
        if(nombre.isEmpty()){
            Toasty.warning(this,"Campo nombre restaurante está vacío",
                    Toast.LENGTH_SHORT,true).show();
        }else if(direccion.isEmpty()){
            Toasty.warning(this,"Campo dirección postal está vacío.",
                    Toast.LENGTH_SHORT , true).show();
        }else if(email.isEmpty()){
            Toasty.warning(this,"Campo email está vacío",
                    Toast.LENGTH_SHORT , true).show();
        }else{
            //NO ESTÁN VACIOS LOS CAMPOS
            //PROCEDEMOS EL ARRANQUE DE BASE DE DATOS
            try{
                gr = new GestorBBDDRestaurantes(this);
                Restaurante restaurante = new Restaurante(etNombre.getText().toString(),efm.fichero.getAbsolutePath(),Integer.parseInt(etTelefono.getText().toString()),
                        etDireccion.getText().toString(),etEmail.getText().toString());

                gr.insertarRestaurante(restaurante);

                //VACIAMOS LOS CAMPOS DESPUES DE INSERCION A LA BBDD
                etNombre.setText("");
                etTelefono.setText("");
                etEmail.setText("");
                etDireccion.setText("");

                Toasty.success(this, "Restaurante insertado correctamente.", Toast.LENGTH_SHORT , true).show();
            } catch (NumberFormatException e){
                Toasty.warning(this,"Formato de telefono incorrecto o está vacio.",
                        Toast.LENGTH_SHORT, true).show();
            }catch(NullPointerException e){
                Toasty.warning(this,"No has tomado la foto.",
                        Toast.LENGTH_SHORT,true).show();
            }catch (Exception e){
                Toasty.error(this,"Ha ocurrido un error al insertar el restaurante",
                        Toast.LENGTH_SHORT,true).show();
            }
        }
    }

    /**
     * Metodo booleano para verificar si la aplicacion tiene permiso de camara
     * @return devuelve true(permitido) o false(no permitido)
     */
    private boolean hasPermisoAccesoCamara(){
        boolean hasPermission=false;
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            hasPermission=true;
        }
        return hasPermission;
    }

    /**
     * Metodo booleano para verificar que tiene permiso de escritura de almacenamiento externo
     * @return devuelve true o false del permiso
     */
    private boolean hasPermisoEscrituraAlmacenamientoExterno(){
        boolean hasPermission=false;
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            hasPermission=true;
        }
        return hasPermission;
    }

    /**
     * Metodo booleano para verificar el permiso de acceso de lectura de almacenamiento externo
     * @return devuelve true o false del permiso
     */
    private boolean hasPermisoLecturaAlmacenamientoExterno(){
        boolean hasPermission=false;
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            hasPermission=true;
        }
        return hasPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISOS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toasty.warning(this, "No tenemos permiso para las fotos", Toast.LENGTH_SHORT, true).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: //BOTON ACTION ATRAS
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
