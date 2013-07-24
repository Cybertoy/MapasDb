/**
* DatabaseHandler.java - Clase para generar y gestionar la Base de Datos  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase DatabaseHandler.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	/** Constante para campo COLUMNA_ID. */
	public static final String COLUMNA_ID = "_id";
	
	/** Constante para campo COLUMNA_NOMBRE. */
	public static final String COLUMNA_NOMBRE = "nombre";
	
	/** Constante para campo COLUMNA_DESCRIPCION. */
	public static final String COLUMNA_DESCRIPCION = "descripcion";
	
	/** Constante para campo COLUMNA_LONGITUD. */
	public static final String COLUMNA_LONGITUD = "longitud";
	
	/** Constante para campo COLUMNA_LATITUD. */
	public static final String COLUMNA_LATITUD = "latitud";
	
	/** Constante para campo COLUMNA_FOTO. */
	public static final String COLUMNA_FOTO = "foto";

	/** Constante para tabla PROJECTION_ALL_FIELDS. */
	public static final String PROJECTION_ALL_FIELDS[] = new String[] {
			COLUMNA_ID, COLUMNA_NOMBRE, COLUMNA_DESCRIPCION, COLUMNA_LONGITUD,
			COLUMNA_LATITUD, COLUMNA_FOTO };

	/** Constante para nombre de la BBDD NOMBRE_DB. */
	public static final String NOMBRE_DB = "Localizaciones.db";
	
	/** Constante para version de la BBDD VERSION_DB. */
	private static final int VERSION_DB = 1;
	
	/** Constante para nombre de la tabla TABLA_DB. */
	public static final String TABLA_DB = "LUGARES";


	/** Sentencia SQL de creaci—n de la tabla CREA_DB_QUERY. */
	private static final String CREA_DB_QUERY = "CREATE TABLE if not exists "
			+ TABLA_DB + "(" + COLUMNA_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNA_NOMBRE
			+ " TEXT NOT NULL," + COLUMNA_DESCRIPCION + " TEXT NOT NULL,"
			+ COLUMNA_LONGITUD + " REAL NOT NULL," + COLUMNA_LATITUD
			+ " REAL NOT NULL," + COLUMNA_FOTO + " TEXT); ";

	/**
	 * Constructor DatabaseHandler.
	 * 
	 * Genera al llamarla la BBDD de la aplicaci—n
	 *
	 * @param context Contexto
	 */
	public DatabaseHandler(Context context) {
		super(context, NOMBRE_DB, null, VERSION_DB);
	}

	/** MŽtodo para la creacion de la tabla en BBDD 
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREA_DB_QUERY);
	}

	/** MŽtodo para actualizar la tabla de BBDD
	 * 
	 * Se borra la tabla y se crea una nueva
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int viejaVersion, int nuevaVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLA_DB);
		onCreate(db);
	}
}
