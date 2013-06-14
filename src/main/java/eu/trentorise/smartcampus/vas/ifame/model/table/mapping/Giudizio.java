package eu.trentorise.smartcampus.vas.ifame.model.table.mapping;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "Giudizio")
public class Giudizio {

	@Id
	@GeneratedValue
	private Long giudizio_id;

	@Column(name = "VOTO")
	private Float voto;

	@Column(name = "COMMENTO")
	private String commento;

	@Column(name = "ULTIMO_AGGIORNAMENTO")
	private Date ultimo_aggiornamento;

	@Column(name = "USER_ID")
	private Long user_id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Piatto_Mensa piatto_mensa;

	public Giudizio() {
		super();
	}

	public Long getGiudizio_id() {
		return giudizio_id;
	}

	public void setGiudizio_id(Long giudizio_id) {
		this.giudizio_id = giudizio_id;
	}

	public Float getVoto() {
		return voto;
	}

	public void setVoto(Float voto) {
		this.voto = voto;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public Date getUltimo_aggiornamento() {
		return ultimo_aggiornamento;
	}

	public void setUltimo_aggiornamento(Date ultimo_aggiornamento) {
		this.ultimo_aggiornamento = ultimo_aggiornamento;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Piatto_Mensa getPiatto_mensa() {
		return piatto_mensa;
	}

	public void setPiatto_mensa(Piatto_Mensa piatto_mensa) {
		this.piatto_mensa = piatto_mensa;
	}

}
