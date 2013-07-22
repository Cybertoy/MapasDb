package com.cybertoy.mapasdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PrincipalActivity extends Activity {

	public interface ActivitiesConstantes {
        public static final int ACTIVIDAD_LISTA = 1001;
        public static final int ACTIVIDAD_MAPAS = 1002;
        public static final int ACTIVIDAD_MOSTRAR = 1003;
        public static final int ACTIVIDAD_EDITAR = 1004;
}
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_layout);
        
        // Objetos del menu
        Button bListaLugares = (Button) findViewById(R.id.botonListaLugares);
        Button bListaMaps = (Button) findViewById(R.id.botonMapaLugares);
        
        // Acciones del menu
        // Vamos a el listado de lugares
        bListaLugares.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(PrincipalActivity.this, ListaLugaresActivity.class);
				startActivity(i);
			}
		});
        
        // Vamos al mapa con puntos de lugares
        bListaMaps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(PrincipalActivity.this, MapaLugaresActivity.class);
				startActivity(i);
			}
		});
    }
}
