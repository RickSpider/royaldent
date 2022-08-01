package com.royaldent.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name = "patologias")
public class Patologia extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6774925545960779689L;
	
	@Id
	@Column(name ="patologiaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long patologiaid;
	private String patologia;
	private String descripcion;

	@Override
	public Object[] getArrayObjectDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getPatologiaid() {
		return patologiaid;
	}

	public void setPatologiaid(Long patologiaid) {
		this.patologiaid = patologiaid;
	}

	public String getPatologia() {
		return patologia;
	}

	public void setPatologia(String patologia) {
		this.patologia = patologia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
