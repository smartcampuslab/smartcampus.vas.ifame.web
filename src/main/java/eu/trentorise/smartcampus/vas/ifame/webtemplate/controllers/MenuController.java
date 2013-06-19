package eu.trentorise.smartcampus.vas.ifame.webtemplate.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.vas.ifame.model.Mensa;
import eu.trentorise.smartcampus.vas.ifame.model.MenuDelGiorno;
import eu.trentorise.smartcampus.vas.ifame.model.MenuDelMese;
import eu.trentorise.smartcampus.vas.ifame.model.MenuDellaSettimana;
import eu.trentorise.smartcampus.vas.ifame.model.Piatto;
import eu.trentorise.smartcampus.vas.ifame.model.Piatto_Mensa;
import eu.trentorise.smartcampus.vas.ifame.repository.MensaRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.PiattoRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.Piatto_MensaRepository;
import eu.trentorise.smartcampus.vas.ifame.utils.MenuXlsUtil;

@Controller("MenuController")
public class MenuController {

	private static final Logger logger = Logger.getLogger(MenuController.class);

	private static Workbook workbook = null;

	@Autowired
	PiattoRepository piattoRepository;

	@Autowired
	MensaRepository mensaRepository;

	@Autowired
	Piatto_MensaRepository piattoMensaRepository;

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

	/*
	 * 
	 * 
	 * old method no more used
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getallpiatti")
	public @ResponseBody
	List<Piatto> getAllPiatti(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			logger.info("/getallpiatti");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
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
	 * 
	 * new service for igradito
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/iGradito/{mensa_id}")
	public @ResponseBody
	List<Piatto_Mensa> getMensaPiatti(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("mensa_id") Long mensa_id) throws IOException {
		try {
			logger.info("/iGradito/{mensa_id}");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				System.out.println(mensa_id);

				return piattoMensaRepository
						.getPiattiWhereMensaIdEquals(mensa_id);
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
	 * 
	 * MENU DEL GIORNO CONTROLLER
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/getmenudelgiorno")
	public @ResponseBody
	MenuDelGiorno getMenuDelGiornoStringList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			logger.info("/getmenudelgiorno");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				Calendar data = Calendar.getInstance();
				int day = data.get(Calendar.DAY_OF_MONTH);

				return MenuXlsUtil.getMenuDelGiorno(day, workbook);
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
	 * 
	 * MENU DEL MESE
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/getmenudelmese")
	public @ResponseBody
	MenuDelMese getMenuDelMese(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {

			logger.info("/getmenudelmese");
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				MenuDelMese mdm = MenuXlsUtil.getMenuDelMese(workbook);

				return mdm;

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
	 * 
	 * ALTERNATIVE CONTROLLER
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/getalternative")
	public @ResponseBody
	List<Piatto> getAlternative(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {

			logger.info("/getalternative");
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				return MenuXlsUtil.getAlternative(workbook);
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
	 * INIZIALIZZO IL WORKBOOK
	 */
	@PostConstruct
	private void initXls() {
		InputStream is = getClass().getResourceAsStream("/Menu.xls");
		WorkbookSettings xlsSettings = new WorkbookSettings();
		xlsSettings.setDrawingsDisabled(true);
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(is, xlsSettings);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MenuController.workbook = w;
	}

	@PostConstruct
	private void inizializzaTabellaPiatti() {

		List<MenuDellaSettimana> mdslist = MenuXlsUtil.getMenuDelMese(workbook)
				.getMenuDellaSettimana();

		Set<Piatto> setPiatti = new HashSet<Piatto>();

		for (MenuDellaSettimana mds : mdslist) {
			List<MenuDelGiorno> mdglist = mds.getMenuDelGiorno();
			for (MenuDelGiorno mdg : mdglist) {
				List<Piatto> piattiDelGiorno = mdg.getPiattiDelGiorno();
				for (Piatto p : piattiDelGiorno) {
					setPiatti.add(p);
				}
			}
		}

		piattoRepository.save(setPiatti);

		inizializzaTabella_PIATTO_MENSA();
	}

	private void inizializzaTabella_PIATTO_MENSA() {

		List<Mensa> lista_mense = mensaRepository.findAll();
		List<Piatto> lista_piatti = piattoRepository.findAll();

		for (Mensa mensa : lista_mense) {
			for (Piatto piatto : lista_piatti) {
				Piatto_Mensa piatto_mensa = new Piatto_Mensa((long) 0,
						(float) 0, mensaRepository.save(mensa),
						piattoRepository.save(piatto));
				piattoMensaRepository.saveAndFlush(piatto_mensa);
			}
		}
		
		
		
	}

}