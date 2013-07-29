package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "Giudizio")
@NamedQueries({
		@NamedQuery(name = "Giudizio.getGiudizi", query = "from Giudizio where mensa_id = ?1 and piatto_id = ?2"),
		@NamedQuery(name = "Giudizio.getUserGiudizio", query = "from Giudizio where mensa_id = ?1 and piatto_id = ?2 and user_id = ?3") })
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

	@Column(name = "MENSA_ID")
	private Long mensa_id;

	@Column(name = "PIATTO_ID")
	private Long piatto_id;

	@Transient
	private List<Likes> likes;

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

	public Long getMensa_id() {
		return mensa_id;
	}

	public void setMensa_id(Long mensa_id) {
		this.mensa_id = mensa_id;
	}

	public Long getPiatto_id() {
		return piatto_id;
	}

	public void setPiatto_id(Long piatto_id) {
		this.piatto_id = piatto_id;
	}

	public List<Likes> getLikes() {
		return likes;
	}

	public void setLikes(List<Likes> likes) {
		this.likes = likes;
	}

}
