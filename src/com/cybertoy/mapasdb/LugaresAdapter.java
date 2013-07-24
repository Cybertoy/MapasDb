/**
* LugaresAdapter.java - Clase de gestión del adapter
* @author  Santiago Martínez Martínez
* @version 1.0 
*/
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

/**
 * Clase LugaresAdapter.
 */
public class LugaresAdapter extends ArrayAdapter<Lugares> {

	/** Contexto. */
	private Context context;
	
	/** Recurso. */
	private int resource;
	
	/** Inflater. */
	private LayoutInflater inflater;

	/**
	 * Instanciamos el adaptador.
	 *
	 * @param ctx 
	 * @param ResourceId
	 * @param objects
	 */
	public LugaresAdapter(Context ctx, int ResourceId, List<Lugares> objects) {
		super(ctx, ResourceId, objects);
		resource = ResourceId;
		inflater = LayoutInflater.from(ctx);
		context = ctx;
	}

	/* Informamos los datos por cada fila de la lista
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int posicion, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(resource, null);

		
		/* Obtenemos el lugar */
		Lugares lugar = getItem(posicion);

		String path_foto;

		/* Informamos el Nombre y la Descripción de la fila correspondiente */
		TextView txtNombre = (TextView) convertView.findViewById(R.id.nombre);
		TextView txtDescripcion = (TextView) convertView
				.findViewById(R.id.descripcion);

		txtNombre.setText(lugar.getNombre());
		txtDescripcion.setText(lugar.getDescripcion());

		ImageView imagen_foto = (ImageView) convertView
				.findViewById(R.id.imagenLugar);
		
		/* Dibujamos la imagen del lugar, comparamos la ruta obtenida con la ruta generica R.drawable.camara, que está en resources
		 * Si es igual se informará con la imagen genérica, sino se pondrá la indicada en la ruta den BBDD
		 */
		
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
