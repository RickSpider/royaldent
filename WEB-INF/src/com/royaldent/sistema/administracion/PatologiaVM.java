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
import com.royaldent.modelo.Patologia;

public class PatologiaVM extends TemplateViewModelLocal {

	private List<Object[]> lPatologias;
	private List<Object[]> lPatologiasOri;
	private Patologia patologiaSelected;

	private boolean opCrearPatologia;
	private boolean opEditarPatologia;
	private boolean opBorrarPatologia;

	@Init(superclass = true)
	public void initPatologiaVM() {

		cargarPatologias();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposePatologiaVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearPatologia = this.operacionHabilitada(ParamsLocal.OP_CREAR_PATOLOGIA);
		this.opEditarPatologia = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PATOLOGIA);
		this.opBorrarPatologia = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PATOLOGIA);

	}

	private void cargarPatologias() {

		// this.lPatologias =
		// this.reg.getAllObjectsByCondicionOrder(Patologia.class.getName(), null,
		// "Patologiaid asc");

		String sql = this.um.getSql("patologias.sql");

		this.lPatologias = this.reg.sqlNativo(sql);

		this.lPatologiasOri = this.lPatologias;
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
	@NotifyChange("lPatologias")
	public void filtrarPatologia() {

		this.lPatologias = this.filtrarListaObject(this.filtroColumns, this.lPatologiasOri);

	}

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalPatologiaAgregar() {

		if (!this.opCrearPatologia)
			return;

		this.editar = false;
		modalPatologia(-1);

	}

	@Command
	public void modalPatologia(@BindingParam("patologiaid") long patologiaid) {

		if (patologiaid != -1) {

			if (!this.opEditarPatologia)
				return;
			
			this.patologiaSelected = this.reg.getObjectById(Patologia.class.getName(), patologiaid);
			this.editar = true;
			
		} else {

			patologiaSelected = new Patologia();

		}

		modal = (Window) Executions.createComponents("/sistema/zul/administracion/patologiaModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		return true;
	}

	@Command
	@NotifyChange("lPatologias")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}

		this.save(patologiaSelected);

		this.patologiaSelected = null;

		this.cargarPatologias();

		this.modal.detach();

		if (editar) {

			Notification.show("La Patologia fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("La Patologia fue agregada.");
		}

	}

	public Patologia getPatologiaSelected() {
		return patologiaSelected;
	}

	public void setPatologiaSelected(Patologia patologiaSelected) {
		this.patologiaSelected = patologiaSelected;
	}

	public boolean isOpCrearPatologia() {
		return opCrearPatologia;
	}

	public void setOpCrearPatologia(boolean opCrearPatologia) {
		this.opCrearPatologia = opCrearPatologia;
	}

	public boolean isOpEditarPatologia() {
		return opEditarPatologia;
	}

	public void setOpEditarPatologia(boolean opEditarPatologia) {
		this.opEditarPatologia = opEditarPatologia;
	}

	public boolean isOpBorrarPatologia() {
		return opBorrarPatologia;
	}

	public void setOpBorrarPatologia(boolean opBorrarPatologia) {
		this.opBorrarPatologia = opBorrarPatologia;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public List<Object[]> getlPatologias() {
		return lPatologias;
	}

	public void setlPatologias(List<Object[]> lPatologias) {
		this.lPatologias = lPatologias;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

}
