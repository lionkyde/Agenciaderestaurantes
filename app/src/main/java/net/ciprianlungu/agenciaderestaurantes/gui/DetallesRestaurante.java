package net.ciprianlungu.agenciaderestaurantes.gui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.ciprianlungu.agenciaderestaurantes.R;

import org.w3c.dom.Text;

import java.io.File;

public class DetallesRestaurante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_restaurante);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView tvNombre = (TextView)findViewById(R.id.detalles_tvNombre);
        ImageView imagenView = (ImageView)findViewById(R.id.detalles_imagen_restaurante);
        TextView tvTelefono = (TextView)findViewById(R.id.detalles_tvTelefono);
        TextView tvDireccion = (TextView)findViewById(R.id.detalles_tvDireccion);
        TextView tvEmail = (TextView)findViewById(R.id.detalles_tvEmail);

        //RECOGER LOS VARIABLES PASADOS AL INTENT
        Bundle bundle = getIntent().getExtras();

        String nombre = String.valueOf(bundle.get("nombre")); //campo nombre
        String ruta_imagen = String.valueOf(bundle.get("imagen")); //ruta imagen
        int telefono = Integer.parseInt(String.valueOf(bundle.get("telefono")));
        String direccion = String.valueOf(bundle.get("direccion"));
        String email = String.valueOf(bundle.get("email"));


        tvNombre.setText(nombre);
        tvTelefono.setText(String.valueOf(telefono));
        tvDireccion.setText(direccion);
        tvEmail.setText(email);


        //CARGA DE IMAGEN A PARTIR DEL EXTRA DE LA RUTA PASADA
        File imagen = new File(ruta_imagen);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath(),bmOptions);
        imagenView.setImageBitmap(bitmap);

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
