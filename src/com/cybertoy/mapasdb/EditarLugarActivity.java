/**
* EditarLugarActivity.java - Clase para gestionar la activity de edici—n de lugares  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cybertoy.mapasdb.PrincipalActivity.ActivitiesConstantes;
import com.google.android.gms.maps.model.LatLng;

/**
 * Clase EditarLugarActivity.
 */
public class EditarLugarActivity extends Activity implements OnClickListener {

	/** Codigo request SELECCION_FOTO. 
	 * 
	 * Este codigo es devuelto cuando vuelve de la seleccion de imagenes
	 * */
	private static final int SELECCION_FOTO = 1;
	
	/** Codigo request CAMARA. 
	 * 
	 * Este c—digo es devuelto cuando vuelve de tomar foto con la camara 
	 */
	private static final int CAMARA = 1888;

	/** Valor de retorno a la hora de seleccionar imagenes */
	private static int CARGA_IMAGEN = 1;

	/** Valor id.
	 * 
	 *  Contiene el id del lugar
	 *  */
	private int id;
	
	/** Valor foto_path 
	 * 
	 * Se inicializa a R.drawable.camara para que el objeto imagen nunca estŽ 
	 * vacio.
	 * A lo largo del c—digo se modifica para tener la ruta de la imagen
	 * */
	private String foto_path = "R.drawable.camara";

	/* Arranca la actividad
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.editar_lugar_layout);

		// Declaramos los botones de la actividad y les asignamos loslistener correspondientes
		Button btnCrear = (Button) findViewById(R.id.botonCrear);
		Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
		Button btnEliminar = (Button) findViewById(R.id.btnEliminar);

		ImageView imagenFoto = (ImageView) findViewById(R.id.imagen_foto);
		btnCrear.setOnClickListener(this);
		btnGuardar.setOnClickListener(this);
		btnEliminar.setOnClickListener(this);
		
		// Ver actividad de la que llaman para recoger los datos adecuadamente
		int activityOrigen = getIntent().getIntExtra("activity-origen", 0);

		// Recuperamos el path de la imagen por defecto para el objeto imagen de la activity
		int imagenDefecto = R.drawable.camara;
		Drawable imagen = getResources().getDrawable(imagenDefecto);

		/* Vemos desde que actividad llamamos para recuperar los datos necesarios y 
		 * mostrar los botones que necesita.
		 */
		
		switch (activityOrigen) {
		case ActivitiesConstantes.ACTIVIDAD_LISTA:
			// Oculto los botones de guardar y eliminar
			btnGuardar.setVisibility(View.GONE);
			btnEliminar.setVisibility(View.GONE);
			
			// Mostramos imagen por defecto
			imagenFoto.setImageDrawable(imagen);
			break;
		case ActivitiesConstantes.ACTIVIDAD_MAPAS:
			// Mostramos imagen por defecto
			imagenFoto.setImageDrawable(imagen);
			
			/* Recuperamos las coordenadas que vienen desde la actividad de mapas
			 * y las muestro en los campos de texto correspondientes
			 */
			Bundle bundle = getIntent().getParcelableExtra("bundle");

			LatLng posicion = bundle.getParcelable("coordenada");

			EditText.class.cast(this.findViewById(R.id.latitud)).setText(
					String.valueOf(posicion.latitude));
			EditText.class.cast(this.findViewById(R.id.longitud)).setText(
					String.valueOf(posicion.longitude));
			
			// Oculto los botones de guardar y eliminar
			btnGuardar.setVisibility(View.GONE);
			btnEliminar.setVisibility(View.GONE);
			break;
		case ActivitiesConstantes.ACTIVIDAD_MOSTRAR:
			// Oculto el boton Crear
			btnCrear.setVisibility(View.GONE);
			break;
		}
	}

	/* Cargamos los datos desde BBDD si fuera necesario, sino, 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		Toast.makeText(this, "Manten pulsada la imagen para a–adir una", Toast.LENGTH_LONG).show();
		Bundle bundle = getIntent().getExtras();

		ImageView imagenFoto = (ImageView) findViewById(R.id.imagen_foto);
		
		// registramos la imagen para que salga un menu al pulsar sobre la imagen
		registerForContextMenu(imagenFoto);

		
		// Vemos si nos llega informacion de la otra activity o no
		if (bundle != null) {
			id = bundle.getInt(DatabaseHandler.COLUMNA_ID);
		} else {
			id = -1;
		}

		/* El id que llega es mayor o igual a 0 por lo que estamos ante una edicion de lugar, se recogen los datos
		 * y se muestran en la activity
		 */
		if (id >= 0) {
			Cursor cursor = null;
			try {
				// Usamos el Content Provider para acceder a los datos
				cursor = this
						.getContentResolver()
						.query(Uri
								.withAppendedPath(LugaresProvider.CONTENT_URI,
										String.valueOf(id)),
								DatabaseHandler.PROJECTION_ALL_FIELDS, null,
								null, null);
				if (cursor.moveToFirst()) {
					
					// Recogemos los datos desde BBDD a traves del Content Provider
					String nombre = cursor.getString(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_NOMBRE));
					String descripcion = cursor
							.getString(cursor
									.getColumnIndex(DatabaseHandler.COLUMNA_DESCRIPCION));
					double latitud = cursor.getDouble(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_LATITUD));
					double longitud = cursor.getDouble(cursor
							.getColumnIndex(DatabaseHandler.COLUMNA_LONGITUD));
					
					// Si la ruta en BBDD es la generica de resources ponemos nuestra imagen defecto sino, mostramos la de la ruta dada
					if (foto_path.contentEquals("R.drawable.camara")) {
						String path_foto = cursor.getString(cursor
								.getColumnIndex(DatabaseHandler.COLUMNA_FOTO));
						if (path_foto.contentEquals("R.drawable.camara")) {
							int imagenDefecto = R.drawable.camara;
							Drawable imagen = getResources().getDrawable(
									imagenDefecto);
							imagenFoto.setImageDrawable(imagen);
						} else {
							Bitmap bm = BitmapFactory.decodeFile(path_foto);
							imagenFoto.setImageBitmap(bm);
						}
					}

					// Informamos los objetos con los datos obtenidos
					EditText.class.cast(this.findViewById(R.id.nombre))
							.setText(nombre);
					EditText.class.cast(this.findViewById(R.id.descripcion))
							.setText(descripcion);
					EditText.class.cast(this.findViewById(R.id.latitud))
							.setText(String.valueOf(latitud));
					EditText.class.cast(this.findViewById(R.id.longitud))
							.setText(String.valueOf(longitud));
				}
			} finally {
				cursor.close();
			}
		}
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Selecciona metodo captura...");
		menu.add(Menu.NONE, SELECCION_FOTO, Menu.NONE,
				"Elige de la biblioteca");
		menu.add(Menu.NONE, CAMARA, Menu.NONE, "Tomar foto");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SELECCION_FOTO:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, CARGA_IMAGEN);
			break;
		case CAMARA:
			Intent camaraIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(camaraIntent, CAMARA);
			break;
		}
		return super.onContextItemSelected(item);
	}

	/* Metodo para procesar los datos devueltos a la hora de obtener una imagen
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ImageView imagenFoto = (ImageView) findViewById(R.id.imagen_foto);
		if (resultCode == Activity.RESULT_OK) {

			// Tama–o que queremos que tenga la imagen guardada
			int scaleWidth = 400, scaleHeigth = 300;

			/* Creamos un nombre para la imagen capturada, tanto por camara como por
			 * biblioteca de imagenes. 
			 */
			String image_name = android.text.format.DateFormat.format(
					"yyyyMMdd_kkmmss", new java.util.Date()).toString();
			File dir = new File(Environment.getExternalStorageDirectory()
					+ "/.MapasDb/");
			if (!dir.exists())
				dir.mkdir();
			File file = new File(dir, "MapasDb_" + image_name + ".png");

			OutputStream outStream = null;

			try {
				outStream = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			
			/* Vemos que requestcode nos ha llegado para operar con una imagen de la biblioteca
			 * o con la camara integrada
			 */
			switch (requestCode) {
			case SELECCION_FOTO:
				Uri selectedImageUri = data.getData();

				Bitmap bitmap = null;

				// Localizamos la imagen seleccionada
				try {
					bitmap = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), selectedImageUri);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Redimensionamos la imagen
				Bitmap resized_galeria = Bitmap.createScaledBitmap(bitmap,
						scaleWidth, scaleHeigth, true);

				// Guardamos la imagen en PNG
				resized_galeria.compress(Bitmap.CompressFormat.PNG, 100,
						outStream);

				try {
					outStream.flush();
					outStream.close();
				} catch (Exception ex) {

				}

				// Asignamos la imagen al ImageView
				foto_path = file.getPath();

				imagenFoto.setImageBitmap(resized_galeria);
				break;
			case CAMARA:

				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

				// Redimensionamos la imagen
				Bitmap resized_camara = Bitmap.createScaledBitmap(thumbnail,
						scaleWidth, scaleHeigth, true);

				// Guardamos la imagen en PNG
				resized_camara.compress(Bitmap.CompressFormat.PNG, 100,
						outStream);

				try {
					outStream.flush();
					outStream.close();
				} catch (Exception ex) {

				}

				foto_path = file.getPath();
				// Mostramos la imagen en la view
				imagenFoto.setImageBitmap(thumbnail);

				break;
			}
		}
	}

	/* Listener para realizar las acciones dependiendo del boton apretado en la activity
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		ContentValues values = new ContentValues();

		// Creamos lugar o actualizamos
		if ((v.getId() == R.id.botonCrear) || ((v.getId() == R.id.btnGuardar))) {
			// Recogemos los datos del elemento pulsado
			String nombre = EditText.class.cast(findViewById(R.id.nombre))
					.getText().toString();
			String descripcion = EditText.class
					.cast(findViewById(R.id.descripcion)).getText().toString();
			String latitud = EditText.class.cast(findViewById(R.id.latitud))
					.getText().toString();
			String longitud = EditText.class.cast(findViewById(R.id.longitud))
					.getText().toString();

			Cursor cursor = null;
			if (id > 0) {
				// El Lugar ya existe, por lo que actualizamos los datos
				cursor = getContentResolver()
						.query(Uri
								.withAppendedPath(LugaresProvider.CONTENT_URI,
										String.valueOf(id)),
								DatabaseHandler.PROJECTION_ALL_FIELDS, null,
								null, null);
				if (cursor.moveToFirst()) {
					values.put(DatabaseHandler.COLUMNA_ID, id);
					values.put(DatabaseHandler.COLUMNA_NOMBRE, nombre);
					values.put(DatabaseHandler.COLUMNA_DESCRIPCION, descripcion);
					values.put(DatabaseHandler.COLUMNA_LATITUD, latitud);
					values.put(DatabaseHandler.COLUMNA_LONGITUD, longitud);
					values.put(DatabaseHandler.COLUMNA_FOTO, foto_path);

					int rowsAffected = getContentResolver().update(
							Uri.withAppendedPath(LugaresProvider.CONTENT_URI,
									String.valueOf(id)), values, null, null);
					if (rowsAffected <= 0)
						Log.w("Database", "no row affected");
					finish();
				}
			} else {
				// Nuevo Lugar, por lo que se inserta valores en BBDD
				values.put(DatabaseHandler.COLUMNA_NOMBRE, nombre);
				values.put(DatabaseHandler.COLUMNA_DESCRIPCION, descripcion);
				values.put(DatabaseHandler.COLUMNA_LATITUD, latitud);
				values.put(DatabaseHandler.COLUMNA_LONGITUD, longitud);
				values.put(DatabaseHandler.COLUMNA_FOTO, foto_path);

				getContentResolver()
						.insert(LugaresProvider.CONTENT_URI, values);
				finish();
			}
		}

		// Eliminamos el lugar 
		if (v.getId() == R.id.btnEliminar) {
			
			// Creamos la ventana de confirmaci—n de borrado del lugar
			AlertDialog.Builder dialogoEliminar = new AlertDialog.Builder(this);
			dialogoEliminar.setTitle("Importante");
			dialogoEliminar.setMessage("ÀEst‡ seguro de querer eliminar este lugar?");
			dialogoEliminar.setIcon(android.R.drawable.ic_delete);
			dialogoEliminar.setCancelable(false);
			
			// Si aceptamos se borrar‡ el lugar
			dialogoEliminar.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getContentResolver().delete(
							Uri.withAppendedPath(LugaresProvider.CONTENT_URI,
									String.valueOf(id)), null, null);
					finish();
				}
			});
			// Si no, no hacemos nada, volvemos a edicion
			dialogoEliminar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			// Mostramos la ventana de confirmaci—n
			dialogoEliminar.show();
		}
	}
}
