/**
* ListaLugaresActivity.java - Clase para generar y gestionar la lista en la activity 
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cybertoy.mapasdb.PrincipalActivity.ActivitiesConstantes;

/**
 * Clase ListaLugaresActivity.
 */
public class ListaLugaresActivity extends ListActivity {

	/** Opcion pulsada en el menu del boton fisico menu MENU_OPCION. */
	private static final int MENU_OPCION = 1;

	/** Adaptador */
	private ArrayAdapter<Lugares> adapter;
	
	/** Lista de lugares */
	public List<Lugares> lugaresList;

	/* Creacion del activity, damos de alta sus objetos
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.lista_lugares_layout);

		/* Creamos mi adaptador de fila de lista */
		lugaresList = new ArrayList<Lugares>();
		adapter = new LugaresAdapter(this, R.layout.lugar_row_layout,
				lugaresList);
		this.setListAdapter(adapter);

		/* Boton solo visible si la lista est‡ vac’a */
		Button bAnhadir = (Button) findViewById(R.id.boton_anhadir);

		/* Listener del boton a–adir, solo visible si la lista est‡ vacia */
		bAnhadir.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(ListaLugaresActivity.this, EditarLugarActivity.class);
				i.putExtra("activity-origen", ActivitiesConstantes.ACTIVIDAD_LISTA);
				startActivityForResult(i, ActivitiesConstantes.ACTIVIDAD_LISTA);
			}
		});
	}

	/* Cada vez que se vuelve a la actividad refresco la lista
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		Cursor lugaresListCursor = null;
		/* Rellenamos la lista de lugares, usamos el content provider para acceder a los datos. */
		try {
			lugaresListCursor = this.getContentResolver().query(
					LugaresProvider.CONTENT_URI,
					DatabaseHandler.PROJECTION_ALL_FIELDS, null, null, null);
			this.lugaresList.clear();
			if (lugaresListCursor != null && lugaresListCursor.moveToFirst()) {
				do {
					
					/* Obtenemos los datos del lugar */
					int id = lugaresListCursor.getInt(lugaresListCursor
							.getColumnIndex(DatabaseHandler.COLUMNA_ID));
					String nombre = lugaresListCursor
							.getString(lugaresListCursor
									.getColumnIndex(DatabaseHandler.COLUMNA_NOMBRE));
					String descripcion = lugaresListCursor
							.getString(lugaresListCursor
									.getColumnIndex(DatabaseHandler.COLUMNA_DESCRIPCION));
					double longitud = Double
							.valueOf(lugaresListCursor.getString(lugaresListCursor
									.getColumnIndex(DatabaseHandler.COLUMNA_LONGITUD)));
					double latitud = Double
							.valueOf(lugaresListCursor.getString(lugaresListCursor
									.getColumnIndex(DatabaseHandler.COLUMNA_LATITUD)));
					String foto = lugaresListCursor.getString(lugaresListCursor
							.getColumnIndex(DatabaseHandler.COLUMNA_FOTO));
					
					/* Con los datos obtenidos a–adimos el lugar a la lista */
					lugaresList.add(new Lugares(id, nombre, descripcion,
							latitud, longitud, foto));
				} while (lugaresListCursor.moveToNext());
			}
			this.adapter.notifyDataSetChanged();
		} finally {
			if (lugaresListCursor != null)
				lugaresListCursor.close();
		}
		super.onResume();
	}

	/* Metodo que se activa al pulsar una de las filas de la lista para mostrar el detalle del lugar
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int posicion, long id) {
		Intent i = new Intent(this, MostrarLugarActivity.class);
		i.putExtra(DatabaseHandler.COLUMNA_ID, this.lugaresList.get(posicion)
				.get_id());
		i.putExtra("activity-origen", ActivitiesConstantes.ACTIVIDAD_LISTA);
		this.startActivity(i);
	}

	/* Creamos el menu accesible con el boton fisico para a–adir lugar
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_OPCION, 0, "A–adir").setIcon(
				android.R.drawable.ic_input_add);
		return true;
	}

	/* Hemos seleccionado una opci—n del menu de boton menu
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;
		switch (item.getItemId()) {
		case MENU_OPCION:
			i = new Intent(this, EditarLugarActivity.class);
			i.putExtra("activity-origen", ActivitiesConstantes.ACTIVIDAD_LISTA);
			break;
		}
		if (i != null) {
			startActivityForResult(i, 1);
		}
		return true;
	}
}
