package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.List;

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
