package net.ciprianlungu.agenciaderestaurantes.gui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.ciprianlungu.agenciaderestaurantes.R;
import net.ciprianlungu.agenciaderestaurantes.persistencia.DatabaseHelper;
import net.ciprianlungu.agenciaderestaurantes.persistencia.GestorBBDDRestaurantes;

import org.w3c.dom.Text;

import java.io.File;

public class DetallesRestaurante extends AppCompatActivity {
    GestorBBDDRestaurantes gr = new GestorBBDDRestaurantes(this);
    Cursor cursor;
    TextView tvNombre;
    ImageView imagenView;
    TextView tvTelefono;
    TextView tvDireccion;
    TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_restaurante);
        getSupportActionBar().setHomeButtonEnabled(true);
        cursor = gr.getRestaurantes();

        tvNombre = (TextView)findViewById(R.id.detalles_tvNombre);
        imagenView = (ImageView)findViewById(R.id.detalles_imagen_restaurante);
        tvTelefono = (TextView)findViewById(R.id.detalles_tvTelefono);
        tvDireccion = (TextView)findViewById(R.id.detalles_tvDireccion);
        tvEmail = (TextView)findViewById(R.id.detalles_tvEmail);

        //RECOGER LOS VARIABLES PASADOS AL INTENT
        Bundle bundle = getIntent().getExtras();

        int position = (int) bundle.get("position"); //campo nombre


        if(cursor != null){
            cursor.moveToPosition(position);

            tvNombre.setText(cursor.getString(1));
            tvTelefono.setText(cursor.getString(3));
            tvDireccion.setText(cursor.getString(4));
            tvEmail.setText(cursor.getString(5));

            //CARGA DE IMAGEN A PARTIR DEL EXTRA DE LA RUTA PASADA
            File imagen = new File(cursor.getString(2));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath(),bmOptions);
            imagenView.setImageBitmap(bitmap);
        }
    }

    public void siguiente(View v){
        Log.d("cursor","position:"+cursor.getPosition()+" count:"+cursor.getCount());
        if(cursor.getPosition() < (cursor.getCount()-1)){
            if(cursor != null && !cursor.isAfterLast()){
                cursor.moveToNext();

                tvNombre.setText(cursor.getString(1));
                tvTelefono.setText(cursor.getString(3));
                tvDireccion.setText(cursor.getString(4));
                tvEmail.setText(cursor.getString(5));

                //CARGA DE IMAGEN A PARTIR DEL EXTRA DE LA RUTA PASADA
                File imagen = new File(cursor.getString(2));
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath(),bmOptions);
                imagenView.setImageBitmap(bitmap);
            }
        }
    }
    public void anterior(View v){
        if(cursor.getPosition() > 0){
            if(cursor != null && !cursor.isAfterLast()){
                cursor.moveToPrevious();

                tvNombre.setText(cursor.getString(1));
                tvTelefono.setText(cursor.getString(3));
                tvDireccion.setText(cursor.getString(4));
                tvEmail.setText(cursor.getString(5));

                //CARGA DE IMAGEN A PARTIR DEL EXTRA DE LA RUTA PASADA
                File imagen = new File(cursor.getString(2));
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath(),bmOptions);
                imagenView.setImageBitmap(bitmap);
            }
        }
    }
    public void llamar(View v){
        //TODO PRIMERO HAY QUE PEDIRLE LOS PERMISOS PARA PODER HACER LLAMADA. CREAMOS BOOLEANO DE PERMISO Y LUEGO HACEMOS LLAMADA
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
