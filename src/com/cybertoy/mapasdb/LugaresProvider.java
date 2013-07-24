/**
* LugaresProvider.java - Clase para generar y gestionar el Content Provider de la aplicaci—n  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/

package com.cybertoy.mapasdb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class LugaresProvider extends ContentProvider {

	public static final String AUTHORITY = "com.cybertoy.mapasdb";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/lugares");
	public static final UriMatcher URI_MATCHER;
	public static final String MIME_TYPE_ALL_ITEMS = "vnd.android.cursor.dir/vnd."
			+ AUTHORITY;
	public static final String MIME_TYPE_SINGLE_ITEM = "vnd.android.cursor.item/vnd."
			+ AUTHORITY;
	private static final String INVALID_URI_MESSAGE = "Uri Invalida: ";
	private static final String EQUALS = "=";

	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(AUTHORITY, "lugares", 1);
		URI_MATCHER.addURI(AUTHORITY, "lugares/#", 2);
	}

	private SQLiteDatabase database;
	private DatabaseHandler dbHandler;

	@Override
	public boolean onCreate() {
		dbHandler = new DatabaseHandler(getContext());
		database = dbHandler.getWritableDatabase();
		return database != null && database.isOpen();
	}

	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
		case 1:
			return MIME_TYPE_ALL_ITEMS;
		case 2:
			return MIME_TYPE_SINGLE_ITEM;
		default:
			throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
		}
	}

	private SQLiteDatabase getOrOpenDatabase() {
		SQLiteDatabase db = null;
		if (this.database != null && database.isOpen())
			db = this.database;
		else
			db = dbHandler.getWritableDatabase();

		return db;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = getOrOpenDatabase().insert(DatabaseHandler.TABLA_DB,
				DatabaseHandler.NOMBRE_DB, values);
		Uri nuevaUri = null;
		switch (URI_MATCHER.match(uri)) {
		case 1:
			if (rowID > 0)
				nuevaUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			break;
		default:
			throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
		}
		return nuevaUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsAffected = 0;
		switch (URI_MATCHER.match(uri)) {
		case 1:
			rowsAffected = getOrOpenDatabase().delete(DatabaseHandler.TABLA_DB,
					selection, selectionArgs);
			break;
		case 2:
			String singleRecordId = uri.getPathSegments().get(1);
			rowsAffected = getOrOpenDatabase().delete(DatabaseHandler.TABLA_DB,
					DatabaseHandler.COLUMNA_ID + EQUALS + singleRecordId,
					selectionArgs);
			break;
		}

		return rowsAffected;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		switch (URI_MATCHER.match(uri)) {
		case 2:
			String id = uri.getPathSegments().get(1);
			qBuilder.appendWhere(DatabaseHandler.COLUMNA_ID + "=" + id);
			break;
		case UriMatcher.NO_MATCH:
			throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
		}

		qBuilder.setTables(DatabaseHandler.TABLA_DB);
		cursor = qBuilder.query(getOrOpenDatabase(), projection, selection,
				selectionArgs, null, null, null);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int rowsAffected = 0;
		String recordId = null;
		switch (URI_MATCHER.match(uri)) {
		case 1:
			rowsAffected = getOrOpenDatabase().update(DatabaseHandler.TABLA_DB,
					values, selection, selectionArgs);
			break;
		case 2:
			recordId = uri.getPathSegments().get(1);
			rowsAffected = getOrOpenDatabase().update(DatabaseHandler.TABLA_DB,
					values, DatabaseHandler.COLUMNA_ID + EQUALS + recordId,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
		}
		return rowsAffected;
	}

}
