package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import eu.trentorise.smartcampus.unidataservice.model.CanteenOpening;
import eu.trentorise.smartcampus.unidataservice.model.CanteenOpeningTimes;

@Entity
@XmlRootElement(name = "Mensa")
public class Mensa {

	@Id
	private String mensa_id;

	@Column(name = "MENSA_NOME")
	private String mensa_nome;

	@Column(name = "MENSA_LINK_ONLINE")
	private String mensa_link_online;

	@Column(name = "MENSA_LINK_OFFLINE")
	private String mensa_link_offline;
	
	@Transient
	private List<CanteenOpeningTimes> times;

	public Mensa(CanteenOpening canteenOpening) {
		super();
		this.mensa_id=canteenOpening.getId();
		this.mensa_nome=canteenOpening.getCanteen();
	}

	public Mensa(String mensa_id,String mensa_nome, String mensa_link_online,
			String mensa_link_offline) {
		super();
		this.mensa_id=mensa_id;
		this.mensa_nome = mensa_nome;
		this.mensa_link_online = mensa_link_online;
		this.mensa_link_offline = mensa_link_offline;
	}

	public String getMensa_id() {
		return mensa_id;
	}

	public void setMensa_id(String mensa_id) {
		this.mensa_id = mensa_id;
	}

	public String getMensa_nome() {
		return mensa_nome;
	}

	public void setMensa_nome(String mensa_nome) {
		this.mensa_nome = mensa_nome;
	}

	public String getMensa_link_online() {
		return mensa_link_online;
	}

	public void setMensa_link_online(String mensa_link_online) {
		this.mensa_link_online = mensa_link_online;
	}

	public String getMensa_link_offline() {
		return mensa_link_offline;
	}

	public void setMensa_link_offline(String mensa_link_offline) {
		this.mensa_link_offline = mensa_link_offline;
	}

	public List<CanteenOpeningTimes> getTimes() {
		return times;
	}

	public void setTimes(List<CanteenOpeningTimes> list) {
		this.times = list;
	}

}
