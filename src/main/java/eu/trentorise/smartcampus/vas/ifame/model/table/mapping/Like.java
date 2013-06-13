package eu.trentorise.smartcampus.vas.ifame.model.table.mapping;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "Like")
public class Like {

	@Id
	@GeneratedValue
	private Long like_id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Giudizio giudizio;
	
	@Column(name = "USER_ID")
	private Long user_id;
	
	@Column(name = "LIKE")
	private Boolean like;

	
	
}
