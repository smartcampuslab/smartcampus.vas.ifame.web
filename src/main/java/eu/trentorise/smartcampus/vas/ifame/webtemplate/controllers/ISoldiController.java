package eu.trentorise.smartcampus.vas.ifame.webtemplate.controllers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.vas.ifame.model.Saldo;
import eu.trentorise.smartcampus.vas.ifame.model.Transaction;

@Controller("ISoldiController")
public class ISoldiController {


	private static final Logger logger = Logger
			.getLogger(ISoldiController.class);
	@Autowired
	private AcService acService;

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

	@RequestMapping(method = RequestMethod.GET, value = "/getsoldi")
	public @ResponseBody
	Saldo getMense(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		Saldo saldo = null;
		try {
			
			logger.info("/getsoldi");
			
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