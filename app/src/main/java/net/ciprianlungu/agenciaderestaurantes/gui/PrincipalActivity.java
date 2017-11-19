package net.ciprianlungu.agenciaderestaurantes.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import net.ciprianlungu.agenciaderestaurantes.R;
import net.ciprianlungu.agenciaderestaurantes.persistencia.GestorBBDDRestaurantes;

public class PrincipalActivity extends AppCompatActivity {
    GestorBBDDRestaurantes gr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        gr = new GestorBBDDRestaurantes(this);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.listview_filas_restaurantes,
                gr.getRestaurantes(),
                new String[] {"nombre"},
                new int[] { R.id.tvNombre});

        ListView lvListaRestaurantes = (ListView)findViewById(R.id.lvListaPersonas);
        lvListaRestaurantes.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.iAnadir:
                Intent intent = new Intent(this,CrearRestauranteActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gr.cerrar();
    }
}
