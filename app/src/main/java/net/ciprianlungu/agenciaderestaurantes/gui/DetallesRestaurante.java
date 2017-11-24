package net.ciprianlungu.agenciaderestaurantes.gui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.ciprianlungu.agenciaderestaurantes.R;
import net.ciprianlungu.agenciaderestaurantes.persistencia.GestorBBDDRestaurantes;

import java.io.File;

public class DetallesRestaurante extends AppCompatActivity implements SensorEventListener {
    private static final int REQUEST_PERMISO = 2;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private ShareActionProvider mShareActionProvider;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

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

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

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
        if(hasPermisoLlamar()){
            hacerLlamada();
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{
                        Manifest.permission.CALL_PHONE},
                    REQUEST_PERMISO);
        }
    }

    public void hacerLlamada(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        String telefono = "tel:"+cursor.getString(3);
        callIntent.setData(Uri.parse(telefono));
        startActivity(callIntent);
    }

    private boolean hasPermisoLlamar(){
        boolean hasPermission = false;
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            hasPermission = true;
        }
        return hasPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISO:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    hacerLlamada();
                }else{
                    Toast.makeText(this,"Sin permiso de llamada, no puedo efectuar la llamada.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_detalles,menu);

        //LOCALIZAR EL MENUITEM CON EL SHAREACTIONPROVIDER
        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
        return true;
    }

    private Intent createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"¡Estoy compartiendo un restaurante que me gustó! El restaurante "+cursor.getString(1));

        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case android.R.id.home:
                Log.d("menu","elegido el item android home");
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_delete:
                AlertDialog dialogo = dialogoBorrado();
                dialogo.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if(x > 8){
                Log.d("sensorito","Izquerda "+String.valueOf(x));
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

            }else if(x < -8){
                Log.d("sensorito","Derecha "+String.valueOf(x));
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
        }

    }
    private AlertDialog dialogoBorrado(){
        AlertDialog borradoDialogo =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Borrar")
                .setMessage("¿Estás seguro que quieras borrar el restaurante?")
                .setIcon(R.drawable.ic_action_delete)

                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //CODIGO DE BORRADO AQUI
                        try{
                            if(cursor != null){
                                Log.d("cursor","La posicion actual de borrado es:"+cursor.getPosition());
                                Log.d("cursor","El id del restaurante es:"+cursor.getString(0));

                                gr.borrarRestaurante(Integer.parseInt(cursor.getString(0)));
                                cursor = gr.getRestaurantes();

                                Toast.makeText(getApplicationContext(),"Borrado con exito",Toast.LENGTH_SHORT).show();
                                cursor = gr.getRestaurantes();
                                if(cursor != null){
                                    cursor.moveToFirst();

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
                        }catch(CursorIndexOutOfBoundsException e){
                            NavUtils.navigateUpFromSameTask(getParent());
                        }

                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return borradoDialogo;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        senSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
}
