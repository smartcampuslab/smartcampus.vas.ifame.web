package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "Transazione")
@NamedQuery(name = "Transazione.getUserTransactions", query = "from Transazione where user_id = ?1")
public class Transazione {

	@Id
	@GeneratedValue
	private Long transazione_id;

	@Column(name = "USER_ID")
	private Long user_id;

	@Column(name = "DATA")
	private Date data;

	@Column(name = "IMPORTO")
	private Float importo;

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Float getImporto() {
		return importo;
	}

	public void setImporto(Float importo) {
		this.importo = importo;
	}

	public Long getTransazione_id() {
		return transazione_id;
	}

	public void setTransazione_id(Long transazione_id) {
		this.transazione_id = transazione_id;
	}

}
