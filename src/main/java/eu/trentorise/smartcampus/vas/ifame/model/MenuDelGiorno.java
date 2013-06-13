package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.trentorise.smartcampus.vas.ifame.model.table.mapping.Piatto;

public class MenuDelGiorno {

	private List<Piatto> piattiDelGiorno;
	private int day;

	public MenuDelGiorno() {
	}

	public List<Piatto> getPiattiDelGiorno() {
		return piattiDelGiorno;
	}

	public void setPiattiDelGiorno(List<Piatto> piattiDelGiorno) {
		this.piattiDelGiorno = piattiDelGiorno;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
