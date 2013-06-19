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

}
