package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.List;

import eu.trentorise.smartcampus.vas.ifame.model.table.mapping.Piatto;

public class Alternative {
	private List<Piatto> alternative;

	public Alternative() {
	}

	public List<Piatto> getAlternative() {
		return alternative;
	}

	public void setAlternative(List<Piatto> alternative) {
		this.alternative = alternative;
	}
}
