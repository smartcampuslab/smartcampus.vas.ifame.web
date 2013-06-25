package eu.trentorise.smartcampus.vas.ifame.webtemplate.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.vas.ifame.model.GiudizioDataToPost;
import eu.trentorise.smartcampus.vas.ifame.model.GiudizioNew;
import eu.trentorise.smartcampus.vas.ifame.model.Mensa;
import eu.trentorise.smartcampus.vas.ifame.repository.GiudizioNewRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.LikesRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.MensaRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.PiattoRepository;

@Controller("GiudizioController")
public class GiudizioController {

	private static final Logger logger = Logger
			.getLogger(GiudizioController.class);
	@Autowired
	private AcService acService;

	@Autowired
	PiattoRepository piattoRepository;

	@Autowired
	MensaRepository mensaRepository;

	@Autowired
	GiudizioNewRepository giudizioNewRepository;

	@Autowired
	LikesRepository likeRepository;

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

	/*
	 * 
	 * 
	 * 
	 * PATH /mensa/{id}/piatto/{id}
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/mensa/{mensa_id}/piatto/{piatto_id}")
	public @ResponseBody
	List<GiudizioNew> getMensaPiatti(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("mensa_id") Long mensa_id,
			@PathVariable("piatto_id") Long piatto_id) throws IOException {
		try {
			logger.info("/mensa/" + mensa_id + "/piatto/" + piatto_id);

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)) {

					return giudizioNewRepository
							.getGiudizi(mensa_id, piatto_id);

				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * 
	 * 
	 * 
	 * PATH /mensa/{id}/piatto/{id}/giudizio/add
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/mensa/{mensa_id}/piatto/{piatto_id}/giudizio/add")
	public @ResponseBody
	List<GiudizioNew> aggiungiGiudizio(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("mensa_id") Long mensa_id,
			@PathVariable("piatto_id") Long piatto_id,
			@RequestBody GiudizioDataToPost data) throws IOException {
		try {
			logger.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio/add");
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);

			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)) {

					GiudizioNew giudizio_old = giudizioNewRepository
							.getUserGiudizio(mensa_id, piatto_id, data.userId);

					if (giudizio_old != null) {
						giudizio_old.setUltimo_aggiornamento(new Date(System
								.currentTimeMillis()));

						giudizio_old.setVoto(data.voto);
						giudizio_old.setCommento(data.commento);

						giudizioNewRepository.save(giudizio_old);

					} else {
						GiudizioNew giudizio = new GiudizioNew();

						giudizio.setUltimo_aggiornamento(new Date(System
								.currentTimeMillis()));
						giudizio.setCommento(data.commento);
						giudizio.setUser_id(data.userId);
						giudizio.setVoto(data.voto);
						giudizio.setMensa_id(mensa_id);
						giudizio.setPiatto_id(piatto_id);

						giudizioNewRepository.save(giudizio);
					}

					return giudizioNewRepository
							.getGiudizi(mensa_id, piatto_id);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
