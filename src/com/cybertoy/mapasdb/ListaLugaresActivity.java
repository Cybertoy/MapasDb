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

public class ListaLugaresActivity extends ListActivity {
	
	private static final int MENU_OPCION = 1;
	
	private ArrayAdapter<Lugares> adapter;
	public List<Lugares> lugaresList;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.lista_lugares_layout);
		
		// Creamos mi adaptador de fila de lista
		lugaresList = new ArrayList<Lugares>();
		adapter = new LugaresAdapter(this, R.layout.lugar_row_layout,lugaresList); 
		this.setListAdapter(adapter);
		
		// Boton solo visible si la lista est‡ vac’a
		Button bAnhadir = (Button) findViewById(R.id.boton_anhadir);
		
		bAnhadir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent();
				i.setClass(ListaLugaresActivity.this, EditarLugarActivity.class);
				i.putExtra("activity-origen", 1);
				startActivityForResult(i, 1);
			}
		});
	}
	
	@Override
	protected void onResume(){
		Cursor lugaresListCursor = null;
		
		try {
			lugaresListCursor = this.getContentResolver().query(LugaresProvider.CONTENT_URI, DatabaseHandler.PROJECTION_ALL_FIELDS,null, null, null);
			this.lugaresList.clear();
			if (lugaresListCursor!=null && lugaresListCursor.moveToFirst()) {
				do {
					int id = lugaresListCursor.getInt(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_ID));
					String nombre = lugaresListCursor.getString(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_NOMBRE));
					String descripcion = lugaresListCursor.getString(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_DESCRIPCION));
					double longitud = Double.valueOf(lugaresListCursor.getString(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_LONGITUD)));
					double latitud = Double.valueOf(lugaresListCursor.getString(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_LATITUD)));
					String foto = lugaresListCursor.getString(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_FOTO));
					lugaresList.add(new Lugares(id, nombre, descripcion,latitud,longitud,foto));
				} while (lugaresListCursor.moveToNext());
			}
			this.adapter.notifyDataSetChanged();
		} finally {
			if (lugaresListCursor != null)
				lugaresListCursor.close();
		}
		super.onResume();
	}
	
	protected void onListItemClick(ListView l, View v, int posicion, long id) {
		Intent i = new Intent(this,MostrarLugarActivity.class);
		i.putExtra(DatabaseHandler.COLUMNA_ID, this.lugaresList.get(posicion).get_id());
		i.putExtra("activity-origen", 1);
		this.startActivity(i);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
		super.onCreateOptionsMenu(menu);
		menu.add(0,MENU_OPCION,0,"A–adir").setIcon(android.R.drawable.ic_input_add);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent i = null;
		switch (item.getItemId()) {
		case MENU_OPCION:
			i = new Intent(this,EditarLugarActivity.class);
			i.putExtra("activity-origen", 1);
			break;		
		}
		if (i != null) {
			startActivityForResult(i, 1);			
		}
		return true;
    }
}
