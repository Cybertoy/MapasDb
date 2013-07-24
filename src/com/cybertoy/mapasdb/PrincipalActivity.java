/**
* MapaLugaresActivity.java - Clase para controlar la pantalla de inicio de la aplicaci—n  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Clase PrincipalActivity.
 */
public class PrincipalActivity extends Activity {

	/**
	 * Interface ActivitiesConstantes.
	 * 
	 * Genero constantes que indicar‡n desde que actividad llamo
	 * 
	 */
	public interface ActivitiesConstantes {
		
		/** Constante ACTIVIDAD_LISTA. */
		public static final int ACTIVIDAD_LISTA = 1;
		
		/** Constante ACTIVIDAD_MAPAS. */
		public static final int ACTIVIDAD_MAPAS = 2;
		
		/** Constante ACTIVIDAD_MOSTRAR. */
		public static final int ACTIVIDAD_MOSTRAR = 3;
		
		/** Constante ACTIVIDAD_EDITAR. */
		public static final int ACTIVIDAD_EDITAR = 4;
	}

	/* MŽtodo que crea el menu de inicio
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal_layout);

		/* Botones del menu */
		Button bListaLugares = (Button) findViewById(R.id.botonListaLugares);
		Button bListaMaps = (Button) findViewById(R.id.botonMapaLugares);

		/* Acciones del menu
		   Vamos al listado de lugares */
		bListaLugares.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(PrincipalActivity.this, ListaLugaresActivity.class);
				startActivity(i);
			}
		});

		/* Vamos al mapa con puntos de lugares */
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
