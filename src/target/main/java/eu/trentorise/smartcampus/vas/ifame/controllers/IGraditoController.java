package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.mediation.engine.MediationParserImpl;
import eu.trentorise.smartcampus.mediation.model.CommentBaseEntity;
import eu.trentorise.smartcampus.moderatorservice.model.State;
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
import eu.trentorise.smartcampus.vas.ifame.utils.TokenUtils;

@Controller("GiudizioController")
public class IGraditoController {

	private static final Logger log = Logger
			.getLogger(IGraditoController.class);

	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private PiattoRepository piattoRepository;

	@Autowired
	private MensaRepository mensaRepository;

	@Autowired
	private GiudizioRepository giudizioNewRepository;

	@Autowired
	private LikesRepository likeRepository;

	@Autowired
	private MediationParserImpl mediationParserImpl;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@PostConstruct
	public void initKeyWords() throws Exception {
 		mediationParserImpl.initKeywords(tokenUtils.getClientToken());
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
			log.debug("/getpiatti");
			String token = tokenUtils.getUserToken();
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			// Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {

				return piattoRepository.findAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			log.debug("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio");

			String token = tokenUtils.getUserToken();
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			// Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {

				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)) {

					List<Giudizio> giudizi_list = giudizioNewRepository
							.getGiudiziApproved(mensa_id, piatto_id);

					for (Giudizio giudizio : giudizi_list) {
						giudizio.setLikes(likeRepository
								.getGiudizioLikes((long) giudizio.getId()));
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
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
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
			log.debug("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/user_id/" + user_id + "/giudizio");

			String token = tokenUtils.getUserToken();
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			// Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {

				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)) {

					return giudizioNewRepository.getUserGiudizio(
							mensa_id, piatto_id, user_id);

				} else {
					/*
					 * not found se non trova mensa e/o piatto
					 */
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
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

			log.debug("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio/add");

			String token = tokenUtils.getUserToken();
			String clientToken = tokenUtils.getClientToken();
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
							.getUserGiudizio(mensa_id, piatto_id,
									data.userId);

					if (giudizio_old != null) {

						log.debug("Aggiorno il giudizio");
						/*
						 * lo aggiorno
						 */
						giudizio_old.setUltimo_aggiornamento(new Date(System.currentTimeMillis()));

						giudizio_old.setVoto(data.voto);
						giudizio_old.setTesto(data.commento);
						boolean keyWordApproved = mediationParserImpl
								.localValidationComment(
										giudizio_old.getTesto(), ""+giudizio_old
												.getId(), userId,
												clientToken);
						if (!keyWordApproved) {
							giudizio_old.setApproved(State.NOT_APPROVED);
						} else {
							giudizio_old.setApproved(State.WAITING);
							mediationParserImpl
							.remoteValidationComment(
									giudizio_old.getTesto(),
									""+giudizio_old.getId(),
									userId, clientToken);						
							giudizio_old.setUser_name(profile.getName() + "."
									+ profile.getSurname());

							giudizio_old = giudizioNewRepository.save(giudizio_old);
							List<Likes> like_list = likeRepository.getGiudizioLikes((long) giudizio_old.getId());
							likeRepository.delete(like_list);
						}
					} else {
						/*
						 * lo aggiungo
						 */
						Giudizio giudizio = new Giudizio();

						giudizio.setUltimo_aggiornamento(new Date(System
								.currentTimeMillis()));
						giudizio.setTesto(data.commento);
						giudizio.setUser_id(data.userId);
						giudizio.setVoto(data.voto);
						giudizio.setMensa_id(mensa_id);
						giudizio.setPiatto_id(piatto_id);
						giudizio.setUser_name(profile.getName() + "." + profile.getSurname());
						giudizio = giudizioNewRepository.save(giudizio);

						boolean keyWordApproved = mediationParserImpl
								.localValidationComment(giudizio.getTesto(),
										""+giudizio.getId(), userId,
										clientToken);
						if (keyWordApproved) {
							giudizio.setApproved(State.WAITING);
							mediationParserImpl
							.remoteValidationComment(giudizio
									.getTesto(), ""+giudizio.getId(), userId, clientToken);
							giudizio = giudizioNewRepository.save(giudizio);
						} else {
							giudizioNewRepository.delete(giudizio);
						}
					}

					updateApprovalStatus();
					
					/*
					 * ritorno la lista di giudizi con anche i likes associati
					 */
					List<Giudizio> giudizi_list = giudizioNewRepository
							.getGiudiziApproved(mensa_id, piatto_id);

					for (Giudizio giudizio : giudizi_list) {
						giudizio.setLikes(likeRepository
								.getGiudizioLikes(giudizio.getId()));
					}

					return giudizi_list;

				} else {
					/*
					 * SE NON TROVO PIATTO e/o MENSA CORRISPONDENTI AI VALORI
					 * PASSATI NELL URL
					 */
					log.debug("Mensa or piatto not found");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;

	}

	public List<CommentBaseEntity> getCommentBase(List<Giudizio> giudizi) {

		ArrayList<CommentBaseEntity> commentList = new ArrayList<CommentBaseEntity>(
				giudizi.size());

		commentList.addAll(giudizi);

		return commentList;
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
			log.debug("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio/" + giudizio_id + "/delete");

			String token = tokenUtils.getUserToken();
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			// Long userId = Long.valueOf(profile.getUserId());
			if (profile != null) {
				if (mensaRepository.exists(mensa_id)
						&& piattoRepository.exists(piatto_id)
						&& giudizioNewRepository.exists(giudizio_id)) {

					Giudizio giudizio_old = giudizioNewRepository
							.getUserGiudizio(mensa_id, piatto_id,
									data.userId);
					if (giudizio_old != null) {
						// se 3esiste elimino i likes
						List<Likes> like_list = likeRepository
								.getGiudizioLikes((long) giudizio_old.getId());

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
								.getGiudizioLikes((long) giudizio.getId()));
					}

					return giudizi_list;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	@Scheduled(fixedRate = 1800000)
	public void synchronizeApprovalStatus() {
		updateApprovalStatus();
	}
	
	private void updateApprovalStatus() {
		List<Giudizio> pending = giudizioNewRepository.getGiudiziPending();
		Map<String, Giudizio> map = new HashMap<String, Giudizio>();
		if (pending != null) {
			for (Giudizio g : pending) {
				map.put(""+g.getId(), g);
			}
			Map<String, Boolean> updateComment = mediationParserImpl.updateComment(new LinkedList<String>(map.keySet()), tokenUtils.getClientToken());
			for (String id : updateComment.keySet()) {
				Giudizio g = map.get(id);
				g.setApproved(updateComment.get(id) ? State.APPROVED : State.NOT_APPROVED);
				giudizioNewRepository.save(g);
			}
		}
	}
}
