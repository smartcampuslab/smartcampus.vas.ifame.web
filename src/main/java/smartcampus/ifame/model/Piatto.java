package smartcampus.ifame.model;

import java.util.Arrays;

public class Piatto {

	private Integer piatto_id;
	private String piatto_name;
	private PiattoType piatto_type;
	private String piatto_description;
	private String[] piatto_ingredients;

	public Piatto() {
	}

	public PiattoType getPiatto_type() {
		return piatto_type;
	}

	public void setPiatto_type(PiattoType piatto_type) {
		this.piatto_type = piatto_type;
	}

	public Integer getPiatto_id() {
		return piatto_id;
	}

	public void setPiatto_id(Integer piatto_id) {
		this.piatto_id = piatto_id;
	}

	public String getPiatto_name() {
		return piatto_name;
	}

	public void setPiatto_name(String piatto_name) {
		this.piatto_name = piatto_name;
	}

	public String getPiatto_description() {
		return piatto_description;
	}

	public void setPiatto_description(String piatto_description) {
		this.piatto_description = piatto_description;
	}

	public String[] getPiatto_ingredients() {
		return piatto_ingredients;
	}

	public void setPiatto_ingredients(String[] piatto_ingredients) {
		this.piatto_ingredients = piatto_ingredients;
	}

}
