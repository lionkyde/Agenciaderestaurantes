package net.ciprianlungu.agenciaderestaurantes.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.ciprianlungu.agenciaderestaurantes.R;

import org.w3c.dom.Text;


/**
 * Created by Lionkyde on 15-Nov-17.
 */

public class RestaurantesAdapter extends CursorAdapter{
    public RestaurantesAdapter(Context context, Cursor c){
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.listview_filas_restaurantes,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNombre = (TextView)view.findViewById(R.id.tvNombre);
        tvNombre.setText(cursor.getString(1));
    }
}
