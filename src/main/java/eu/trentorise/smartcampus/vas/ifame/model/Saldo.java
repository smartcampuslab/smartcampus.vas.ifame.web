package eu.trentorise.smartcampus.vas.ifame.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import eu.trentorise.smartcampus.unidataservice.model.OperaPayment;

@XmlRootElement(name = "Saldo")
public class Saldo {

	private Long user_id;
	private Long card_id;
	

	private String credit;
	private List<OperaPayment> payments;

	public Saldo() {
	}
	

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getCard_id() {
		return card_id;
	}

	public void setCard_id(Long card_id) {
		this.card_id = card_id;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}


	public List<OperaPayment> getPayments() {
		return payments;
	}


	public void setPayments(List<OperaPayment> payments) {
		this.payments = payments;
	}


}