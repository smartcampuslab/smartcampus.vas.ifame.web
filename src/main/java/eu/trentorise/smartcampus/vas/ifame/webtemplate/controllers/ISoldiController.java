package eu.trentorise.smartcampus.vas.ifame.webtemplate.controllers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.vas.ifame.model.Saldo;
import eu.trentorise.smartcampus.vas.ifame.model.Transaction;
import eu.trentorise.smartcampus.vas.ifame.model.Transazione;
import eu.trentorise.smartcampus.vas.ifame.repository.TransazioniRepository;

@Controller("ISoldiController")
public class ISoldiController {

	private static final Logger logger = Logger
			.getLogger(ISoldiController.class);
	@Autowired
	private AcService acService;

	@Autowired
	TransazioniRepository transazioniRepository;

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${services.server}")
	private String serverAddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@RequestMapping(method = RequestMethod.GET, value = "/isoldi/getsoldi")
	public @ResponseBody
	Saldo getSoldi(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		Saldo saldo = null;
		try {

			logger.info("/isoldi/getsoldi");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {
				saldo = createFakeSaldo();
				return saldo;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/isoldi/user/{user_id}/transazioni")
	public @ResponseBody
	List<Transazione> getTransazioni(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("user_id") Long user_id) throws IOException {
		Saldo saldo = null;
		try {
			logger.info("/isoldi/user/{user_id}/transazioni");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				return transazioniRepository.getUserTransactions(user_id);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

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

	@PostConstruct
	private void initTransactions() {

		Random rand = new Random();

		Float[] importi = { (float) 2.90, (float) 4.20, (float) 4.70 };
		Integer[] user_id = { 216, 217, 218, 219, 225 };

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				Transazione t = new Transazione();
				t.setData(new Date(System.currentTimeMillis()));

				t.setUser_id((long) user_id[i]);

				t.setImporto(importi[j]);

				transazioniRepository.save(t);
			}
		}

	}
}