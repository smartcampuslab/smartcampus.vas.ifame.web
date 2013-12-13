package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.vas.ifame.model.Likes;
import eu.trentorise.smartcampus.vas.ifame.repository.GiudizioRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.LikesRepository;

@Controller("LikeController")
public class LikeController {

	private static final Logger logger = Logger
			.getLogger(IGraditoController.class);

	@Autowired
	GiudizioRepository giudizioNewRepository;

	@Autowired
	LikesRepository likeRepository;

	@RequestMapping(method = RequestMethod.POST, value = "/giudizio/{giudizio_id}/like")
	public @ResponseBody
	void doLike(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("giudizio_id") Long giudizio_id,
			@RequestBody Likes like) throws IOException {
		try {
			logger.info("giudizio/" + giudizio_id + "/like");

			// se l'id del giudizio non esiste torno BAD REQUEST
			if (giudizioNewRepository.exists(giudizio_id)) {
				if (like.getUser_id() != null) {
					if (like.getIs_like() != null) {
						// ho tutti i dati compreso like

						// controllo se c'era gi� il like
						Likes old_like = likeRepository.alreadyLiked(
								giudizio_id, like.getUser_id());

						if (old_like != null) {
							// aggiorno quello esistente
							old_like.setIs_like(like.getIs_like());
							likeRepository.save(old_like);
						} else {
							// inserisco il nuovo like
							like.setGiudizio_id(giudizio_id);
							likeRepository.save(like);
						}
						return;
					}
				}
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		/*
		 * BAD REQUEST -> SE HO AVUTO ERRORI NEI CONTROLLI
		 */
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/giudizio/{giudizio_id}/like/delete")
	public @ResponseBody
	void deleteLike(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("giudizio_id") Long giudizio_id,
			@RequestBody Likes like) throws IOException {
		try {
			logger.info("giudizio/" + giudizio_id + "/like -> delete");

			// se l'id del giudizio o del like non esiste torno BAD REQUEST
			if (giudizioNewRepository.exists(giudizio_id)) {

				Likes old_like = likeRepository.alreadyLiked(giudizio_id,
						like.getUser_id());

				if (old_like != null) {

					likeRepository.delete(old_like);
					return;

				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		/*
		 * BAD REQUEST -> SE HO AVUTO ERRORI NEI CONTROLLI
		 */
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return;
	}
}
