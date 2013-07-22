package com.cybertoy.mapasdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
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

import com.google.android.gms.maps.model.LatLng;

public class EditarLugarActivity extends Activity implements OnClickListener {

	private static final int FOTOS_EXISTENTES=1;
	private static final int CAMARA=1888;
	
	private static int RESULT_LOAD_IMAGE = 1;
			
	private int id;
	private String foto_path = "R.drawable.camara";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.editar_lugar_layout);
		
		Button btnCrear = (Button) findViewById(R.id.botonCrear);
		Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
		Button btnEliminar = (Button) findViewById(R.id.btnEliminar);
		
		ImageView imagenFoto = (ImageView) findViewById(R.id.imagen_foto);
		btnCrear.setOnClickListener(this);
		btnGuardar.setOnClickListener(this);
		btnEliminar.setOnClickListener(this);
		
		// Ver actividad de la que llaman para recoger los datos adecuadamente 
		int activityOrigen = getIntent().getIntExtra("activity-origen", 0);

    	int imagenDefecto = R.drawable.camara;
		Drawable imagen = getResources().getDrawable(imagenDefecto);	
    	
		switch (activityOrigen) {
        case 1:
        	btnGuardar.setVisibility(View.GONE);
        	btnEliminar.setVisibility(View.GONE);
        	imagenFoto.setImageDrawable(imagen);
            break;
        case 2:
        	imagenFoto.setImageDrawable(imagen);
        	Bundle bundle = getIntent().getParcelableExtra("bundle");
    		
    		LatLng posicion = bundle.getParcelable("coordenada");
    		
    		EditText.class.cast(this.findViewById(R.id.latitud)).setText(String.valueOf(posicion.latitude));
    		EditText.class.cast(this.findViewById(R.id.longitud)).setText(String.valueOf(posicion.longitude));
    		btnGuardar.setVisibility(View.GONE);
        	btnEliminar.setVisibility(View.GONE);
            break;
        case 3:
        	btnCrear.setVisibility(View.GONE);
        	break;
        }
	}
	
	@Override
	public void onResume() {
		Bundle bundle = getIntent().getExtras();

		ImageView imagenFoto = (ImageView) findViewById(R.id.imagen_foto);
		registerForContextMenu(imagenFoto);
		
		if ( bundle != null) {
			id = bundle.getInt(DatabaseHandler.COLUMNA_ID);
		} else {
			id = -1;
		}

		if (id >= 0) {
			Cursor cursor = null;
			try {
				cursor = this.getContentResolver().query(Uri.withAppendedPath(LugaresProvider.CONTENT_URI, String.valueOf(id)), DatabaseHandler.PROJECTION_ALL_FIELDS, null,null,null);
				if (cursor.moveToFirst()) {
					String nombre = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMNA_NOMBRE));
					String descripcion = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMNA_DESCRIPCION));
					double latitud = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.COLUMNA_LATITUD));
					double longitud = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.COLUMNA_LONGITUD));
					if (foto_path.contentEquals("R.drawable.camara")) {
						String path_foto = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMNA_FOTO));
						if (path_foto.contentEquals("R.drawable.camara")) {
				        	int imagenDefecto = R.drawable.camara;
				        	Drawable imagen = getResources().getDrawable(imagenDefecto);
				        	imagenFoto.setImageDrawable(imagen);
						} else {
					        Bitmap bm = BitmapFactory.decodeFile(path_foto);
					        imagenFoto.setImageBitmap(bm);	
						}
					}
					
					EditText.class.cast(this.findViewById(R.id.nombre)).setText(nombre);
					EditText.class.cast(this.findViewById(R.id.descripcion)).setText(descripcion);
					EditText.class.cast(this.findViewById(R.id.latitud)).setText(String.valueOf(latitud));
					EditText.class.cast(this.findViewById(R.id.longitud)).setText(String.valueOf(longitud));
				}
			}
			finally
			{
				cursor.close();
			}
		}
		super.onResume();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.setHeaderTitle("Selecciona metodo captura...");
		menu.add(Menu.NONE, FOTOS_EXISTENTES, Menu.NONE, "Elige de la biblioteca");
		menu.add(Menu.NONE, CAMARA, Menu.NONE, "Tomar foto");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case FOTOS_EXISTENTES:
			Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	      	startActivityForResult(i, RESULT_LOAD_IMAGE);
			break;
		case CAMARA:
			Intent camaraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			startActivityForResult(camaraIntent, CAMARA);
			break;
		}
		return super.onContextItemSelected(item);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	ImageView imagenFoto = (ImageView) findViewById(R.id.imagen_foto);
    	if (resultCode == Activity.RESULT_OK) {
		
    		// Tama–o que queremos que tenga la imagen guardada
    		int scaleWidth = 400, scaleHeigth = 300;
		
    		String image_name = android.text.format.DateFormat.format("yyyyMMdd_kkmmss", new java.util.Date()).toString();
    		File dir = new File(Environment.getExternalStorageDirectory() + "/.MapasDb/");
    		if (!dir.exists())
    			dir.mkdir();
    		File file = new File(dir, "MapasDb_" + image_name + ".png");
		
    		OutputStream outStream = null;
		
    		try {
    			outStream = new FileOutputStream(file);
    		} catch (FileNotFoundException e1) {
    			e1.printStackTrace();
    		}
		
    		switch (requestCode) {
    		case FOTOS_EXISTENTES:
    			Uri selectedImageUri = data.getData();

    			Bitmap bitmap = null;

    			// 	Localizamos la imagen seleccionada
    			try {
    				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}

    			// 	Redimensionamos la imagen
    			Bitmap resized_galeria = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeigth, true);

    			// Guardamos la imagen en PNG
    			resized_galeria.compress(Bitmap.CompressFormat.PNG, 100, outStream);

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
    			Bitmap resized_camara = Bitmap.createScaledBitmap(thumbnail, scaleWidth, scaleHeigth, true);
    			
    			// Guardamos la imagen en PNG
    			resized_camara.compress(Bitmap.CompressFormat.PNG, 100, outStream);
    			
    			try {
    				outStream.flush();
    				outStream.close();
    			} catch (Exception ex) {
    				
    			}
    			
    			foto_path = file.getPath();
    			imagenFoto.setImageBitmap(thumbnail);

				break;
    		}
    	}
    }
	
	@Override
	public void onClick(View v) {
		ContentValues values = new ContentValues();
		
		if ((v.getId() == R.id.botonCrear) || ((v.getId() == R.id.btnGuardar))) {
			// Recogemos los datos del elemento pulsado
			String nombre = EditText.class.cast(findViewById(R.id.nombre)).getText().toString();
			String descripcion = EditText.class.cast(findViewById(R.id.descripcion)).getText().toString();
			String latitud = EditText.class.cast(findViewById(R.id.latitud)).getText().toString();
			String longitud = EditText.class.cast(findViewById(R.id.longitud)).getText().toString();
			
			Cursor cursor = null;
			if (id > 0) {
				// El Lugar ya existe, por lo que actualizamos los datos
				cursor = getContentResolver().query(Uri.withAppendedPath(LugaresProvider.CONTENT_URI, String.valueOf(id)), DatabaseHandler.PROJECTION_ALL_FIELDS, null,null,null);
				if (cursor.moveToFirst()) {
					values.put(DatabaseHandler.COLUMNA_ID, id);
					values.put(DatabaseHandler.COLUMNA_NOMBRE, nombre);
					values.put(DatabaseHandler.COLUMNA_DESCRIPCION, descripcion);
					values.put(DatabaseHandler.COLUMNA_LATITUD, latitud);
					values.put(DatabaseHandler.COLUMNA_LONGITUD, longitud);
					values.put(DatabaseHandler.COLUMNA_FOTO, foto_path);
					
					int rowsAffected = getContentResolver().update(Uri.withAppendedPath(LugaresProvider.CONTENT_URI, String.valueOf(id)), values, null,null);
					if (rowsAffected <= 0)
						Log.w("Database","no row affected");
				}
			} else {
				// Nuevo Lugar, por lo que se inserta
				values.put(DatabaseHandler.COLUMNA_NOMBRE, nombre);
				values.put(DatabaseHandler.COLUMNA_DESCRIPCION, descripcion);
				values.put(DatabaseHandler.COLUMNA_LATITUD, latitud);
				values.put(DatabaseHandler.COLUMNA_LONGITUD, longitud);
				values.put(DatabaseHandler.COLUMNA_FOTO, foto_path);
				
				getContentResolver().insert(LugaresProvider.CONTENT_URI, values);
			}
		}
		
		if (v.getId() == R.id.btnEliminar) {
			getContentResolver().delete(Uri.withAppendedPath(LugaresProvider.CONTENT_URI, String.valueOf(id)), null, null);
			Toast.makeText(this, "Lugar eliminado", Toast.LENGTH_SHORT).show();
		}
		
		finish();
	}
}
