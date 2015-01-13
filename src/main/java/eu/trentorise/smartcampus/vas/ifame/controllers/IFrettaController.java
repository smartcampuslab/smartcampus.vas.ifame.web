package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;
import java.util.List;

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
import eu.trentorise.smartcampus.vas.ifame.model.Mensa;
import eu.trentorise.smartcampus.vas.ifame.repository.MensaRepository;

@Controller("IFrettaController")
public class IFrettaController {

	@Autowired
	MensaRepository mensaRepository;

	private static final Logger logger = Logger
			.getLogger(IFrettaController.class);

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getmense")
	public @ResponseBody
	List<Mensa> getMense(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			logger.info("/getmense");
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			if (profile != null) {

				return mensaRepository.findAll();
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
