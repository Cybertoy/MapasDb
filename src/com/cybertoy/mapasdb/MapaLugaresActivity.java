package com.cybertoy.mapasdb;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaLugaresActivity extends FragmentActivity implements OnMarkerClickListener, OnMapClickListener {

	GoogleMap mapa;
	LocationManager lm;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_lugares_layout);
		
	    mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    mapa.setOnMapClickListener(this);
	    
	    mapa.setMyLocationEnabled(true);
	    
	    LocationListener ll = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				
				mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
				
			}
		};
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
	}

	protected void onResume() {
		mapa.clear();
		Cursor lugaresListCursor = null;
		
		try {
			lugaresListCursor = this.getContentResolver().query(LugaresProvider.CONTENT_URI, DatabaseHandler.PROJECTION_ALL_FIELDS,null, null, null);

			if (lugaresListCursor!=null && lugaresListCursor.moveToFirst()) {
				do {
					int id = lugaresListCursor.getInt(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_ID));
					double longitud = Double.valueOf(lugaresListCursor.getString(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_LONGITUD)));
					double latitud = Double.valueOf(lugaresListCursor.getString(lugaresListCursor.getColumnIndex(DatabaseHandler.COLUMNA_LATITUD)));
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
	
	public void crearMarcador (int id, LatLng coordenadas) {
		MarkerOptions marcador = new MarkerOptions();
		
		marcador.position(coordenadas);
		marcador.snippet(String.valueOf(id));
		mapa.addMarker(marcador);
		
		mapa.setOnMarkerClickListener(this);
	}

	@Override
	public boolean onMarkerClick(Marker marcador) {		
		Intent i = new Intent(this,MostrarLugarActivity.class);
		i.putExtra(DatabaseHandler.COLUMNA_ID, Integer.parseInt(marcador.getSnippet()));
		this.startActivity(i);
		
		return true;
	}

	@Override
	public void onMapClick(LatLng coordenadas) {
		Bundle bundle = new Bundle();
		Intent i = new Intent(this, EditarLugarActivity.class);
		bundle.putParcelable("coordenada", coordenadas);
		i.putExtra("bundle", bundle);
		i.putExtra("activity-origen", 2);
		startActivity(i);
	}
}
