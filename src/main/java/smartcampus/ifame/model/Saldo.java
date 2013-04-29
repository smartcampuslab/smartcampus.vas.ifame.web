package smartcampus.ifame.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSoldi")
public class Saldo {

	private Long user_id;
	private Long card_id;
	private String credit;
	private List<Transaction> transactions;

	public Saldo() {
		Random rand = new Random();

		this.user_id = Math.abs(rand.nextLong());
		this.card_id = Math.abs(rand.nextLong());

		DecimalFormat df = new DecimalFormat("###.##");
		double number = rand.nextDouble() + rand.nextInt(10);
		this.credit = df.format(number);

		this.transactions = new ArrayList<Transaction>();
		for (int i = 0; i < 5; i++) {

			double n = rand.nextDouble() + rand.nextInt(10);
			Transaction t = new Transaction();

			t.setValue(df.format(n));
			t.setTimemillis(System.currentTimeMillis());

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

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

}

class Transaction {

	private Long timemillis;
	private String value;

	public Long getTimemillis() {
		return timemillis;
	}

	public void setTimemillis(Long timemillis) {
		this.timemillis = timemillis;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}