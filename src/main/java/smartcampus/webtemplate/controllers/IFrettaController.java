package smartcampus.webtemplate.controllers;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import smartcampus.ifame.model.ListaMense;
import smartcampus.ifame.model.Mensa;
import smartcampus.ifame.model.init.ListaMenseInit;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.CommunicatorConnectorException;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnector;
import eu.trentorise.smartcampus.dt.model.EventObject;
import eu.trentorise.smartcampus.dt.model.ObjectFilter;
import eu.trentorise.smartcampus.filestorage.client.Filestorage;
import eu.trentorise.smartcampus.filestorage.client.FilestorageException;
import eu.trentorise.smartcampus.filestorage.client.model.AppAccount;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.UserAccount;
import eu.trentorise.smartcampus.journeyplanner.JourneyPlannerConnector;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.socialservice.SocialService;
import eu.trentorise.smartcampus.socialservice.SocialServiceException;
import eu.trentorise.smartcampus.socialservice.model.Group;
import eu.trentorise.smartcampus.vas.ifame.repository.MensaRepository;

@Controller("IFrettaController")
public class IFrettaController {
	
	
	@Autowired
	MensaRepository mensaRepository;

	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
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
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				ListaMense lm = getMenseValues();
				
				
				
				return mensaRepository.findAll();
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	public  ListaMense getMenseValues() {

		final String url_povo_0_off = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo1.jpg";
		final String url_povo_1_off = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo2.jpg";
		final String url_tommaso_gar_off = "http://www.operauni.tn.it/upload/cms/456_x/gar-offline.jpg";
		final String url_zanella_off = "http://www.operauni.tn.it/upload/cms/456_x/mensa-zanella.jpg";
		final String url_mesiano_1_off = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-offline.jpg";
		final String url_mesiano_2_off = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-web-2.jpg";

		final String url_povo_0 = "http://www.operauni.tn.it/upload/Webcam/Povo01.jpg";
		final String url_povo_1 = "http://www.operauni.tn.it/upload/Webcam/Povo02.jpg";
		final String url_tommaso_gar = "http://www.operauni.tn.it/upload/Webcam/MensaUni.jpg";
		final String url_zanella = "http://www.operauni.tn.it/upload/Webcam/mensa_zanella.jpg";
		final String url_mesiano_1 = "http://www.operauni.tn.it/upload/Webcam/MensaMes01.jpg";
		final String url_mesiano_2 = "http://www.operauni.tn.it/upload/cms/456_x/MensaMes02.jpg";

		final String name_povo_0 = "Povo Mensa";
		final String name_povo_1 = "Povo Mensa Veloce";
		final String name_tommaso_gar = "Tommaso Gar";
		final String name_zanella = "Zanella";
		final String name_mesiano_1 = "Mesiano 1";
		final String name_mesiano_2 = "Mesiano 2";

		Long[] id = { (long) 656356, (long) 344647, (long) 455365,
				(long) 356356, (long) 212112, (long) 455478 };

		Mensa povo_0;
		Mensa povo_1;
		Mensa tommaso_gar;
		Mensa zanella;
		Mensa mesiano_1;
		Mensa mesiano_2;

		Calendar date = Calendar.getInstance();
		int hour = date.get(Calendar.HOUR_OF_DAY);
		if (hour >= 12 && hour < 14) {
			// mense online
			povo_0 = new Mensa(id[0], name_povo_0, url_povo_1);
			povo_1 = new Mensa(id[1], name_povo_1, url_povo_0);
			tommaso_gar = new Mensa(id[2], name_tommaso_gar, url_tommaso_gar);
			zanella = new Mensa(id[3], name_zanella, url_zanella);
			mesiano_1 = new Mensa(id[4], name_mesiano_1, url_mesiano_1);
			mesiano_2 = new Mensa(id[5], name_mesiano_2, url_mesiano_2);

		} else {
			// mense offline
			povo_0 = new Mensa(id[0], name_povo_0, url_povo_1_off);
			povo_1 = new Mensa(id[1], name_povo_1, url_povo_0_off);
			tommaso_gar = new Mensa(id[2], name_tommaso_gar,
					url_tommaso_gar_off);
			zanella = new Mensa(id[3], name_zanella, url_zanella_off);
			mesiano_1 = new Mensa(id[4], name_mesiano_1, url_mesiano_1_off);
			mesiano_2 = new Mensa(id[5], name_mesiano_2, url_mesiano_2_off);

		}

		ArrayList<Mensa> mense = new ArrayList<Mensa>();
		mensaRepository.save(povo_0);
		mensaRepository.save(povo_1);
		mensaRepository.save(tommaso_gar);
		mensaRepository.save(zanella);
		mensaRepository.save(mesiano_1);
		mensaRepository.save(mesiano_2);

		ListaMense lm = new ListaMense();
		lm.setLast_update_milis(System.currentTimeMillis());
		lm.setList(mense);

		return lm;
	}
}
