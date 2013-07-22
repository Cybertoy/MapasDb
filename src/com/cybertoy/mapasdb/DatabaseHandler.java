package com.cybertoy.mapasdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Identificadores
	public static final String COLUMNA_ID = "_id";
	public static final String COLUMNA_NOMBRE = "nombre";
	public static final String COLUMNA_DESCRIPCION = "descripcion";
	public static final String COLUMNA_LONGITUD = "longitud";
	public static final String COLUMNA_LATITUD = "latitud";
	public static final String COLUMNA_FOTO = "foto";

	public static final String PROJECTION_ALL_FIELDS [] = new String[]{COLUMNA_ID, COLUMNA_NOMBRE, COLUMNA_DESCRIPCION, 
																	   COLUMNA_LONGITUD, COLUMNA_LATITUD, COLUMNA_FOTO};
	
	// Constantes
	public static final String NOMBRE_DB = "Localizaciones.db";
	private static final int VERSION_DB = 1;
	public static final String TABLA_DB = "LUGARES";

	// Sentencia SQL de creacion de tabla
	private static final String CREA_DB_QUERY = "CREATE TABLE if not exists " + TABLA_DB + "(" + 
							COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
							COLUMNA_NOMBRE + " TEXT NOT NULL," + 
							COLUMNA_DESCRIPCION + " TEXT NOT NULL," +
							COLUMNA_LONGITUD + " REAL NOT NULL," +
							COLUMNA_LATITUD + " REAL NOT NULL," +
							COLUMNA_FOTO + " TEXT); ";	
	
	public DatabaseHandler(Context context) {
		super(context, NOMBRE_DB, null, VERSION_DB);
	}
	
	@Override 
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREA_DB_QUERY);
	}
	
	@Override 
	public void onUpgrade(SQLiteDatabase db, int viejaVersion, int nuevaVersion ) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLA_DB);
		onCreate(db);
	}
}
