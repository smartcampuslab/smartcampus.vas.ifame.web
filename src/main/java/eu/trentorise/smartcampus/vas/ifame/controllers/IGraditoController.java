package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.filestorage.client.utils.Utils;
import eu.trentorise.smartcampus.mediation.model.CommentBaseEntity;
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
import eu.trentorise.smartcampus.vas.ifame.utils.EasyTokenManger;

@Controller("GiudizioController")
public class IGraditoController {

	private static final Logger log = Logger
			.getLogger(IGraditoController.class);

	@Autowired
	PiattoRepository piattoRepository;

	@Autowired
	MensaRepository mensaRepository;

	@Autowired
	GiudizioRepository giudizioNewRepository;

	@Autowired
	LikesRepository likeRepository;

	// @Autowired
	// private MediationParserImpl mediationParserImpl;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;
	@Autowired
	@Value("${client.id.sc}")
	private String clientId;
	@Autowired
	@Value("${client.secret.sc}")
	private String clientSecret;

	private eu.trentorise.smartcampus.vas.ifame.utils.EasyTokenManger tkm;

	@PostConstruct
	private void init() {
		tkm = new EasyTokenManger(profileaddress, clientId, clientSecret);

	}

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
			log.info("/getpiatti");

			return piattoRepository.findAll();

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
			@PathVariable("mensa_id") String mensa_id,
			@PathVariable("piatto_id") Long piatto_id) throws IOException {
		try {
			log.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio");

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

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}

		/*
		 * BAD REQUEST SE HO ERRORI NEI CONTROLLI
		 */
		//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
			@PathVariable("mensa_id") String mensa_id,
			@PathVariable("piatto_id") Long piatto_id,
			@PathVariable("user_id") Long user_id) throws IOException {
		try {
			log.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/user_id/" + user_id + "/giudizio");

			if (mensaRepository.exists(mensa_id)
					&& piattoRepository.exists(piatto_id)) {

				return  giudizioNewRepository.getUserGiudizioApproved(mensa_id,
						piatto_id, user_id);

			} else {
				/*
				 * not found se non trova mensa e/o piatto
				 */
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
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

	@Scheduled(fixedDelay = 900000)
	// 15min
	public void updateRemoteComment() throws AACException {
		log.debug("Update comment in local");
		// aggiorno i commenti
		// Map<String, Boolean> updatedCommentList = mediationParserImpl
		// .updateComment(0,System.currentTimeMillis(),
		// tkm.getClientSmartCampusToken());
		// if (updatedCommentList != null && !updatedCommentList.isEmpty()) {
		// Iterator iterator = updatedCommentList.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Map.Entry mapEntry = (Map.Entry) iterator.next();
		//
		// Giudizio g = giudizioNewRepository.findOne((Long)mapEntry.getKey());
		// g.setApproved((Boolean)mapEntry.getValue());
		// giudizioNewRepository.saveAndFlush(g);
		//
		// }
		// }
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
			@PathVariable("mensa_id") String mensa_id,
			@PathVariable("piatto_id") Long piatto_id,
			@RequestBody GiudizioDataToPost data) throws IOException {
		try {

			log.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
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
							.getUserGiudizioApproved(mensa_id, piatto_id,
									data.userId);

					// mediationParserImpl.updateKeyWord(token);

					if (giudizio_old != null) {

						log.info("Aggiorno il giudizio");
						/*
						 * lo aggiorno
						 */
						giudizio_old.setUltimo_aggiornamento(new Date(System
								.currentTimeMillis()));

						giudizio_old.setVoto(data.voto);
						giudizio_old.setTesto(data.commento);

						// giudizio_old.setApproved(mediationParserImpl
						// .localValidationComment(
						// giudizio_old.getTesto(), giudizio_old
						// .getId().toString(), userId,
						// token));

						if (giudizio_old.isApproved()) {
							// giudizio_old.setApproved(mediationParserImpl
							// .remoteValidationComment(
							// giudizio_old.getTesto(),
							// giudizio_old.getId().toString(),
							// userId, token));
						}

						if (giudizio_old.isApproved()) {
							giudizio_old.setUser_name(profile.getName() + "."
									+ profile.getSurname());

							giudizio_old = giudizioNewRepository
									.save(giudizio_old);

							List<Likes> like_list = likeRepository
									.getGiudizioLikes((long) giudizio_old
											.getId());

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

						giudizio.setApproved(true);

						giudizio.setUser_name(profile.getName() + "."
								+ profile.getSurname());

						giudizio = giudizioNewRepository.save(giudizio);

						// // giudizio.setApproved(mediationParserImpl
						// .localValidationComment(giudizio.getTesto(),
						// giudizio.getId().toString(), userId,
						// token));

						giudizio = giudizioNewRepository.save(giudizio);

						if (giudizio.isApproved()) {
							// giudizio.setApproved(mediationParserImpl
							// .remoteValidationComment(giudizio
							// .getTesto(), giudizio.getId().toString(), userId,
							// token));
						}

						if (giudizio.isApproved()) {
							giudizioNewRepository.save(giudizio);
						} else {
							giudizioNewRepository.delete(giudizio);
						}
					}
					//
					// // aggiorno i commenti
					// List<CommentBaseEntity> updatedCommentList =
					// (List<CommentBaseEntity>) mediationParserImpl
					// .updateCommentToMediationService(
					// getCommentBase(giudizioNewRepository
					// .findAll()), token);
					//
					// for (CommentBaseEntity updatedEntity :
					// updatedCommentList) {
					//
					// Giudizio g = giudizioNewRepository
					// .findOne(updatedEntity.getId());
					// g.setApproved(updatedEntity.isApproved());
					// giudizioNewRepository.saveAndFlush(g);
					//
					// }

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
					log.info("Mensa or piatto not found");
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
		log.info("Bad request");
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
			@PathVariable("mensa_id") String mensa_id,
			@PathVariable("piatto_id") Long piatto_id,
			@PathVariable("giudizio_id") Long giudizio_id,
			@RequestBody GiudizioDataToPost data) throws IOException {
		try {
			log.info("/mensa/" + mensa_id + "/piatto/" + piatto_id
					+ "/giudizio/" + giudizio_id + "/delete");

			if (mensaRepository.exists(mensa_id)
					&& piattoRepository.exists(piatto_id)
					&& giudizioNewRepository.exists(giudizio_id)) {

				Giudizio giudizio_old = giudizioNewRepository
						.getUserGiudizioApproved(mensa_id, piatto_id,
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
