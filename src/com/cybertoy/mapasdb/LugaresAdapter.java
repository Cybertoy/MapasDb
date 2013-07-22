package com.cybertoy.mapasdb;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LugaresAdapter extends ArrayAdapter<Lugares> {
	
	private Context context;
	private int resource;
	private LayoutInflater inflater;
	

	public LugaresAdapter(Context ctx, int ResourceId,List<Lugares> objects) {
		super(ctx, ResourceId, objects);
		resource = ResourceId;
		inflater = LayoutInflater.from(ctx);
		context = ctx;
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(resource, null);
		
        Lugares lugar = getItem(posicion);
        
        String path_foto;

        TextView txtNombre = (TextView) convertView.findViewById(R.id.nombre);
        TextView txtDescripcion = (TextView) convertView.findViewById(R.id.descripcion);
        
        txtNombre.setText(lugar.getNombre());
        txtDescripcion.setText(lugar.getDescripcion());

        ImageView imagen_foto = (ImageView) convertView.findViewById(R.id.imagenLugar);
        path_foto = lugar.getFoto();
		if (path_foto.contentEquals("R.drawable.camara")) {
			int imagenDefecto = R.drawable.camara;
			Drawable imagen = context.getResources().getDrawable(imagenDefecto);
	        imagen_foto.setImageDrawable(imagen);
		} else {
		    Bitmap bm = BitmapFactory.decodeFile(path_foto);
		    imagen_foto.setImageBitmap(bm);	
		}
            
            
        return convertView;
    }
}
