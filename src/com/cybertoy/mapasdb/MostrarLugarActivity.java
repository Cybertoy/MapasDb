/**
* MostrarLugarActivity.java - Clase para generar y gestionar la activity para mostrar el detalle del lugar  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cybertoy.mapasdb.PrincipalActivity.ActivitiesConstantes;

/**
 * Clase MostrarLugarActivity.
 */
public class MostrarLugarActivity extends Activity {

	private Bundle bundle;
	
	private int id;

	/* Creamos la activity para mostrar lugares
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.mostrar_lugar_layout);

		/* Asociamos al boton Editar un Listener para que nos lleve
		 * a la activity de edici—n, le pasamos el id del lugar y desde que activity le llamo
		 */
		Button bEditar = (Button) findViewById(R.id.boton_editar);
		bundle = getIntent().getExtras();
		bEditar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bundle != null) {
					id = bundle.getInt(DatabaseHandler.COLUMNA_ID);
				} else {
					id = -1;
				}
				Intent i = new Intent();
				i.setClass(MostrarLugarActivity.this, EditarLugarActivity.class);
				i.putExtra(DatabaseHandler.COLUMNA_ID, id);
				i.putExtra("activity-origen", ActivitiesConstantes.ACTIVIDAD_MOSTRAR);
				startActivity(i);
				finish();
			}
		});
	}

	/* Carga los datos de BBDD en la activity
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		bundle = this.getIntent().getExtras();

		ImageView imagen_foto = (ImageView) findViewById(R.id.image_foto);

		
		// Si nos envia datos otra activity...
		if (bundle != null) {
			//Recogemos el id del lugar
			id = bundle.getInt(DatabaseHandler.COLUMNA_ID);
		} else {
			// Ponemos un id menor que 0 y mostramos la imagen por defecto en el imageview
			id = -1;
			int imagenDefecto = R.drawable.camara;
			Drawable imagen = getResources().getDrawable(imagenDefecto);
			imagen_foto.setImageDrawable(imagen);
		}
		
		// Nuestro id es positivo
		if (id > 0) {
			Cursor cursor = null;
			try {
				// Recogemos el dato mediante la query de nuestro Content Provider
				cursor = this
						.getContentResolver()
						.query(Uri
								.withAppendedPath(LugaresProvider.CONTENT_URI,
										String.valueOf(id)),
								DatabaseHandler.PROJECTION_ALL_FIELDS, null,
								null, null);
				if (cursor.moveToFirst()) {
					// Recogemos los datos
					String nombre = cursor.getString(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_NOMBRE));
					String descripcion = cursor
							.getString(cursor
									.getColumnIndex(DatabaseHandler.COLUMNA_DESCRIPCION));
					double latitud = cursor.getDouble(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_LATITUD));
					double longitud = cursor.getDouble(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_LONGITUD));
					
					// Informamos los valores en su view correspondiente
					String path_foto = cursor.getString(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_FOTO));

					TextView.class.cast(this.findViewById(R.id.label_nombre))
							.setText(nombre);
					TextView.class.cast(
							this.findViewById(R.id.label_descripcion)).setText(
							descripcion);
					TextView.class.cast(this.findViewById(R.id.coordenadas))
					.setText("Lat:" + String.valueOf(latitud) + " Long:" + String.valueOf(longitud));

					
					/* Dibujamos en el Imageview la imagen por defecto o la que trae dependiendo de la ruta que
					 * tenga almacenada.
					 */		
					if (path_foto.contentEquals("R.drawable.camara")) {
						int imagenDefecto = R.drawable.camara;
						Drawable imagen = getResources().getDrawable(
								imagenDefecto);
						imagen_foto.setImageDrawable(imagen);
					} else {
						Bitmap bm = BitmapFactory.decodeFile(path_foto);
						imagen_foto.setImageBitmap(bm);
					}

				}
			} finally {
				cursor.close();
			}
		}
		super.onResume();
	}

}
