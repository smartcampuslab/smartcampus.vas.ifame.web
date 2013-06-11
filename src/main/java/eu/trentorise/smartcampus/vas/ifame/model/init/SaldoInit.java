package eu.trentorise.smartcampus.vas.ifame.model.init;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import eu.trentorise.smartcampus.vas.ifame.model.Saldo;
import eu.trentorise.smartcampus.vas.ifame.model.Transaction;


public class SaldoInit {

	public static Saldo createFakeSaldo() {

		Saldo s = new Saldo();

		Random rand = new Random();

		DecimalFormat df = new DecimalFormat("###.##");
		double number = rand.nextDouble() + rand.nextInt(10);

		s.setCredit(df.format(number));
		s.setUser_id(Math.abs(rand.nextLong()));
		s.setCard_id(Math.abs(rand.nextLong()));

		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		for (int i = 0; i < 5; i++) {

			double n = rand.nextDouble() + rand.nextInt(10);
			Transaction t = new Transaction();

			t.setValue(df.format(n));
			t.setTimemillis(System.currentTimeMillis());

			transactions.add(t);
		}
		s.setTransactions(transactions);

		return s;
	}
}
