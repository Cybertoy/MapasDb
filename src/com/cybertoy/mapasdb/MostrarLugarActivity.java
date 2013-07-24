/**
* MostrarLugarActivity.java - Clase para generar y gestionar la activity para mostrar el detalle del lugar  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

import com.cybertoy.mapasdb.PrincipalActivity.ActivitiesConstantes;

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

public class MostrarLugarActivity extends Activity {

	private Bundle bundle;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.mostrar_lugar_layout);

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

	@Override
	protected void onResume() {
		bundle = this.getIntent().getExtras();

		ImageView imagen_foto = (ImageView) findViewById(R.id.image_foto);

		if (bundle != null) {
			id = bundle.getInt(DatabaseHandler.COLUMNA_ID);
		} else {
			id = -1;
			int imagenDefecto = R.drawable.camara;
			Drawable imagen = getResources().getDrawable(imagenDefecto);
			imagen_foto.setImageDrawable(imagen);
		}
		if (id > 0) {
			Cursor cursor = null;
			try {
				cursor = this
						.getContentResolver()
						.query(Uri
								.withAppendedPath(LugaresProvider.CONTENT_URI,
										String.valueOf(id)),
								DatabaseHandler.PROJECTION_ALL_FIELDS, null,
								null, null);
				if (cursor.moveToFirst()) {
					String nombre = cursor.getString(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_NOMBRE));
					String descripcion = cursor
							.getString(cursor
									.getColumnIndex(DatabaseHandler.COLUMNA_DESCRIPCION));
					String path_foto = cursor.getString(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_FOTO));

					TextView.class.cast(this.findViewById(R.id.label_nombre))
							.setText(nombre);
					TextView.class.cast(
							this.findViewById(R.id.label_descripcion)).setText(
							descripcion);

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
