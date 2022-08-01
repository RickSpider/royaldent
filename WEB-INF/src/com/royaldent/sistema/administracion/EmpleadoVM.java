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
import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;
import com.doxacore.util.UtilStaticMetodos;
import com.royaldent.modelo.Empleado;

public class EmpleadoVM extends TemplateViewModelLocal {

	private List<Object[]> lEmpleados;
	private List<Object[]> lEmpleadosOri;
	private Empleado empleadoSelected;
	private Usuario usuarioSelected;

	private boolean opCrearEmpleado;
	private boolean opEditarEmpleado;
	private boolean opBorrarEmpleado;

	@Init(superclass = true)
	public void initEmpleadoVM() {

		cargarEmpleados();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEmpleadoVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearEmpleado = this.operacionHabilitada(ParamsLocal.OP_CREAR_EMPLEADO);
		this.opEditarEmpleado = this.operacionHabilitada(ParamsLocal.OP_EDITAR_EMPLEADO);
		this.opBorrarEmpleado = this.operacionHabilitada(ParamsLocal.OP_BORRAR_EMPLEADO);

	}

	private void cargarEmpleados() {

		String sql = this.um.getSql("empleados.sql");

		this.lEmpleados = this.reg.sqlNativo(sql);

		this.lEmpleadosOri = this.lEmpleados;
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
	@NotifyChange("lEmpleados")
	public void filtrarEmpleado() {

		this.lEmpleados = this.filtrarListaObject(this.filtroColumns, this.lEmpleadosOri);

	}

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalEmpleadoAgregar() {

		if (!this.opCrearEmpleado)
			return;

		this.editar = false;
		modalEmpleado(-1);

	}

	@Command
	public void modalEmpleado(@BindingParam("empleadoid") long empleadoid) {

		if (empleadoid != -1) {

			if (!this.opEditarEmpleado)
				return;
			
			this.empleadoSelected = this.reg.getObjectById(Empleado.class.getName(), empleadoid);
			this.usuarioSelected = this.empleadoSelected.getUsuario();
			
			this.buscarDocumento=this.empleadoSelected.getDocumentoTipo().getTipo();
			this.buscarSexo=this.empleadoSelected.getSexo().getTipo();
			this.buscarCiudad=this.empleadoSelected.getCiudad().getCiudad();
			
			this.editar = true;
			
		} else {

			empleadoSelected = new Empleado();
			usuarioSelected = new Usuario();
			
			this.buscarDocumento="";
			this.buscarSexo="";
			this.buscarCiudad="";

		}

		modal = (Window) Executions.createComponents("/sistema/zul/administracion/empleadoModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		return true;
	}

	@Command
	@NotifyChange("lEmpleados")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}
		
		this.usuarioSelected.setFullName(this.empleadoSelected.getNombre()+ " " +this.empleadoSelected.getApellido());

		if (this.usuarioSelected.getPassword().length() != 64) {
			
			this.usuarioSelected.setPassword((UtilStaticMetodos.getSHA256(this.usuarioSelected.getPassword())));		
			
		}
		
		this.empleadoSelected.setUsuario(this.save(usuarioSelected));
		
		this.empleadoSelected.setEmpleadoid(this.empleadoSelected.getUsuario().getUsuarioid());
		
		this.save(this.empleadoSelected);

		this.empleadoSelected = null;
		this.usuarioSelected = null;

		this.cargarEmpleados();

		this.modal.detach();

		if (editar) {

			Notification.show("El Empleado fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("El Empleado fue agregada.");
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
			this.empleadoSelected.setDocumentoTipo(buscarSelectedDocumento);

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
			this.empleadoSelected.setSexo(buscarSelectedSexo);

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
			this.empleadoSelected.setCiudad(buscarSelectedCiudad);

		}

		// fin buscar ciudad

	public Empleado getEmpleadoSelected() {
		return empleadoSelected;
	}

	public void setEmpleadoSelected(Empleado empleadoSelected) {
		this.empleadoSelected = empleadoSelected;
	}

	public boolean isOpCrearEmpleado() {
		return opCrearEmpleado;
	}

	public void setOpCrearEmpleado(boolean opCrearEmpleado) {
		this.opCrearEmpleado = opCrearEmpleado;
	}

	public boolean isOpEditarEmpleado() {
		return opEditarEmpleado;
	}

	public void setOpEditarEmpleado(boolean opEditarEmpleado) {
		this.opEditarEmpleado = opEditarEmpleado;
	}

	public boolean isOpBorrarEmpleado() {
		return opBorrarEmpleado;
	}

	public void setOpBorrarEmpleado(boolean opBorrarEmpleado) {
		this.opBorrarEmpleado = opBorrarEmpleado;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public List<Object[]> getlEmpleados() {
		return lEmpleados;
	}

	public void setlEmpleados(List<Object[]> lEmpleados) {
		this.lEmpleados = lEmpleados;
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
