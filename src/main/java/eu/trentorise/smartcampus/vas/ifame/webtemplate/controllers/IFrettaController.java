package eu.trentorise.smartcampus.vas.ifame.webtemplate.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
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
import eu.trentorise.smartcampus.vas.ifame.model.Mensa;
import eu.trentorise.smartcampus.vas.ifame.repository.MensaRepository;

@Controller("IFrettaController")
public class IFrettaController {

	@Autowired
	MensaRepository mensaRepository;

	private static final Logger logger = Logger
			.getLogger(IFrettaController.class);
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

	@RequestMapping(method = RequestMethod.GET, value = "/getmense")
	public @ResponseBody
	List<Mensa> getMense(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			logger.info("/getmense");
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				return mensaRepository.findAll();
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@PostConstruct
	public void initMense() {

		final String url_povo_0_offline = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo1.jpg";
		final String url_povo_1_offline = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo2.jpg";
		final String url_tommaso_gar_offline = "http://www.operauni.tn.it/upload/cms/456_x/gar-offline.jpg";
		final String url_zanella_offline = "http://www.operauni.tn.it/upload/cms/456_x/mensa-zanella.jpg";
		final String url_mesiano_1_offline = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-offline.jpg";
		final String url_mesiano_2_offline = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-web-2.jpg";

		final String url_povo_0_online = "http://www.operauni.tn.it/upload/Webcam/Povo01.jpg";
		final String url_povo_1_online = "http://www.operauni.tn.it/upload/Webcam/Povo02.jpg";
		final String url_tommaso_gar_online = "http://www.operauni.tn.it/upload/Webcam/MensaUni.jpg";
		final String url_zanella_online = "http://www.operauni.tn.it/upload/Webcam/mensa_zanella.jpg";
		final String url_mesiano_1_online = "http://www.operauni.tn.it/upload/Webcam/MensaMes01.jpg";
		final String url_mesiano_2_online = "http://www.operauni.tn.it/upload/MensaMes02.jpg";

		final String name_povo_0 = "Povo Mensa";
		final String name_povo_1 = "Povo Mensa Veloce";
		final String name_tommaso_gar = "Tommaso Gar";
		final String name_zanella = "Zanella";
		final String name_mesiano_1 = "Mesiano 1";
		final String name_mesiano_2 = "Mesiano 2";

		Mensa povo_0 = new Mensa(name_povo_0, url_povo_0_online,
				url_povo_0_offline);
		Mensa povo_1 = new Mensa(name_povo_1, url_povo_1_online,
				url_povo_1_offline);
		Mensa tommaso_gar = new Mensa(name_tommaso_gar, url_tommaso_gar_online,
				url_tommaso_gar_offline);
		Mensa zanella = new Mensa(name_zanella, url_zanella_online,
				url_zanella_offline);
		Mensa mesiano_1 = new Mensa(name_mesiano_1, url_mesiano_1_online,
				url_mesiano_1_offline);
		Mensa mesiano_2 = new Mensa(name_mesiano_2, url_mesiano_2_online,
				url_mesiano_2_offline);

		mensaRepository.save(povo_0);
		mensaRepository.save(povo_1);
		mensaRepository.save(tommaso_gar);
		mensaRepository.save(zanella);
		mensaRepository.save(mesiano_1);
		mensaRepository.save(mesiano_2);

	}
}
