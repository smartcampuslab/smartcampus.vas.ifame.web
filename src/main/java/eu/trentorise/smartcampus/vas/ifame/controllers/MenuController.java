package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.unidataservice.CanteenService;
import eu.trentorise.smartcampus.unidataservice.model.Dish;
import eu.trentorise.smartcampus.unidataservice.model.Menu;
import eu.trentorise.smartcampus.vas.ifame.model.MenuDelGiorno;
import eu.trentorise.smartcampus.vas.ifame.model.MenuDelMese;
import eu.trentorise.smartcampus.vas.ifame.model.Piatto;
import eu.trentorise.smartcampus.vas.ifame.repository.MensaRepository;
import eu.trentorise.smartcampus.vas.ifame.repository.PiattoRepository;
import eu.trentorise.smartcampus.vas.ifame.utils.EasyTokenManger;

@Controller("MenuController")
public class MenuController {

	private static final Logger logger = Logger.getLogger(MenuController.class);

	private static Workbook workbook = null;

	@Autowired
	PiattoRepository piattoRepository;

	@Autowired
	MensaRepository mensaRepository;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;
	@Autowired
	@Value("${client.id.sc}")
	private String clientId;
	@Autowired
	@Value("${client.secret.sc}")
	private String clientSecret;

	private EasyTokenManger tkm;

	@Autowired
	@Value("${url.studente.service}")
	private String URLStudenteService;

	private CanteenService studentInfoService;

	public static MenuDelMese MenuDelMeseUpdateForDish;
	
	@PostConstruct
	private void initStudentInfoService() {
		studentInfoService = new CanteenService(URLStudenteService);
		tkm = new EasyTokenManger(profileaddress, clientId, clientSecret);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getmenudelgiorno")
	public @ResponseBody
	MenuDelGiorno getMenuDelGiornoStringList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			logger.info("/getmenudelgiorno");

			Date datan = new Date(System.currentTimeMillis()
					- Long.valueOf("5843351778"));// TEST
			
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

			// call client unidata
			String dataFrom = sdf.format(datan);
			String dataTo = sdf.format(datan);

			List<Menu> listaMenu = studentInfoService.getMenu(
					tkm.getClientSmartCampusToken(), dataFrom, dataTo);

			return extrapolateMenu(sdf, listaMenu);
		

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private MenuDelGiorno extrapolateMenu(SimpleDateFormat sdf,
			List<Menu> listaMenu) throws ParseException {
		// FUNZIONE MAGICA //
		MenuDelGiorno menu = null;
		for (Menu index : listaMenu) {

			if (index.getType().compareTo("c") != 0) {
				menu = new MenuDelGiorno();

				Date date = sdf.parse(index.getDate());
				Calendar cal = new GregorianCalendar();
				cal.setTime(date);
				menu.setDay(cal.get(Calendar.DAY_OF_MONTH));

				List<Piatto> p = new ArrayList<Piatto>();

				for (Dish d : index.getDishes()) {
					Piatto piatto = new Piatto(d.getName(), d.getCal());
					p.add(piatto);

				}

				menu.setPiattiDelGiorno(p);
			}
		}
		return menu;

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

			MenuDelMese mm = new MenuDelMese();
			Calendar data = Calendar.getInstance();

			
			//TEST
			
			data.add(Calendar.MONTH, -2);
			
			//TEST
			
			
			
			int month = data.get(Calendar.MONTH);
			data.set(Calendar.DAY_OF_MONTH, 1);
			Date firstDateOfMonth = data.getTime();
			mm.setStart_day(data.get(Calendar.DAY_OF_MONTH));
			data.set(Calendar.DAY_OF_MONTH,
					data.getActualMaximum(Calendar.DATE));
			Date lastDateOfMonth = data.getTime();
			mm.setEnd_day(data.get(Calendar.DAY_OF_MONTH));

			// call client unidata
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
			String dataFrom = sdf.format(firstDateOfMonth);
			String dataTo = sdf.format(lastDateOfMonth);

			List<Menu> listaMenu = studentInfoService.getMenu(
					tkm.getClientSmartCampusToken(), dataFrom, dataTo);

			List<MenuDelGiorno> mg = new ArrayList<MenuDelGiorno>();
			for (Menu menu : listaMenu) {
				if(menu.getType().compareTo("p")==0){
				MenuDelGiorno newMenu = new MenuDelGiorno();

				Date date = sdf.parse(menu.getDate());
				Calendar cal = new GregorianCalendar();
				cal.setTime(date);
				newMenu.setDay(cal.get(Calendar.DAY_OF_MONTH));
				List<Piatto> piattiList=new ArrayList<Piatto>();
				for (Dish piattoEntrante : menu.getDishes()) {
					Piatto nuovopiatto=new Piatto();
					//nuovopiatto.setPiatto_id(piattoEntrante.getId());
					nuovopiatto.setPiatto_kcal(piattoEntrante.getCal());
					nuovopiatto.setPiatto_nome(piattoEntrante.getName());
					piattiList.add(nuovopiatto);
				}
				newMenu.setPiattiDelGiorno(piattiList);
				mg.add(newMenu);
				}

			}
			Collections.sort(mg, new Comparator<MenuDelGiorno>() {
			    public int compare(MenuDelGiorno a, MenuDelGiorno b) {
			        return Integer.signum(a.getDay() - b.getDay());
			    }			 
			});

			mm.setMenuDelGg(mg);
			MenuDelMeseUpdateForDish = mm;
			return mm;
		
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
	 * 
	 * restituisce le alternative al menu del giorno che sono sempre fisse
	 * leggendo l'excel
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/getalternative")
	public @ResponseBody
	List<Piatto> getAlternative(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {

			logger.info("/getalternative");
			//List<Piatto> alternative = new ArrayList<Piatto>();
			
			return getAlternativeMenu();// NewMenuXlsUtil.getAlternative(workbook);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// Test provvisorio
	public List<Piatto> getAlternativeMenu(){
		List<Piatto> alternative = piattoRepository.findAll();
		List<Piatto> ListAlternative = new ArrayList<Piatto>();
		for (Piatto p : alternative) {
			if ((p.getPiatto_nome().compareTo("Pasta/riso burro - pomodoro")==0)||
			(p.getPiatto_nome().compareTo("Pasta al ragu'")==0)||
			(p.getPiatto_nome().compareTo("Riso all’inglese")==0)||
			(p.getPiatto_nome().compareTo("Bistecca di tacchino")==0)||
			(p.getPiatto_nome().compareTo("Bistecca di manzo")==0)||
			(p.getPiatto_nome().compareTo("Pesce ai ferri")==0)||
			(p.getPiatto_nome().compareTo("Tagliere misto di salumi e formaggi")==0)||
			(p.getPiatto_nome().compareTo("Prosciutto crudo di parma")==0)||
			(p.getPiatto_nome().compareTo("Speck dell’alto adige")==0)||
			(p.getPiatto_nome().compareTo("Insalata mista di stagione")==0)||
			(p.getPiatto_nome().compareTo("Verdure grigliate")==0)){
				ListAlternative.add(p);
			}
		}
//		Piatto p = new Piatto();
//		p.setPiatto_id((long) 1);
//		p.setPiatto_nome("Pasta/Riso burro - pomodoro");
//		p.setPiatto_kcal("582/532");
//		alternative.add(p);
//		Piatto p1 = new Piatto();
//		p1.setPiatto_id((long) 2);
//		p1.setPiatto_nome("Pasta al ragu'");
//		p1.setPiatto_kcal("624");
//		alternative.add(p1);
//		Piatto p2 = new Piatto();
//		p2.setPiatto_id((long) 3);
//		p2.setPiatto_nome("Riso all’inglese");
//		p2.setPiatto_kcal("532");
//		alternative.add(p2);
//		Piatto p3 = new Piatto();
//		p3.setPiatto_id((long) 4);
//		p3.setPiatto_nome("Bistecca di tacchino");
//		p3.setPiatto_kcal("104");
//		alternative.add(p3);
//		Piatto p4 = new Piatto();
//		p4.setPiatto_id((long) 5);
//		p4.setPiatto_nome("Bistecca di manzo");
//		p4.setPiatto_kcal("190");
//		alternative.add(p4);
//		Piatto p5 = new Piatto();
//		p5.setPiatto_id((long) 6);
//		p5.setPiatto_nome("Pesce ai ferri");
//		p5.setPiatto_kcal("109");
//		alternative.add(p5);
//		Piatto p6 = new Piatto();
//		p6.setPiatto_id((long) 7);
//		p6.setPiatto_nome("Tagliere misto di salumi e formaggi");
//		p6.setPiatto_kcal("220");
//		alternative.add(p6);
//		Piatto p7 = new Piatto();
//		p7.setPiatto_id((long) 8);
//		p7.setPiatto_nome("Prosciutto crudo di Parma");
//		p7.setPiatto_kcal("202");
//		alternative.add(p7);
//		Piatto p8 = new Piatto();
//		p8.setPiatto_id((long) 9);
//		p8.setPiatto_nome("Speck dell’Alto Adige");
//		p8.setPiatto_kcal("225");
//		alternative.add(p8);
//		Piatto p9 = new Piatto();
//		p9.setPiatto_id((long) 10);
//		p9.setPiatto_nome("Insalata mista di stagione");
//		p9.setPiatto_kcal("114");
//		alternative.add(p9);
//		Piatto p10 = new Piatto();
//		p10.setPiatto_id((long) 11);
//		p10.setPiatto_nome("Verdure grigliate");
//		p10.setPiatto_kcal("48");
//		alternative.add(p10);
	
		
		return ListAlternative;
		
	} 
	

}

