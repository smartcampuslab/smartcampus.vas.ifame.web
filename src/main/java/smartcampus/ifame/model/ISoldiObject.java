package smartcampus.ifame.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSoldi")
public class ISoldiObject {

	private Long user_id;
	private Long card_id;
	private Float credit;
	private List<Transaction> transactions;

	public ISoldiObject() {
		Random rand = new Random();

		this.user_id = Math.abs(rand.nextLong());
		this.card_id = Math.abs(rand.nextLong());
		this.credit = rand.nextFloat() + rand.nextInt(10);

		this.transactions = new ArrayList<Transaction>();
		for (int i = 0; i < 5; i++) {
			Transaction t = new Transaction();
			t.setValue(rand.nextFloat() + rand.nextInt(10));
			// t.setDate(new Date(Math.abs(System.currentTimeMillis()
			// - rand.nextLong())));
			t.setDate(new Date(System.currentTimeMillis()));

			transactions.add(t);
		}

	};

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
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

	public Float getCredit() {
		return credit;
	}

	public void setCredit(Float credit) {
		this.credit = credit;
	}
}

class Transaction {

	private Date date;
	private Float value;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
}