package smartcampus.webtemplate.controllers;

import java.awt.Menu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;

import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import smartcampus.ifame.model.MenuDelGiorno;
import smartcampus.ifame.model.MenuDellaSettimana;
import smartcampus.ifame.model.PiattiList;
import smartcampus.ifame.model.Piatto;
import smartcampus.ifame.model.Saldo;
import smartcampus.ifame.model.init.PiattoInit;
import smartcampus.ifame.model.init.ReadExcel;
import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.read.biff.BiffException;

@Controller("MenuController")
public class MenuController {

	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
	private static final Logger logger = Logger.getLogger(MenuController.class);
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

	@RequestMapping(method = RequestMethod.GET, value = "/getallpiatti")
	public @ResponseBody
	PiattiList getAllPiatti(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				PiattiList pl = new PiattiList();
				List<Piatto> list = PiattoInit.getpiatti();
				pl.setPiatti(list);

				return pl;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getmenudelgiorno")
	public @ResponseBody
	MenuDelGiorno getMenuDelGiorno(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {
				MenuDelGiorno mdg = new MenuDelGiorno();

				List<Piatto> list = PiattoInit.getpiatti();
				mdg.setPiattiDelGiorno(list);
				// String day = "";

				mdg.setMenuDate(new Date(System.currentTimeMillis()));
				// mdg.setMenuDate(new Date(day));

				return mdg;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * 
	 * 
	 * FUNZIONE NUOVA
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/getmenudelgiornolist")
	public @ResponseBody
	List<String> getMenuDelGiornoStringList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {
				
				return ReadExcel.getMenuOfTheDay("13");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	@RequestMapping(method = RequestMethod.GET, value = "/getmenudellasettimana")
	public @ResponseBody
	MenuDellaSettimana getMenuDellaSettimana(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			if (profile != null) {

				MenuDellaSettimana mds = new MenuDellaSettimana();
				ArrayList<MenuDelGiorno> listmdg = new ArrayList<MenuDelGiorno>();

				for (int i = 0; i < 7; i++) {
					MenuDelGiorno mdg = new MenuDelGiorno();
					List<Piatto> list = PiattoInit.getpiatti();
					mdg.setPiattiDelGiorno(list);
					mdg.setMenuDate(new Date(System.currentTimeMillis()));
					listmdg.add(mdg);
				}

				mds.setMenuDellaSettimana(listmdg);
				mds.setEnd_day(new Date(System.currentTimeMillis()));
				mds.setStart_day(new Date(System.currentTimeMillis()));

				return mds;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}