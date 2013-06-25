package eu.trentorise.smartcampus.vas.ifame.webtemplate.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import eu.trentorise.smartcampus.vas.ifame.model.GiudizioNew;
import eu.trentorise.smartcampus.vas.ifame.model.Likes;
import eu.trentorise.smartcampus.vas.ifame.repository.GiudizioNewRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.LikesRepository;

@Controller("LikeController")
public class LikeController {

	private static final Logger logger = Logger
			.getLogger(GiudizioController.class);
	@Autowired
	private AcService acService;

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

	@RequestMapping(method = RequestMethod.POST, value = "/like")
	public @ResponseBody
	void doLike(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody Likes like) throws IOException {
		try {
			logger.info("/like");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				if (like.getGiudizio_id() != null && like.getUser_id() != null
						&& like.getIs_like() != null) {

					if (giudizioNewRepository.exists(like.getGiudizio_id())) {

						Likes old_like = likeRepository.alreadyLiked(
								like.getGiudizio_id(), like.getUser_id());

						if (old_like != null) {
							old_like.setIs_like(like.getIs_like());

							likeRepository.save(old_like);
						} else {

							likeRepository.save(like);
						}
						return;
					}
				}
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return;
	}
}
