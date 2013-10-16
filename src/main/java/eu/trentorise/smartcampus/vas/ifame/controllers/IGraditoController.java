package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.mediation.engine.MediationParserImpl;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.vas.ifame.model.Giudizio;
import eu.trentorise.smartcampus.vas.ifame.model.GiudizioDataToPost;
import eu.trentorise.smartcampus.vas.ifame.model.Likes;
import eu.trentorise.smartcampus.vas.ifame.model.Piatto;
import eu.trentorise.smartcampus.vas.ifame.repository.GiudizioRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.LikesRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.MensaRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.PiattoRepository;

@Controller("GiudizioController")
public class IGraditoController {

	private static final Logger logger = Logger
			.getLogger(IGraditoController.class);

	@Autowired
	PiattoRepository piattoRepository;

	@Autowired
	MensaRepository mensaRepository;

	@Autowired
	GiudizioRepository giudizioNewRepository;

	@Autowired
	LikesRepository likeRepository;
	
	@Autowired
	private MediationParserImpl mediationParserImpl;

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

	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	/*
	 * 
	 * 
	 * RITORNA LA LISTA DI TUTTI PIATTI PER IGRADITO
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getpiatti")
	public @ResponseBody
	List<Piatto> getAllPiatti(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			logger.info("/getpiatti");
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {

				return piattoRepository.findAll();
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * 
	 * 
	 * GET GIUDIZI /mensa/{id}/piatto/{id}/giudizio
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/mensa/{mensa_id}/piatto/{piatto_id}/giudizio")
	public @ResponseBody
	List<Giudizio> getMensaPiatti(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("mensa_id") Long mensa_id,
			@PathVariable("piatto_id") Long piatto_id) throws IOException {
		try {
			logger.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {

				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)) {

					List<Giudizio> giudizi_list = giudizioNewRepository
							.getGiudiziApproved(mensa_id, piatto_id);

					for (Giudizio giudizio : giudizi_list) {
						giudizio.setLikes(likeRepository
								.getGiudizioLikes(giudizio.getGiudizio_id()));
					}

					return giudizi_list;

				} else {
					/*
					 * not found se non trova mensa e/o piatto
					 */
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		/*
		 * BAD REQUEST SE HO ERRORI NEI CONTROLLI
		 */
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return null;
	}

	/*
	 * 
	 * 
	 * GET user giudizio /mensa/{id}/piatto/{id}/giudizio
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/mensa/{mensa_id}/piatto/{piatto_id}/user/{user_id}/giudizio")
	public @ResponseBody
	Giudizio getUserGiudizio(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("mensa_id") Long mensa_id,
			@PathVariable("piatto_id") Long piatto_id,
			@PathVariable("user_id") Long user_id) throws IOException {
		try {
			logger.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/user_id/" + user_id + "/giudizio");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {

				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)) {

					return giudizioNewRepository.getUserGiudizioApproved(mensa_id,
							piatto_id, user_id);

				} else {
					/*
					 * not found se non trova mensa e/o piatto
					 */
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		/*
		 * BAD REQUEST SE HO ERRORI NEI CONTROLLI
		 */
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return null;
	}

	/*
	 * 
	 * 
	 * ADD or UPDATE GIUDIZIO /mensa/{id}/piatto/{id}/giudizio/add
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/mensa/{mensa_id}/piatto/{piatto_id}/giudizio/add")
	public @ResponseBody
	List<Giudizio> aggiungiGiudizio(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("mensa_id") Long mensa_id,
			@PathVariable("piatto_id") Long piatto_id,
			@RequestBody GiudizioDataToPost data) throws IOException {
		try {
			logger.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio/add");
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {

				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)) {
					/*
					 * controllo se ha gi� inserito un giudizio
					 */
					Giudizio giudizio_old = giudizioNewRepository
							.getUserGiudizioApproved(mensa_id, piatto_id, data.userId);
					

					
					

					if (giudizio_old != null) {
						/*
						 * lo aggiorno
						 */
						giudizio_old.setUltimo_aggiornamento(new Date(System
								.currentTimeMillis()));

						giudizio_old.setVoto(data.voto);
						giudizio_old.setCommento(data.commento);
						giudizio_old.setApproved(mediationParserImpl.validateComment(giudizio_old.getCommento(),giudizio_old.getGiudizio_id().intValue(),userId,token));
						
						giudizio_old = giudizioNewRepository.save(giudizio_old);

						List<Likes> like_list = likeRepository
								.getGiudizioLikes(giudizio_old.getGiudizio_id());

						likeRepository.delete(like_list);

					} else {
						/*
						 * lo aggiungo
						 */
						Giudizio giudizio = new Giudizio();

						giudizio.setUltimo_aggiornamento(new Date(System
								.currentTimeMillis()));
						giudizio.setCommento(data.commento);
						giudizio.setUser_id(data.userId);
						giudizio.setVoto(data.voto);
						giudizio.setMensa_id(mensa_id);
						giudizio.setPiatto_id(piatto_id);
						giudizio.setApproved(true); // if giudizio is approved false,is not possible to update
						
						giudizio=giudizioNewRepository.save(giudizio);
						giudizio.setApproved(mediationParserImpl.validateComment(giudizio.getCommento(),giudizio.getGiudizio_id().intValue(),userId,token));
						giudizioNewRepository.save(giudizio);
					}

					/*
					 * ritorno la lista di giudizi con anche i likes associati
					 */
					List<Giudizio> giudizi_list = giudizioNewRepository
							.getGiudiziApproved(mensa_id, piatto_id);

					for (Giudizio giudizio : giudizi_list) {
						giudizio.setLikes(likeRepository
								.getGiudizioLikes(giudizio.getGiudizio_id()));
					}

					return giudizi_list;

				} else {
					/*
					 * SE NON TROVO PIATTO e/o MENSA CORRISPONDENTI AI VALORI
					 * PASSATI NELL URL
					 */
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		/*
		 * BAD REQUEST SE HO ERRORI NEI CONTROLLI
		 */
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return null;
	}

	/*
	 * 
	 * 
	 * DELETE GIUDIZIO
	 * /mensa/{mensa_id}/piatto/{piatto_id}/giudizio/{giudizio_id}/delete
	 */

	@RequestMapping(method = RequestMethod.DELETE, value = "/mensa/{mensa_id}/piatto/{piatto_id}/giudizio/{giudizio_id}/delete")
	public @ResponseBody
	List<Giudizio> eliminaGiudizio(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("mensa_id") Long mensa_id,
			@PathVariable("piatto_id") Long piatto_id,
			@PathVariable("giudizio_id") Long giudizio_id,
			@RequestBody GiudizioDataToPost data) throws IOException {
		try {
			logger.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio/" + giudizio_id + "/delete");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {
				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)
						&& giudizioNewRepository.exists(giudizio_id)) {

					Giudizio giudizio_old = giudizioNewRepository
							.getUserGiudizioApproved(mensa_id, piatto_id, data.userId);
					if (giudizio_old != null) {
						// se 3esiste elimino i likes
						List<Likes> like_list = likeRepository
								.getGiudizioLikes(giudizio_old.getGiudizio_id());

						likeRepository.delete(like_list);

						// e poi elimino il giudizio
						giudizioNewRepository.delete(giudizio_old);
					}

					/*
					 * RITORNO LA LISTA DI GIUDIZI
					 */

					List<Giudizio> giudizi_list = giudizioNewRepository
							.getGiudiziApproved(mensa_id, piatto_id);

					for (Giudizio giudizio : giudizi_list) {
						giudizio.setLikes(likeRepository
								.getGiudizioLikes(giudizio.getGiudizio_id()));
					}

					return giudizi_list;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		/*
		 * BAD REQUEST SE HO ERRORI NEI CONTROLLI
		 */
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return null;
	}
}
