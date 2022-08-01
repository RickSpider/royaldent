package com.royaldent.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;

@Entity
@Table(name = "empleados")
public class Empleado extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5014067858986801344L;

	@Id
	private Long empleadoid;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="empleadoid")
	private Usuario usuario;
	
	private String nombre;
	private String apellido;

	@ManyToOne
	@JoinColumn(name="sexoid")
	private Tipo sexo;
	
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;
	private String telefono;
	

	@ManyToOne
	@JoinColumn(name="documentotipoid")
	private Tipo documentoTipo;
	private String documentoNum;
	
	@ManyToOne
	@JoinColumn(name = "ciudadid")
	private Ciudad ciudad;
	
	private String direccion;
	
	private String color;

	public Long getEmpleadoid() {
		return empleadoid;
	}
	public void setEmpleadoid(Long empleadoid) {
		this.empleadoid = empleadoid;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
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
	public Ciudad getCiudad() {
		return ciudad;
	}
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	public Tipo getSexo() {
		return sexo;
	}
	public void setSexo(Tipo sexo) {
		this.sexo = sexo;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	
	

}
