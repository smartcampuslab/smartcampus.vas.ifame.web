package eu.trentorise.smartcampus.vas.ifame.model.table.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "Piatto")
public class Piatto {

	@Id
	@GeneratedValue
	private Long piatto_id;

	@Column(name = "PIATTO_NOME")
	private String piatto_nome;

	@Column(name = "PIATTO_KCAL")
	private String piatto_kcal;

	public Piatto() {
		super();
	}

	public Piatto(String piatto_nome, String piatto_kcal) {
		super();
		this.piatto_nome = piatto_nome;
		this.piatto_kcal = piatto_kcal;
	}

	public Long getPiatto_id() {
		return piatto_id;
	}

	public void setPiatto_id(Long piatto_id) {
		this.piatto_id = piatto_id;
	}

	public String getPiatto_nome() {
		return piatto_nome;
	}

	public void setPiatto_nome(String piatto_nome) {
		this.piatto_nome = piatto_nome;
	}

	public String getPiatto_kcal() {
		return piatto_kcal;
	}

	public void setPiatto_kcal(String piatto_kcal) {
		this.piatto_kcal = piatto_kcal;
	}

}
