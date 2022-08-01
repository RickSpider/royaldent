package com.royaldent.sistema.administracion;

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
import com.royaldent.modelo.Consultorio;

public class ConsultorioVM extends TemplateViewModelLocal {

	private List<Object[]> lConsultorios;
	private List<Object[]> lConsultoriosOri;
	private Consultorio consultorioSelected;

	private boolean opCrearConsultorio;
	private boolean opEditarConsultorio;
	private boolean opBorrarConsultorio;

	@Init(superclass = true)
	public void initConsultorioVM() {

		cargarConsultorios();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeConsultorioVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearConsultorio = this.operacionHabilitada(ParamsLocal.OP_CREAR_CONSULTORIO);
		this.opEditarConsultorio = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CONSULTORIO);
		this.opBorrarConsultorio = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CONSULTORIO);

	}

	private void cargarConsultorios() {

		String sql = this.um.getSql("consultorios.sql");

		this.lConsultorios = this.reg.sqlNativo(sql);

		this.lConsultoriosOri = this.lConsultorios;
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
	@NotifyChange("lConsultorios")
	public void filtrarConsultorio() {

		this.lConsultorios = this.filtrarListaObject(this.filtroColumns, this.lConsultoriosOri);

	}

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalConsultorioAgregar() {

		if (!this.opCrearConsultorio)
			return;

		this.editar = false;
		modalConsultorio(-1);

	}

	@Command
	public void modalConsultorio(@BindingParam("consultorioid") long consultorioid) {

		if (consultorioid != -1) {

			if (!this.opEditarConsultorio)
				return;
			
			this.consultorioSelected = this.reg.getObjectById(Consultorio.class.getName(), consultorioid);
			this.editar = true;
			
		} else {

			consultorioSelected = new Consultorio();

		}

		modal = (Window) Executions.createComponents("/sistema/zul/administracion/consultorioModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		return true;
	}

	@Command
	@NotifyChange("lConsultorios")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}

		this.save(consultorioSelected);

		this.consultorioSelected = null;

		this.cargarConsultorios();

		this.modal.detach();

		if (editar) {

			Notification.show("El Consultorio fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("El Consultorio fue agregada.");
		}

	}

	public Consultorio getConsultorioSelected() {
		return consultorioSelected;
	}

	public void setConsultorioSelected(Consultorio consultorioSelected) {
		this.consultorioSelected = consultorioSelected;
	}

	public boolean isOpCrearConsultorio() {
		return opCrearConsultorio;
	}

	public void setOpCrearConsultorio(boolean opCrearConsultorio) {
		this.opCrearConsultorio = opCrearConsultorio;
	}

	public boolean isOpEditarConsultorio() {
		return opEditarConsultorio;
	}

	public void setOpEditarConsultorio(boolean opEditarConsultorio) {
		this.opEditarConsultorio = opEditarConsultorio;
	}

	public boolean isOpBorrarConsultorio() {
		return opBorrarConsultorio;
	}

	public void setOpBorrarConsultorio(boolean opBorrarConsultorio) {
		this.opBorrarConsultorio = opBorrarConsultorio;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public List<Object[]> getlConsultorios() {
		return lConsultorios;
	}

	public void setlConsultorios(List<Object[]> lConsultorios) {
		this.lConsultorios = lConsultorios;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

}
