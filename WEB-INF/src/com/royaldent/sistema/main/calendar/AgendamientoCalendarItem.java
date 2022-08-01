package com.royaldent.sistema.main.calendar;

import org.zkoss.calendar.impl.SimpleCalendarItem;

public class AgendamientoCalendarItem extends SimpleCalendarItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8392532095107910281L;
	
	private Long agendamientoId;

	public Long getAgendamientoId() {
		return agendamientoId;
	}

	public void setAgendamientoId(Long agendamientoId) {
		this.agendamientoId = agendamientoId;
	}

}
