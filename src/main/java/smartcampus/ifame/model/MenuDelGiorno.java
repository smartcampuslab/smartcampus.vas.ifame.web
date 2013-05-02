package smartcampus.ifame.model;

import java.util.Date;
import java.util.List;

public class MenuDelGiorno {

	private List<Piatto> piattiDelGiorno;
	private Date menuDate;

	public MenuDelGiorno() {
	}

	public List<Piatto> getPiattiDelGiorno() {
		return piattiDelGiorno;
	}

	public void setPiattiDelGiorno(List<Piatto> piattiDelGiorno) {
		this.piattiDelGiorno = piattiDelGiorno;
	}

	public Date getMenuDate() {
		return menuDate;
	}

	public void setMenuDate(Date menuDate) {
		this.menuDate = menuDate;
	}

}
