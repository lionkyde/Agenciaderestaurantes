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

public class CrearRestauranteActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_PERMISOS = 2;
    ExternalFileManager efm;
    RestaurantesAdapter ra;
    GestorBBDDRestaurantes gr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_restaurante);
        efm = new ExternalFileManager();
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void addFoto(View view) {
        if (hasPermisoAccesoCamara() && hasPermisoEscrituraAlmacenamientoExterno() && hasPermisoLecturaAlmacenamientoExterno()) {
            takePhoto();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISOS);
        }
    }
    public void takePhoto(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camera, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imagenview = (ImageView)findViewById(R.id.view_imagen);
            imagenview.setImageBitmap(imageBitmap);
            try {
                efm.guardarImagen(imageBitmap);
                Toast.makeText(this, "Imagen almacenada", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void addRestaurante(View view) {
        EditText etNombre = (EditText)findViewById(R.id.etNombre);
        EditText etTelefono = (EditText)findViewById(R.id.etTelefono);
        EditText etEmail = (EditText)findViewById(R.id.etEmail);
        EditText etDireccion = (EditText)findViewById(R.id.etDireccion);
        ImageView iv_imagen = (ImageView)findViewById(R.id.view_imagen);
        String nombre = etNombre.getText().toString();
        String direccion = etDireccion.getText().toString();
        String email = etEmail.getText().toString();

        if(nombre.isEmpty()){
            Log.d("campos","Algunos campos están vacios");
            Toast.makeText(this,"Campo nombre restaurante está vacío",
                    Toast.LENGTH_SHORT).show();
        }else if(direccion.isEmpty()){
            Toast.makeText(this,"Campo dirección postal está vacío.",
                    Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(this,"Campo email está vacío",
                    Toast.LENGTH_SHORT).show();
        }else{
            try{
                gr = new GestorBBDDRestaurantes(this);
                Restaurante restaurante = new Restaurante(etNombre.getText().toString(),efm.fichero.getAbsolutePath(),Integer.parseInt(etTelefono.getText().toString()),
                        etDireccion.getText().toString(),etEmail.getText().toString());

                Log.d("EXCEPTIONBBDD","persona creada"+etNombre.getText().toString()+efm.fichero.getAbsolutePath()+Integer.parseInt(etTelefono.getText().toString())+
                        etDireccion.getText().toString()+etEmail.getText().toString());
                gr.insertarRestaurante(restaurante);

                etNombre.setText("");
                etTelefono.setText("");
                etEmail.setText("");
                etDireccion.setText("");


                Toast.makeText(this, "Restaurante insertado correctamente.", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e){
                Toast.makeText(this,"Formato de telefono incorrecto",
                        Toast.LENGTH_SHORT).show();
            }catch(NullPointerException e){
                Toast.makeText(this,"No has tomado la foto.",
                        Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.d("error", String.valueOf(e));
                Toast.makeText(this,"Ha ocurrido un error al insertar el restaurante",
                        Toast.LENGTH_SHORT).show();
                Log.d("EXCEPTIONBBDD","exception",e);
            }
        }
    }

    //METODOS PARA DEVOLUCION DE PERMISOS DE CAMARA, ALMACENAMIENTO EXTERNO ETC.
    private boolean hasPermisoAccesoCamara(){
        boolean hasPermission=false;
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            hasPermission=true;
        }
        return hasPermission;
    }
    private boolean hasPermisoEscrituraAlmacenamientoExterno(){
        boolean hasPermission=false;
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            hasPermission=true;
        }
        return hasPermission;
    }
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
                    Toast.makeText(this, "No tenemos permiso para las fotos", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case android.R.id.home:
                Log.d("menu","elegido el item android home");
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
