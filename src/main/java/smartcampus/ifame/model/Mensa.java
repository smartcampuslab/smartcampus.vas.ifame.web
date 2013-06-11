package smartcampus.ifame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement(name = "Mensa")
public class Mensa {

	
	@Id
	@GeneratedValue
	private Long mensa_id;
	
	@Column(name="MENSA_NAME")
	private String mensa_name;
	
	@Column(name="MENSA_LINK")
	private String mensa_link;

	public Mensa() {

	}

	public Mensa(Long mensa_id, String mensa_name, String mensa_link) {
		super();
		this.mensa_id = mensa_id;
		this.mensa_name = mensa_name;
		this.mensa_link = mensa_link;
	}

	public Long getMensa_id() {
		return mensa_id;
	}

	public void setMensa_id(Long mensa_id) {
		this.mensa_id = mensa_id;
	}

	public String getMensa_name() {
		return mensa_name;
	}

	public void setMensa_name(String mensa_name) {
		this.mensa_name = mensa_name;
	}

	public String getMensa_link() {
		return mensa_link;
	}

	public void setMensa_link(String mensa_link) {
		this.mensa_link = mensa_link;
	}
}
