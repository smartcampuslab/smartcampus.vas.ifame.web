package smartcampus.ifame.model;

import java.util.Date;
import java.util.List;

public class MenuDellaSettimana {

	private List<MenuDelGiorno> menuDellaSettimana;
	private Date start_day;
	private Date end_day;

	public MenuDellaSettimana() {
	}

	public List<MenuDelGiorno> getMenuDellaSettimana() {
		return menuDellaSettimana;
	}

	public void setMenuDellaSettimana(List<MenuDelGiorno> menuDellaSettimana) {
		this.menuDellaSettimana = menuDellaSettimana;
	}

	public Date getStart_day() {
		return start_day;
	}

	public void setStart_day(Date start_day) {
		this.start_day = start_day;
	}

	public Date getEnd_day() {
		return end_day;
	}

	public void setEnd_day(Date end_day) {
		this.end_day = end_day;
	}
}
