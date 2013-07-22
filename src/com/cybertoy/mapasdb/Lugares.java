package com.cybertoy.mapasdb;

public class Lugares {

	private int id;
	private String nombre;
	private String descripcion;
	private double longitud;  
	private double latitud;
	private String foto;
	
	public Lugares(int id, String nombre, String descripcion, double longitud, double latitud, String foto) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.longitud = longitud;
		this.latitud = latitud;
		this.foto = foto;
	}


	/**
	 * @return the _id
	 */
	public int get_id() {
		return id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this.id = _id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the longitud
	 */
	public double getLongitud() {
		return longitud;
	}

	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	/**
	 * @return the latitud
	 */
	public double getLatitud() {
		return latitud;
	}

	/**
	 * @param latitud the latitud to set
	 */
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	/**
	 * @return the foto
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * @param foto the foto to set
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}	
	
}
