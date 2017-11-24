package net.ciprianlungu.agenciaderestaurantes.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.ciprianlungu.agenciaderestaurantes.R;

import org.w3c.dom.Text;

import java.io.File;


/**
 * Created by Ciprian George Lungu on 15-Nov-17.
 *
 */

public class RestaurantesAdapter extends CursorAdapter{
    /**
     * Constructor que recibe el contexto y el cursor
     * @param context devuelve el context actual
     * @param c uso de cursor para manejo de base de datos
     */
    public RestaurantesAdapter(Context context, Cursor c){
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.listview_filas_restaurantes,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //BUSQUEDA DE IDS
        TextView tvNombre = (TextView)view.findViewById(R.id.tvNombre);
        ImageView imageview = (ImageView)view.findViewById(R.id.view_imagen);
        tvNombre.setText(cursor.getString(1));

        //CARGA DE IMAGEN
        File imagen = new File(cursor.getString(2));
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath(),bmOptions);
        imageview.setImageBitmap(bitmap);
    }
}
