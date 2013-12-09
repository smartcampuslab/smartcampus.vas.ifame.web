package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.StudentInfoService;
import eu.trentorise.smartcampus.unidataservice.model.OperaPayment;
import eu.trentorise.smartcampus.vas.ifame.model.Saldo;

@Controller("ISoldiController")
public class ISoldiController {

	private static final Logger logger = Logger
			.getLogger(ISoldiController.class);

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

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${url.studente.service}")
	private String URLStudenteService;

	private StudentInfoService studentInfoService;

	@PostConstruct
	private void initStudentInfoService() {
		studentInfoService = new StudentInfoService(URLStudenteService);
	}

	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/isoldi/getsoldi")
	public @ResponseBody
	Saldo getSoldi(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		Saldo saldo = null;
		try {

			logger.info("/isoldi/getsoldi");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			if (profile != null) {
				/*
				 * ritorna un oggetto saldo inizializzato a random
				 */
				saldo = new Saldo();

				saldo.setCredit(studentInfoService.getOperaCard(token)
						.getRemaining());
				List<OperaPayment> listPayments = studentInfoService
						.getOperaCard(token).getPayments();
				if (listPayments.size() > 3)
					saldo.setPayments(studentInfoService.getOperaCard(token)
							.getPayments().subList(0, 3));
				else
					saldo.setPayments(studentInfoService.getOperaCard(token)
							.getPayments());

				return saldo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}