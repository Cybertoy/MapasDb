/**
* MapaLugaresActivity.java - Clase para generar y gestionar el mapa con la vista de los lugares existentes en BBDD  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cybertoy.mapasdb.PrincipalActivity.ActivitiesConstantes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The Class MapaLugaresActivity.
 */
public class MapaLugaresActivity extends FragmentActivity implements
		OnMarkerClickListener, OnMapClickListener {

	/** Objeto mapa. */
	GoogleMap mapa;
	
	/** Gestor de localizaci—n. */
	LocationManager lm;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_lugares_layout);
		
		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mapa.setOnMapClickListener(this);

		mapa.setMyLocationEnabled(true);
		
		Location miLocalizacion = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(miLocalizacion != null){
			Log.i("SMM", "antes long");
			
			double longitud = miLocalizacion.getLongitude();
			double latitud = miLocalizacion.getLatitude();
			
			LatLng posicion = new LatLng(latitud, longitud);
			
			mapa.moveCamera(CameraUpdateFactory.newLatLng(posicion));
			mapa.animateCamera(CameraUpdateFactory.zoomTo(20));
		}

		LocationListener ll = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onLocationChanged(Location location) {

			}
		};
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
	}

	/* COn este metodo cargamos y recargamos los puntos configurados en BBDD
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		/* Borramos los puntos obtenidos, por si se ha de actualizar algœn punto */
		mapa.clear();
		
		Cursor lugaresListCursor = null;

		try {
			/* Accedemos a los datos de la BBDD */
			lugaresListCursor = this.getContentResolver().query(
					LugaresProvider.CONTENT_URI,
					DatabaseHandler.PROJECTION_ALL_FIELDS, null, null, null);

			if (lugaresListCursor != null && lugaresListCursor.moveToFirst()) {
				do {
					/* Creamos cada marcador obtenido de la BBDD */
					int id = lugaresListCursor.getInt(lugaresListCursor
							.getColumnIndex(DatabaseHandler.COLUMNA_ID));
					double longitud = Double
							.valueOf(lugaresListCursor.getString(lugaresListCursor
									.getColumnIndex(DatabaseHandler.COLUMNA_LONGITUD)));
					double latitud = Double
							.valueOf(lugaresListCursor.getString(lugaresListCursor
									.getColumnIndex(DatabaseHandler.COLUMNA_LATITUD)));
					LatLng coordenadas = new LatLng(latitud, longitud);

					crearMarcador(id, coordenadas);

				} while (lugaresListCursor.moveToNext());
			}
		} finally {
			if (lugaresListCursor != null)
				lugaresListCursor.close();
		}
		super.onResume();
	}

	/**
	 * Crear marcador.
	 *
	 * @param id
	 * @param coordenadas
	 */
	public void crearMarcador(int id, LatLng coordenadas) {
		MarkerOptions marcador = new MarkerOptions();

		marcador.position(coordenadas);
		marcador.snippet(String.valueOf(id));
		mapa.addMarker(marcador);

		mapa.setOnMarkerClickListener(this);
	}

	/* Activado cuando se pulsa un marcador, se ha usado el snippet para guardar el id del elemento y as’ poderlo pasar a la actividad Mostrar 
	 * a falta de m‡s funcionalidad por parte de la API de Google que resuelva este tema.
	 * 
	 * @see com.google.android.gms.maps.GoogleMap.OnMarkerClickListener#onMarkerClick(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public boolean onMarkerClick(Marker marcador) {
		Intent i = new Intent(this, MostrarLugarActivity.class);
		i.putExtra(DatabaseHandler.COLUMNA_ID,
				Integer.parseInt(marcador.getSnippet()));
		this.startActivity(i);

		return true;
	}

	/* Cuando se pulsa en el mapa y no es un marcador se pasa la posici—n donde se ha pulsado a la actividad editar para as’ tenerlo
	 * informado ya en el formulario.
	 * 
	 * @see com.google.android.gms.maps.GoogleMap.OnMapClickListener#onMapClick(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMapClick(LatLng coordenadas) {
		Bundle bundle = new Bundle();
		Intent i = new Intent(this, EditarLugarActivity.class);
		bundle.putParcelable("coordenada", coordenadas);
		i.putExtra("bundle", bundle);
		i.putExtra("activity-origen", ActivitiesConstantes.ACTIVIDAD_MAPAS);
		startActivity(i);
	}
}
