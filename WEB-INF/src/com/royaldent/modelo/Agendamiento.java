package com.royaldent.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "agendamientos")
public class Agendamiento extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2001002171433549215L;
	
	@Id
	@Column(name ="agendamientoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long agendamientoid;
	

	@ManyToOne
	@JoinColumn(name="empleadoid")
	private Empleado odontologo;
	

	@ManyToOne
	@JoinColumn(name="pacienteid")
	private Paciente paciente;

	@ManyToOne
	@JoinColumn(name="consultorioid")
	private Consultorio consultorio;
	
	private Date fecha;
	
	private int duracion;

	@ManyToOne
	@JoinColumn(name="motivoid")
	private Tipo motivo;
	

	@ManyToOne
	@JoinColumn(name="estadoid")
	private Tipo estado;
	
	private String observacion;
	
	
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
	
	public Empleado getOdontologo() {
		return odontologo;
	}
	public void setOdontologo(Empleado odontologo) {
		this.odontologo = odontologo;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Tipo getEstado() {
		return estado;
	}
	public void setEstado(Tipo estado) {
		this.estado = estado;
	}
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	public Tipo getMotivo() {
		return motivo;
	}
	public void setMotivo(Tipo motivo) {
		this.motivo = motivo;
	}
	public Long getAgendamientoid() {
		return agendamientoid;
	}
	public void setAgendamientoid(Long agendamientoid) {
		this.agendamientoid = agendamientoid;
	}
	public Consultorio getConsultorio() {
		return consultorio;
	}
	public void setConsultorio(Consultorio consultorio) {
		this.consultorio = consultorio;
	}
	
}
