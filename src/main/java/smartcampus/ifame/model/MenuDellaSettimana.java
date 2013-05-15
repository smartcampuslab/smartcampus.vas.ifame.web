package smartcampus.ifame.model;

import java.util.Date;
import java.util.List;

public class MenuDellaSettimana {

	private List<MenuDelGiorno> menuDellaSettimana;

	private int start_day;

	public MenuDellaSettimana() {
	}

	public List<MenuDelGiorno> getMenuDellaSettimana() {
		return menuDellaSettimana;
	}

	public void setMenuDellaSettimana(List<MenuDelGiorno> menuDellaSettimana) {
		this.menuDellaSettimana = menuDellaSettimana;
	}

	public int getStart_day() {
		return start_day;
	}

	public void setStart_day(int start_day) {
		this.start_day = start_day;
	}
}
