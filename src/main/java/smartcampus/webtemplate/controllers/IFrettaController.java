package smartcampus.webtemplate.controllers;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;

import java.io.IOException;
import java.util.ArrayList;
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

@Controller("IFrettaController")
public class IFrettaController {

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
	ListaMense getMense(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				final String url_povo_0 = "http://www.operauni.tn.it/cms-01.00/articolo.asp?IDcms=13737&s=279&l=IT";
				final String url_povo_1 = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo2.jpg";
				final String url_tommaso_gar = "http://www.operauni.tn.it/upload/cms/456_x/gar-offline.jpg";
				final String url_zanella = "http://www.operauni.tn.it/upload/cms/456_x/mensa-zanella.jpg";
				final String url_mesiano = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-offline.jpg";

				final String name_povo_0 = "POVO_0";
				final String name_povo_1 = "POVO_1";
				final String name_tommaso_gar = "TOMMASO_GAR";
				final String name_zanella = "ZANELLA";
				final String name_mesiano = "MESIANO";

				Long[] id = { (long) 656356, (long) 344647, (long) 455365,
						(long) 356356, (long) 212112 };

				Mensa povo_0 = new Mensa(id[0], name_povo_0, url_povo_0);
				Mensa povo_1 = new Mensa(id[1], name_povo_1, url_povo_1);
				Mensa tommaso_gar = new Mensa(id[2], name_tommaso_gar,
						url_tommaso_gar);
				Mensa zanella = new Mensa(id[3], name_zanella, url_zanella);
				Mensa mesiano = new Mensa(id[4], name_mesiano, url_mesiano);

				ArrayList<Mensa> mense = new ArrayList<Mensa>();
				mense.add(povo_0);
				mense.add(povo_1);
				mense.add(tommaso_gar);
				mense.add(zanella);
				mense.add(mesiano);

				ListaMense lm = new ListaMense();
				lm.setLast_update_milis(System.currentTimeMillis());
				lm.setList(mense);

				return lm;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
