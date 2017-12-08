package net.ciprianlungu.agenciaderestaurantes.gui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import net.ciprianlungu.agenciaderestaurantes.R;
import net.ciprianlungu.agenciaderestaurantes.persistencia.GestorBBDDRestaurantes;

import java.io.File;

import es.dmoral.toasty.Toasty;

/**
 * Actividad principal de la aplicacion.
 * Se mostrará la lista de todos los restaurantes.
 * Cuando se pulse click sobre uno de la lista de restaurante se accederá a otro intent de detalles
 * @author Ciprian George Lungu
 */
public class PrincipalActivity extends AppCompatActivity {
    GestorBBDDRestaurantes gr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mostradorLista();
    }

    /**
     * Metodo de mostrador de la lista con listview, consultando a la base de datos.
     */
    private void mostradorLista(){
        gr = new GestorBBDDRestaurantes(this);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.listview_filas_restaurantes,
                gr.getRestaurantes(),
                new String[] {"nombre","ruta_imagen"},
                new int[] { R.id.tvNombre , R.id.ivImagen});

        ListView lvListaRestaurantes = (ListView)findViewById(R.id.lvListaPersonas);
        lvListaRestaurantes.setAdapter(adapter);
        lvListaRestaurantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //LLAMADA A OTRO INTENT
                Log.d("prueba","he pulsado");
                Intent intent = new Intent(PrincipalActivity.this,DetallesRestaurante.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.iAnadir: //BOTON ANIADIR
                Intent intent = new Intent(this,CrearRestauranteActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mostradorLista();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cerramos la base de datos.
        gr.cerrar();
    }
}