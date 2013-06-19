package eu.trentorise.smartcampus.vas.ifame.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;

import eu.trentorise.smartcampus.vas.ifame.model.Giudizio;

@Entity
@XmlRootElement(name = "Likes")
public class Likes {

	@Id
	@GeneratedValue
	private Long like_id;

	@ManyToOne
	// (fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "GIUDIZIO_ID")
	private Giudizio giudizio;

	@Column(name = "USER_ID")
	private Long user_id;

	@Column(name = "IS_LIKE")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean is_like;

	public Likes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getLike_id() {
		return like_id;
	}

	public void setLike_id(Long like_id) {
		this.like_id = like_id;
	}

	public Giudizio getGiudizio() {
		return giudizio;
	}

	public void setGiudizio(Giudizio giudizio) {
		this.giudizio = giudizio;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Boolean getIs_like() {
		return is_like;
	}

	public void setIs_like(Boolean is_like) {
		this.is_like = is_like;
	}

}
