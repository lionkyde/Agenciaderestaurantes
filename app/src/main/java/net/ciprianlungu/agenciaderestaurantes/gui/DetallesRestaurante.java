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

/**
 * ACTIVIDAD DE DETALLES DEL RESTAURANTE, DESPUES DE HABER CLICKEADO UNA DEL LISTVIEW.
 * PARA VER LOS DETALLES DEL RESTAURANTE
 * @author Ciprian George Lungu
 */
public class DetallesRestaurante extends AppCompatActivity implements SensorEventListener {
    private static final int REQUEST_PERMISO = 2;

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
        getSupportActionBar().setHomeButtonEnabled(true); //ACTIVACION DEL BOTON DE ATRÁS

        //Guardamos los resultados de la consulta de base de datos de todos los restaurantes
        //en un cursor
        cursor = gr.getRestaurantes();

        //SENSOR
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        //IDENTIFICACION DE TEXTVIEWS/IMAGEVIEWS
        tvNombre = (TextView)findViewById(R.id.detalles_tvNombre);
        imagenView = (ImageView)findViewById(R.id.detalles_imagen_restaurante);
        tvTelefono = (TextView)findViewById(R.id.detalles_tvTelefono);
        tvDireccion = (TextView)findViewById(R.id.detalles_tvDireccion);
        tvEmail = (TextView)findViewById(R.id.detalles_tvEmail);

        //RECOGER LOS VARIABLES PASADOS AL INTENT
        Bundle bundle = getIntent().getExtras();
        int position = (int) bundle.get("position"); //POSICION CURSOR


        if(cursor != null){
            cursor.moveToPosition(position); //ACCESO A POSICION ESPECIFICA

            //RELLENAMOS LOS CAMPOS CON LOS DATOS
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

    /**
     * CLICK Metodo para mostrar los datos de la consulta de la siguiente posicion.
     * @param v objeto de vista del layout
     */
    public void siguiente(View v){
        Log.d("cursor","position:"+cursor.getPosition()+" count:"+cursor.getCount());
        if(cursor.isLast()){
            //ESTOY EN ULTIMA POSICION
            cursor.moveToFirst();
            Log.d("cursor","Next: "+cursor.getPosition());
            tvNombre.setText(cursor.getString(1));
            tvTelefono.setText(cursor.getString(3));
            tvDireccion.setText(cursor.getString(4));
            tvEmail.setText(cursor.getString(5));

            //CARGA DE IMAGEN A PARTIR DEL EXTRA DE LA RUTA PASADA
            File imagen = new File(cursor.getString(2));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath(),bmOptions);
            imagenView.setImageBitmap(bitmap);
        }else{
            //NO ESTOY EN ULTIMA POSICION
            cursor.moveToNext();
            Log.d("cursor","Next: "+cursor.getPosition());
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

    /**
     * CLICK Metodo para mostrar los datos de la consulta de la posicion anterior del actual
     * @param v objeto de vista del layout
     */
    public void anterior(View v){
        if(cursor.isFirst()){
            //ESTOY EN PRIMERA POSICION
            cursor.moveToLast();
            tvNombre.setText(cursor.getString(1));
            tvTelefono.setText(cursor.getString(3));
            tvDireccion.setText(cursor.getString(4));
            tvEmail.setText(cursor.getString(5));

            //CARGA DE IMAGEN A PARTIR DEL EXTRA DE LA RUTA PASADA
            File imagen = new File(cursor.getString(2));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath(),bmOptions);
            imagenView.setImageBitmap(bitmap);
        }else{
            //NO ESTOY EN PRIMERA POSICION
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

    /**
     * CLICK Metodo para verificacion de permiso para hacer llamada de telefono.
     * @param v
     */
    public void llamar(View v){
        if(hasPermisoLlamar()){
            //tiene permiso
            hacerLlamada();
        }else{
            //No tiene permiso
            //lo pedimos
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CALL_PHONE},
                    REQUEST_PERMISO);
        }
    }

    /**
     * Metodo de llamada de telefono
     */
    public void hacerLlamada(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        //el formato de string del telefono tiene que ser 'tel:numero'
        String telefono = "tel:"+cursor.getString(3);
        callIntent.setData(Uri.parse(telefono));
        startActivity(callIntent);
    }

    /**
     * Metodo booleano para comprobar el permiso para llamada
     * @return devuelve true o false del pemiso
     */
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

    /**
     * Estructura del intent con su mensaje personalizado para compartir el restaurante
     * @return devuelve el intent de compartir
     */
    private Intent createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"¡Estoy compartiendo un restaurante que me gustó! El restaurante "+cursor.getString(1));
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: //BOTON DE ATRAS
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_delete: //BOTON DE BORRAR
                AlertDialog dialogo = dialogoBorrado();
                dialogo.show(); //MOSTRAMOS EL DIALOGO
                return true;
            case R.id.detalles_item_map: //BOTON DE UBICACION GOOGLE MAPS
                muestraUbicacion(cursor.getString(4));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo para abrir intent a la aplicacion de google maps con la localizacion del restaurante.
     * @param direccion La direccion del restaurante
     */
    private void muestraUbicacion(String direccion){
        Uri googleUri = Uri.parse("geo:0,0?q="+Uri.encode(direccion));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,googleUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        //Comprobamos otra vez que el sensor sea de tipo acelerometro
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if(x > 8){
                if(cursor.getPosition() > 0){
                    if(cursor != null && !cursor.isAfterLast()){
                        cursor.moveToPrevious(); //anterior cursor cuando gira izquerda

                        //Rellenamos los campos
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
                if(cursor.getPosition() < (cursor.getCount()-1)){
                    if(cursor != null && !cursor.isAfterLast()){
                        cursor.moveToNext(); //movemos al siguiente cursor cuando gira derecha

                        //Rellenamos los campos
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

    /**
     * Metodo de dialogo para mostrar al usuario la confirmacion del borrado del usuario.
     * En caso caso que confirma, se borra permanentemente el restaurante de la BBDD.
     * @return devuelve el AlertDialog
     */
    private AlertDialog dialogoBorrado(){
        AlertDialog borradoDialogo =new AlertDialog.Builder(this)
                //Personalizacion del dialogo
                .setTitle("Borrar")
                .setMessage("¿Estás seguro que quieras borrar el restaurante?")
                .setIcon(R.drawable.ic_action_delete)
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //CODIGO DE BORRADO AQUI
                        try{
                            if(cursor != null){
                                gr.borrarRestaurante(Integer.parseInt(cursor.getString(0)));
                                cursor = gr.getRestaurantes();

                                Toast.makeText(getApplicationContext(),"Borrado con exito",Toast.LENGTH_SHORT).show();
                                cursor = gr.getRestaurantes();
                                if(cursor != null){
                                    //Despues de borrado del restaurante, nos posicionamos en la
                                    //primera posicion del cursor
                                    cursor.moveToFirst();

                                    //RELLENAMOS LOS DATOS
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
                            //EN CASO QUE CURSOR ESTA VACIO
                            //QUE PUEDE PASAR CUANDO BORRES TODOS LOS RESTAURANTES
                            //SE VAYA A LA ACTIVIDAD PRINCIPAL DE LISTADOS
                            NavUtils.navigateUpFromSameTask(getParent());
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //NO HACEMOS NADA
                        dialog.dismiss();
                    }
                })
                .create();
        return borradoDialogo;
    }
    public void mostrarContenido(){
        //RELLENAMOS LOS DATOS
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        //Cuando el programa o actividad está en modo pausa, que se pare el sensor
        //para evitar consumos extra de aplicacion y bateria
        senSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Cuando la actividad o programa vuelve a usar, arrancamos el listener para coger
        //datos del sensor
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Cerramos BBDD
        gr.cerrar();
    }
}
