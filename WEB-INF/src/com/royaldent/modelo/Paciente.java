package com.royaldent.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "pacientes")
public class Paciente extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5669292145764846689L;
	
	@Id
	@Column(name ="pacienteid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long pacienteid;
	private String nombre;
	private String apellido;
	

	@ManyToOne
	@JoinColumn(name="sexoid")
	private Tipo sexo;
	
	private String direccion;
	private String email;
	private String telefono;
	
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;
	
	@ManyToOne
	@JoinColumn(name = "documentotipoid")
	private Tipo documentoTipo;
	
	private String documentoNum;

	@ManyToOne
	@JoinColumn(name = "ciudadid")
	private Ciudad ciudad;
	
	@ManyToMany
	@JoinColumn(name = "patologiaid")
	private List<Patologia> patologias;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
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
	public Long getPacienteid() {
		return pacienteid;
	}
	public void setPacienteid(Long pacienteid) {
		this.pacienteid = pacienteid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getFullNombre() {
		return this.nombre+" "+this.apellido;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Tipo getDocumentoTipo() {
		return documentoTipo;
	}
	public void setDocumentoTipo(Tipo documentoTipo) {
		this.documentoTipo = documentoTipo;
	}
	public String getDocumentoNum() {
		return documentoNum;
	}
	public void setDocumentoNum(String documentoNum) {
		this.documentoNum = documentoNum;
	}
	public Ciudad getCiudad() {
		return ciudad;
	}
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Tipo getSexo() {
		return sexo;
	}

	public void setSexo(Tipo sexo) {
		this.sexo = sexo;
	}

	public List<Patologia> getPatologias() {
		return patologias;
	}

	public void setPatologias(List<Patologia> patologias) {
		this.patologias = patologias;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
	

}
