package smartcampus.ifame.model;

public class PiattoKcal {

	private String piatto;
	private String kcal;

	public PiattoKcal() {
	}

	public PiattoKcal(String piatto, String kcal) {
		this.piatto = piatto;
		this.kcal = kcal;
	}

	public String getPiatto() {
		return piatto;
	}

	public void setPiatto(String piatto) {
		this.piatto = piatto;
	}

	public String getKcal() {
		return kcal;
	}

	public void setKcal(String kcal) {
		this.kcal = kcal;
	}

}
