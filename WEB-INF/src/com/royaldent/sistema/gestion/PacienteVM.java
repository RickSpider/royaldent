package com.royaldent.sistema.gestion;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.royaldent.util.ParamsLocal;
import com.royaldent.util.TemplateViewModelLocal;
import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;
import com.royaldent.modelo.Paciente;
import com.royaldent.modelo.Patologia;

public class PacienteVM extends TemplateViewModelLocal {

	private List<Object[]> lPacientes;
	private List<Object[]> lPacientesOri;
	private Paciente pacienteSelected;
	private Usuario usuarioSelected;

	private boolean opCrearPaciente;
	private boolean opEditarPaciente;
	private boolean opBorrarPaciente;

	@Init(superclass = true)
	public void initPacienteVM() {

		cargarPacientes();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposePacienteVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearPaciente = this.operacionHabilitada(ParamsLocal.OP_CREAR_PACIENTE);
		this.opEditarPaciente = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PACIENTE);
		this.opBorrarPaciente = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PACIENTE);

	}

	private void cargarPacientes() {

		// this.lPacientes =
		// this.reg.getAllObjectsByCondicionOrder(Paciente.class.getName(), null,
		// "Pacienteid asc");

		String sql = this.um.getSql("pacientes.sql");

		this.lPacientes = this.reg.sqlNativo(sql);

		this.lPacientesOri = this.lPacientes;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lPacientes")
	public void filtrarPaciente() {

		this.lPacientes = this.filtrarListaObject(this.filtroColumns, this.lPacientesOri);

	}

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalPacienteAgregar() {

		if (!this.opCrearPaciente)
			return;

		this.editar = false;
		modalPaciente(-1);

	}

	@Command
	public void modalPaciente(@BindingParam("pacienteid") long pacienteid) {

		if (pacienteid != -1) {

			if (!this.opEditarPaciente)
				return;

			this.pacienteSelected = this.reg.getObjectById(Paciente.class.getName(), pacienteid);
			
			this.buscarDocumento=this.pacienteSelected.getDocumentoTipo().getTipo();
			this.buscarSexo=this.pacienteSelected.getSexo().getTipo();
			this.buscarCiudad=this.pacienteSelected.getCiudad().getCiudad();
			
			this.editar = true;

		} else {

			pacienteSelected = new Paciente();
			
			this.buscarDocumento="";
			this.buscarSexo="";
			this.buscarCiudad="";

		}

		modal = (Window) Executions.createComponents("/sistema/zul/gestion/pacienteModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	public void modalPacientePatologia(@BindingParam("pacienteid") long pacienteid) {

		modal = (Window) Executions.createComponents("/sistema/zul/gestion/pacientePatologiaModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		return true;
	}
	
	public void borrarPatologia(@BindingParam("patologia") Patologia patologia) {
		
		this.pacienteSelected.getPatologias().remove(patologia);
		
	}

	@Command
	@NotifyChange("lPacientes")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}

		this.save(pacienteSelected);

		this.pacienteSelected = null;

		this.cargarPacientes();

		this.modal.detach();

		if (editar) {

			Notification.show("El Paciente fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("El Paciente fue agregada.");
		}

	}

	// buscarDocumentoTipo

	private List<Object[]> lDocumentosBuscarOri = null;
	private List<Object[]> lDocumentosBuscar = null;
	private Tipo buscarSelectedDocumento = null;
	private String buscarDocumento = "";

	@Command
	@NotifyChange("lDocumentosBuscar")
	public void filtrarDocumentoBuscar() {

		this.lDocumentosBuscar = this.filtrarListaObject(buscarDocumento, this.lDocumentosBuscarOri);

	}

	@Command
	@NotifyChange("lDocumentosBuscar")
	public void generarListaBuscarDocumento() {

		String buscarDocumentoSQL = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_DOCUMENTO);

		// System.out.println(buscarDocumentoSQL);

		this.lDocumentosBuscar = this.reg.sqlNativo(buscarDocumentoSQL);

		this.lDocumentosBuscarOri = this.lDocumentosBuscar;
	}

	@Command
	@NotifyChange("buscarDocumento")
	public void onSelectDocumento(@BindingParam("id") long id) {

		this.buscarSelectedDocumento = this.reg.getObjectById(Tipo.class.getName(), id);
		this.buscarDocumento = this.buscarSelectedDocumento.getTipo();
		this.pacienteSelected.setDocumentoTipo(buscarSelectedDocumento);

	}

	// fin buscarDocument

	// buscarSexoTipo

	private List<Object[]> lSexosBuscarOri = null;
	private List<Object[]> lSexosBuscar = null;
	private Tipo buscarSelectedSexo = null;
	private String buscarSexo = "";

	@Command
	@NotifyChange("lSexosBuscar")
	public void filtrarSexoBuscar() {

		this.lSexosBuscar = this.filtrarListaObject(buscarSexo, this.lSexosBuscarOri);

	}

	@Command
	@NotifyChange("lSexosBuscar")
	public void generarListaBuscarSexo() {

		String buscarSexoSQL = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_SEXO);

		this.lSexosBuscar = this.reg.sqlNativo(buscarSexoSQL);

		this.lSexosBuscarOri = this.lSexosBuscar;
	}

	@Command
	@NotifyChange("buscarSexo")
	public void onSelectSexo(@BindingParam("id") long id) {

		this.buscarSelectedSexo = this.reg.getObjectById(Tipo.class.getName(), id);
		this.buscarSexo = this.buscarSelectedSexo.getTipo();
		this.pacienteSelected.setSexo(buscarSelectedSexo);

	}

	// fin sexo tipo

	// buscador de Ciudad

	private List<Object[]> lCiudadesBuscarOri = null;
	private List<Object[]> lCiudadesBuscar = null;
	private Ciudad buscarSelectedCiudad = null;
	private String buscarCiudad = "";

	@Command
	@NotifyChange("lCiudadesBuscar")
	public void filtrarCiudadBuscar() {

		this.lCiudadesBuscar = this.filtrarListaObject(buscarCiudad, this.lCiudadesBuscarOri);

	}

	@Command
	@NotifyChange("lCiudadesBuscar")
	public void generarListaBuscarCiudad() {

		this.lCiudadesBuscar = this.reg.sqlNativo(this.um.getCoreSql("ciudades.sql"));

		this.lCiudadesBuscarOri = this.lCiudadesBuscar;
	}

	@Command
	@NotifyChange("buscarCiudad")
	public void onSelectCiudad(@BindingParam("id") long id) {

		this.buscarSelectedCiudad = this.reg.getObjectById(Ciudad.class.getName(), id);
		this.buscarCiudad = this.buscarSelectedCiudad.getCiudad();
		this.pacienteSelected.setCiudad(buscarSelectedCiudad);

	}

	// fin buscar ciudad

	public Paciente getPacienteSelected() {
		return pacienteSelected;
	}

	public void setPacienteSelected(Paciente pacienteSelected) {
		this.pacienteSelected = pacienteSelected;
	}

	public boolean isOpCrearPaciente() {
		return opCrearPaciente;
	}

	public void setOpCrearPaciente(boolean opCrearPaciente) {
		this.opCrearPaciente = opCrearPaciente;
	}

	public boolean isOpEditarPaciente() {
		return opEditarPaciente;
	}

	public void setOpEditarPaciente(boolean opEditarPaciente) {
		this.opEditarPaciente = opEditarPaciente;
	}

	public boolean isOpBorrarPaciente() {
		return opBorrarPaciente;
	}

	public void setOpBorrarPaciente(boolean opBorrarPaciente) {
		this.opBorrarPaciente = opBorrarPaciente;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public List<Object[]> getlPacientes() {
		return lPacientes;
	}

	public void setlPacientes(List<Object[]> lPacientes) {
		this.lPacientes = lPacientes;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public Usuario getUsuarioSelected() {
		return usuarioSelected;
	}

	public void setUsuarioSelected(Usuario usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
	}

	public List<Object[]> getlDocumentosBuscar() {
		return lDocumentosBuscar;
	}

	public void setlDocumentosBuscar(List<Object[]> lDocumentosBuscar) {
		this.lDocumentosBuscar = lDocumentosBuscar;
	}

	public String getBuscarDocumento() {
		return buscarDocumento;
	}

	public void setBuscarDocumento(String buscarDocumento) {
		this.buscarDocumento = buscarDocumento;
	}

	public List<Object[]> getlSexosBuscar() {
		return lSexosBuscar;
	}

	public void setlSexosBuscar(List<Object[]> lSexosBuscar) {
		this.lSexosBuscar = lSexosBuscar;
	}

	public String getBuscarSexo() {
		return buscarSexo;
	}

	public void setBuscarSexo(String buscarSexo) {
		this.buscarSexo = buscarSexo;
	}

	public List<Object[]> getlCiudadesBuscar() {
		return lCiudadesBuscar;
	}

	public void setlCiudadesBuscar(List<Object[]> lCiudadesBuscar) {
		this.lCiudadesBuscar = lCiudadesBuscar;
	}

	public String getBuscarCiudad() {
		return buscarCiudad;
	}

	public void setBuscarCiudad(String buscarCiudad) {
		this.buscarCiudad = buscarCiudad;
	}

}
