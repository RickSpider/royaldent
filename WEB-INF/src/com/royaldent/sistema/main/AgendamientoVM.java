package com.royaldent.sistema.main;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Window;

import com.doxacore.modelo.Tipo;
import com.royaldent.modelo.Agendamiento;
import com.royaldent.modelo.Consultorio;
import com.royaldent.modelo.Empleado;
import com.royaldent.modelo.Paciente;
import com.royaldent.sistema.main.calendar.AgendamientoCalendarItem;
import com.royaldent.util.ParamsLocal;
import com.royaldent.util.TemplateViewModelLocal;

public class AgendamientoVM extends TemplateViewModelLocal {

	private SimpleCalendarModel calendarModel;
	private Agendamiento agendamientoSelected;

	@Init(superclass = true)
	public void initConsultorioVM() {
		cargarAgendamientos();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeConsultorioVM(@ContextParam(ContextType.VIEW) Component view) {

		Selectors.wireEventListeners(view, this);

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub

	}

	

	@NotifyChange("calendarModel")
	public boolean cargarAgendamientos() {

		
		/*
		 * SimpleDateFormat dataSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		 * 
		 * 
		 * String[][] evts = { // Red Events { "2022/07/30 00:00", "2022/07/30 03:00",
		 * STYLE_RED, HEADER_STYLE_RED, CONTENT_STYLE_RED, "ZK Jet Released" }
		 * 
		 * };
		 * 
		 * 
		 * 
		 * calendarModel = new SimpleCalendarModel(); for (int i = 0; i < evts.length;
		 * i++) { SimpleCalendarItem sce = new SimpleCalendarItem(); try {
		 * sce.setBeginDate(dataSDF.parse(evts[i][0]));
		 * sce.setEndDate(dataSDF.parse(evts[i][1])); } catch (ParseException e) {
		 * e.printStackTrace(); }
		 * 
		 * sce.setStyle(evts[i][2]); sce.setHeaderStyle(evts[i][3]);
		 * sce.setContent(evts[i][5]); calendarModel.add(sce); }
		 */

		List<Agendamiento> lAgendamientos = this.reg.getAllObjects(Agendamiento.class.getName());

		this.calendarModel = new SimpleCalendarModel();
		
		for (Agendamiento x : lAgendamientos) {
			AgendamientoCalendarItem sce = new AgendamientoCalendarItem();
			sce.setBeginDate(x.getFecha());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(x.getFecha());
			calendar.add(Calendar.MINUTE, x.getDuracion());
			sce.setAgendamientoId(x.getAgendamientoid());
			sce.setEndDate(calendar.getTime());
			sce.setStyle("background-color: "+x.getOdontologo().getColor()+"; color: #FFFFFF;");
			
			if (x.getEstado().getSigla().compareTo(ParamsLocal.SIGLA_AGENDAMIENTO_NO_CONFIRMATDO) == 0) {

				sce.setHeaderStyle(ParamsLocal.CALENDAR_STYLE_RED);

			}

			if (x.getEstado().getSigla().compareTo(ParamsLocal.SIGLA_AGENDAMIENTO_CONFIRMATDO) == 0) {

				sce.setHeaderStyle(ParamsLocal.CALENDAR_STYLE_ORANGE);

			}

			if (x.getEstado().getSigla().compareTo(ParamsLocal.SIGLA_AGENDAMIENTO_ATENDIDO) == 0) {

				sce.setHeaderStyle(ParamsLocal.CALENDAR_STYLE_GREEN);

			}

			sce.setContent(x.getPaciente().getFullNombre());
			this.calendarModel.add(sce);
		}
		
		BindUtils.postNotifyChange(null, null, this, "calendarModel");
		
		return true;
		
		//this.calendarModel = calendarModelAux;
	
	}

	private Window modal;

	@Listen(CalendarsEvent.ON_ITEM_CREATE + " = #calendars; " + CalendarsEvent.ON_ITEM_EDIT + "  = #calendars")
	public void agendamientoModal(CalendarsEvent event) {

		event.stopClearGhost();
		AgendamientoCalendarItem aci = (AgendamientoCalendarItem) event.getCalendarItem();

		if (aci != null) {

			this.agendamientoSelected = this.reg.getObjectById(Agendamiento.class.getName(), aci.getAgendamientoId());
			this.buscarEmpleado = this.agendamientoSelected.getOdontologo().getUsuario().getFullName();
			this.buscarPaciente = this.agendamientoSelected.getPaciente().getFullNombre();
			this.buscarEstado = this.agendamientoSelected.getEstado().getTipo();
			this.buscarMotivo = this.agendamientoSelected.getMotivo().getTipo();
			this.buscarConsultorio = this.agendamientoSelected.getConsultorio().getConsultorio();

		} else {

			this.agendamientoSelected = new Agendamiento();
			this.agendamientoSelected.setFecha(event.getBeginDate());
			long diff = event.getEndDate().getTime() - event.getBeginDate().getTime();
			this.agendamientoSelected.setDuracion((int) TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS));
			this.buscarEmpleado = "";
			this.buscarPaciente = "";
			this.buscarEstado = "";
			this.buscarMotivo = "";
			this.buscarConsultorio = "";

		}

		modal = (Window) Executions.createComponents("/sistema/zul/main/agendamientoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private String algo;

	@Command
	public void guardar() {

		this.save(this.agendamientoSelected);
		
		this.modal.detach();
		
		CompletableFuture.runAsync( () -> {
			
			BindUtils.postGlobalCommand("agendamientosGlobal", EventQueues.APPLICATION, "update", null);
			
		});
	}
	
	@GlobalCommand
	@NotifyChange({"calendarModel","algo"})
	public void update() {
		
		if (this.cargarAgendamientos()) {
			
			this.algo = this.getCurrentUser().getAccount();
		}
		
		
	}

	// Seccion buscarPaciente

	private List<Object[]> lPacientesbuscarOri;
	private List<Object[]> lPacientesBuscar;
	private String buscarPaciente = "";

	@Command
	@NotifyChange("lPacientesBuscar")
	public void filtrarPacienteBuscar() {

		this.lPacientesBuscar = this.filtrarListaObject(buscarPaciente, this.lPacientesbuscarOri);

	}

	@Command
	@NotifyChange("lPacientesBuscar")
	public void generarListaBuscarPaciente() {

		String sqlBuscarPaciente = this.um.getSql("pacientes.sql");

		this.lPacientesBuscar = this.reg.sqlNativo(sqlBuscarPaciente);
		this.lPacientesbuscarOri = this.lPacientesBuscar;
	}

	@Command
	@NotifyChange("buscarPaciente")
	public void onSelectPaciente(@BindingParam("id") long id) {

		Paciente paciente = this.reg.getObjectById(Paciente.class.getName(), id);
		this.agendamientoSelected.setPaciente(paciente);
		this.buscarPaciente = paciente.getFullNombre();
	}

	// fins buscadorPaciente
	
	// buscarConsultorio

	private List<Object[]> lConsultoriosbuscarOri;
	private List<Object[]> lConsultoriosBuscar;
	private String buscarConsultorio = "";

	@Command
	@NotifyChange("lConsultoriosBuscar")
	public void filtrarConsultorioBuscar() {

		this.lConsultoriosBuscar = this.filtrarListaObject(buscarConsultorio, this.lConsultoriosbuscarOri);

	}

	@Command
	@NotifyChange("lConsultoriosBuscar")
	public void generarListaBuscarConsultorio() {

		String sqlBuscarConsultorio = this.um.getSql("consultorios.sql");

		this.lConsultoriosBuscar = this.reg.sqlNativo(sqlBuscarConsultorio);
		this.lConsultoriosbuscarOri = this.lConsultoriosBuscar;
	}

	@Command
	@NotifyChange("buscarConsultorio")
	public void onSelectConsultorio(@BindingParam("id") long id) {

		Consultorio consultorio = this.reg.getObjectById(Consultorio.class.getName(), id);
		this.agendamientoSelected.setConsultorio(consultorio);
		this.buscarConsultorio = consultorio.getConsultorio();
		
	}

	// fin BuscarConsultorio

	// buscarEmpleado

	private List<Object[]> lEmpleadosbuscarOri;
	private List<Object[]> lEmpleadosBuscar;
	private String buscarEmpleado = "";

	@Command
	@NotifyChange("lEmpleadosBuscar")
	public void filtrarEmpleadoBuscar() {

		this.lEmpleadosBuscar = this.filtrarListaObject(buscarEmpleado, this.lEmpleadosbuscarOri);

	}

	@Command
	@NotifyChange("lEmpleadosBuscar")
	public void generarListaBuscarEmpleado() {

		String sqlBuscarEmpleado = this.um.getSql("empleados.sql");

		this.lEmpleadosBuscar = this.reg.sqlNativo(sqlBuscarEmpleado);
		this.lEmpleadosbuscarOri = this.lEmpleadosBuscar;
	}

	@Command
	@NotifyChange("buscarEmpleado")
	public void onSelectEmpleado(@BindingParam("id") long id) {

		Empleado empleado = this.reg.getObjectById(Empleado.class.getName(), id);
		this.agendamientoSelected.setOdontologo(empleado);
		this.buscarEmpleado = empleado.getUsuario().getFullName();
	}

	// fin BuscarEmpleado
	


	// buscarMotivo

	private List<Object[]> lMotivosbuscarOri;
	private List<Object[]> lMotivosBuscar;
	private String buscarMotivo = "";

	@Command
	@NotifyChange("lMotivosBuscar")
	public void filtrarMotivoBuscar() {

		this.lMotivosBuscar = this.filtrarListaObject(buscarMotivo, this.lMotivosbuscarOri);

	}

	@Command
	@NotifyChange("lMotivosBuscar")
	public void generarListaBuscarMotivo() {

		String sqlBuscarMotivo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_MOTIVO);

		this.lMotivosBuscar = this.reg.sqlNativo(sqlBuscarMotivo);
		this.lMotivosbuscarOri = this.lMotivosBuscar;
	}

	@Command
	@NotifyChange("buscarMotivo")
	public void onSelectMotivo(@BindingParam("id") long id) {

		Tipo motivo = this.reg.getObjectById(Tipo.class.getName(), id);
		this.agendamientoSelected.setMotivo(motivo);
		this.buscarMotivo = motivo.getTipo();
	}

	// fin BuscarMotivo

	// buscarEstado

	private List<Object[]> lEstadosbuscarOri;
	private List<Object[]> lEstadosBuscar;
	private String buscarEstado = "";

	@Command
	@NotifyChange("lEstadosBuscar")
	public void filtrarEstadoBuscar() {

		this.lEstadosBuscar = this.filtrarListaObject(buscarEstado, this.lEstadosbuscarOri);

	}

	@Command
	@NotifyChange("lEstadosBuscar")
	public void generarListaBuscarEstado() {

		String sqlBuscarEstado = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_AGENDAMIENTO);

		this.lEstadosBuscar = this.reg.sqlNativo(sqlBuscarEstado);
		this.lEstadosbuscarOri = this.lEstadosBuscar;
	}

	@Command
	@NotifyChange("buscarEstado")
	public void onSelectEstado(@BindingParam("id") long id) {

		Tipo estado = this.reg.getObjectById(Tipo.class.getName(), id);
		this.agendamientoSelected.setEstado(estado);
		this.buscarEstado = estado.getTipo();
	}

	// fin BuscarEstado

	public SimpleCalendarModel getCalendarModel() {
		return calendarModel;
	}

	public void setCalendarModel(SimpleCalendarModel calendarModel) {
		this.calendarModel = calendarModel;
	}

	public Agendamiento getAgendamientoSelected() {
		return agendamientoSelected;
	}

	public void setAgendamientoSelected(Agendamiento agendamientoSelected) {
		this.agendamientoSelected = agendamientoSelected;
	}

	public List<Object[]> getlPacientesBuscar() {
		return lPacientesBuscar;
	}

	public void setlPacientesBuscar(List<Object[]> lPacientesBuscar) {
		this.lPacientesBuscar = lPacientesBuscar;
	}

	public String getBuscarPaciente() {
		return buscarPaciente;
	}

	public void setBuscarPaciente(String buscarPaciente) {
		this.buscarPaciente = buscarPaciente;
	}

	public List<Object[]> getlEmpleadosBuscar() {
		return lEmpleadosBuscar;
	}

	public void setlEmpleadosBuscar(List<Object[]> lEmpleadosBuscar) {
		this.lEmpleadosBuscar = lEmpleadosBuscar;
	}

	public String getBuscarEmpleado() {
		return buscarEmpleado;
	}

	public void setBuscarEmpleado(String buscarEmpleado) {
		this.buscarEmpleado = buscarEmpleado;
	}

	public List<Object[]> getlMotivosBuscar() {
		return lMotivosBuscar;
	}

	public void setlMotivosBuscar(List<Object[]> lMotivosBuscar) {
		this.lMotivosBuscar = lMotivosBuscar;
	}

	public String getBuscarMotivo() {
		return buscarMotivo;
	}

	public void setBuscarMotivo(String buscarMotivo) {
		this.buscarMotivo = buscarMotivo;
	}

	public List<Object[]> getlEstadosBuscar() {
		return lEstadosBuscar;
	}

	public void setlEstadosBuscar(List<Object[]> lEstadosBuscar) {
		this.lEstadosBuscar = lEstadosBuscar;
	}

	public String getBuscarEstado() {
		return buscarEstado;
	}

	public void setBuscarEstado(String buscarEstado) {
		this.buscarEstado = buscarEstado;
	}

	public List<Object[]> getlConsultoriosBuscar() {
		return lConsultoriosBuscar;
	}

	public void setlConsultoriosBuscar(List<Object[]> lConsultoriosBuscar) {
		this.lConsultoriosBuscar = lConsultoriosBuscar;
	}

	public String getBuscarConsultorio() {
		return buscarConsultorio;
	}

	public void setBuscarConsultorio(String buscarConsultorio) {
		this.buscarConsultorio = buscarConsultorio;
	}

	public String getAlgo() {
		return algo;
	}

	public void setAlgo(String algo) {
		this.algo = algo;
	}

}
