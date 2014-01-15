package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import eu.trentorise.smartcampus.mediation.model.CommentBaseEntity;

@Entity
@XmlRootElement(name = "Giudizio")
@NamedQueries({
		@NamedQuery(name = "Giudizio.getGiudiziApproved", query = "from Giudizio where mensa_id = ?1 and piatto_id = ?2 and approved=1"),
		@NamedQuery(name = "Giudizio.getUserGiudizioApproved", query = "from Giudizio where mensa_id = ?1 and piatto_id = ?2 and user_id = ?3 "),
		@NamedQuery(name = "Giudizio.getGiudiziAll", query = "from Giudizio where mensa_id = ?1 and piatto_id = ?2 and approved=1"),
		@NamedQuery(name = "Giudizio.getUserGiudizioAll", query = "from Giudizio where mensa_id = ?1 and piatto_id = ?2 and user_id = ?3 ")

})
public class Giudizio extends CommentBaseEntity {

	public Giudizio() {
		super();
	}

	@Column(name = "VOTO")
	private Float voto;

	@Column(name = "ULTIMO_AGGIORNAMENTO")
	private Date ultimo_aggiornamento;

	@Column(name = "USER_ID")
	private Long user_id;

	@Column(name = "USER_NAME")
	private String user_name;

	@Column(name = "MENSA_ID")
	private String mensa_id;

	@Column(name = "PIATTO_ID")
	private Long piatto_id;

	@Transient
	private List<Likes> likes;

	public Float getVoto() {
		return voto;
	}

	public void setVoto(Float voto) {
		this.voto = voto;
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

	public String getMensa_id() {
		return mensa_id;
	}

	public void setMensa_id(String mensa_id) {
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

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

}
