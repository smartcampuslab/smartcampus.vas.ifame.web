package smartcampus.ifame.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MenuDelGiorno {

	private List<PiattoKcal> piattiDelGiorno;
	private int day;

	public MenuDelGiorno() {
	}

	public List<PiattoKcal> getPiattiDelGiorno() {
		return piattiDelGiorno;
	}

	public void setPiattiDelGiorno(List<PiattoKcal> piattiDelGiorno) {
		this.piattiDelGiorno = piattiDelGiorno;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
