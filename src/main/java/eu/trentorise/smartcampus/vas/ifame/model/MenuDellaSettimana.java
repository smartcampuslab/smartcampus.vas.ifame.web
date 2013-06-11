package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.Date;
import java.util.List;

public class MenuDellaSettimana {

	private List<MenuDelGiorno> menuDelGiorno;

	private int start_day;
	private int end_day;

	public MenuDellaSettimana() {
	}

	public List<MenuDelGiorno> getMenuDelGiorno() {
		return menuDelGiorno;
	}

	public void setMenuDelGiorno(List<MenuDelGiorno> menuDelGiorno) {
		this.menuDelGiorno = menuDelGiorno;
	}

	public int getStart_day() {
		return start_day;
	}

	public void setStart_day(int start_day) {
		this.start_day = start_day;
	}

	public int getEnd_day() {
		return end_day;
	}

	public void setEnd_day(int end_day) {
		this.end_day = end_day;
	}

}
