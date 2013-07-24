/**
* Lugares.java - Clase para gestionar los objetos de tipo Lugares  
* @author  Santiago Mart’nez Mart’nez
* @version 1.0 
*/
package com.cybertoy.mapasdb;

/**
 * Clase Lugares.
 */
public class Lugares {

	/** atributos de la clase */
	private int id;
	
	private String nombre;
	
	private String descripcion;
	
	private double longitud;
	
	private double latitud;
	
	private String foto;

	/**
	 * Instantiates a new lugares.
	 *
	 * @param id Id del lugar
	 * @param nombre Nombre
	 * @param descripcion Descripci—n del lugar
	 * @param longitud Longitud del lugar
	 * @param latitud Latitud del lugar
	 * @param foto Ruta de la imagen
	 */
	public Lugares(int id, String nombre, String descripcion, double longitud,
			double latitud, String foto) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.longitud = longitud;
		this.latitud = latitud;
		this.foto = foto;
	}

	/* GETTERS Y SETTERS DE LA CLASE */
	
	/**
	 * Devuelve el _id.
	 *
	 * @return _id
	 */
	public int get_id() {
		return id;
	}

	/**
	 * Informa el _id.
	 *
	 * @param _id
	 */
	public void set_id(int _id) {
		this.id = _id;
	}

	/**
	 * Devuelve el nombre.
	 *
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Informa el nombre.
	 *
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la descripcion.
	 *
	 * @return descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Informa la descripcion.
	 *
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve la longitud.
	 *
	 * @return longitud
	 */
	public double getLongitud() {
		return longitud;
	}

	/**
	 * Informa la longitud.
	 *
	 * @param longitud
	 */
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	/**
	 * Devuelve la latitud.
	 *
	 * @return latitud
	 */
	public double getLatitud() {
		return latitud;
	}

	/**
	 * Informa la latitud.
	 *
	 * @param latitud
	 */
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	/**
	 * Devuelve la ruta foto.
	 *
	 * @return foto
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * Informa la ruta foto.
	 *
	 * @param foto
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

}
