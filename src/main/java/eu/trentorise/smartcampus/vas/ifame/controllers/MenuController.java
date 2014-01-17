package eu.trentorise.smartcampus.vas.ifame.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

			Calendar data = Calendar.getInstance();
			int day = data.get(Calendar.DAY_OF_MONTH);

			Date datan = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-m-d");

			// call client unidata
			String dataFrom = sdf.format(datan);
			String dataTo = sdf.format(datan);

			List<Menu> listaMenu = studentInfoService.getMenu(
					tkm.getClientSmartCampusToken(), dataFrom, dataTo);

			// http://localhost:8080/core.unidata/data/getmenu/2013-11-01/2013-11-01
			// String
			// piattiDiungiorno="[{\"id\":\"323031332d31312d30315f63\",\"date\":\"2013-11-01\",\"dishes\":[{\"name\":\"Zuppa di farro e fagioli\",\"cal\":\"485\"},{\"name\":\"Pasta panna e prosciutto\",\"cal\":\"619\"},{\"name\":\"Pasta cacio e pepe\",\"cal\":\"605\"},{\"name\":\"Saltimbocca alla romana\",\"cal\":\"312\"},{\"name\":\"Bruschetta rustica\",\"cal\":\"346\"},{\"name\":\"Patate al forno\",\"cal\":\"292\"},{\"name\":\"Carciofi saltati\",\"cal\":\"164\"}],\"type\":\"c\"},{\"id\":\"323031332d31312d30315f70\",\"date\":\"2013-11-01\",\"dishes\":[{\"name\":\"Zuppa di ceci\",\"cal\":\"472\"},{\"name\":\"Pasta alla marinara\",\"cal\":\"450\"},{\"name\":\"Wurstel farciti\",\"cal\":\"370\"},{\"name\":\"Merluzzo alle olive e capperi\",\"cal\":\"184\"},{\"name\":\"Crocchette di patate\",\"cal\":\"350\"},{\"name\":\"Carote prezzemolate\",\"cal\":\"140\"}],\"type\":\"p\"}]";

			return extrapolateMenu(sdf, listaMenu);
			// TEST
			// MenuDelGiorno mgg = new MenuDelGiorno();
			// mgg.setDay(15);
			// List<Piatto> lp = new ArrayList<Piatto>();
			// lp.add(new Piatto("test1","0"));
			// lp.add(new Piatto("test2","100"));
			//
			// mgg.setPiattiDelGiorno(lp);
			//
			// return mgg;

			// return NewMenuXlsUtil.getMenuDelGiorno(day, workbook);

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

			menu = new MenuDelGiorno();

			Date date = sdf.parse(index.getDate());
			menu.setDay(date.getDay());

			List<Piatto> p = new ArrayList<Piatto>();

			for (Dish d : index.getDishes()) {
				Piatto piatto = new Piatto(d.getName(), d.getCal());
				p.add(piatto);

			}

			menu.setPiattiDelGiorno(p);
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

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DATE, 1);
			Date firstDateOfMonth = cal.getTime();
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
			Date lastDateOfMonth = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-m-d");

			// call client unidata
			String dataFrom = sdf.format(firstDateOfMonth);
			String dataTo = sdf.format(lastDateOfMonth);

			List<Menu> listaMenu = studentInfoService.getMenu(
					tkm.getClientSmartCampusToken(), dataFrom, dataTo);
			MenuDelMese mm = new MenuDelMese();
			List<MenuDelGiorno> mg = new ArrayList<MenuDelGiorno>();
			for (Menu menu : listaMenu) {
				mg.add(extrapolateMenu(sdf, listaMenu));
				mm.setMenuDelGg(mg);
				mm.setStart_day(Integer.parseInt(dataFrom));
				mm.setEnd_day(Integer.parseInt(dataTo));
			}
			 MenuDelMese mmese = new MenuDelMese();
			 mmese.setStart_day(1);
			 mmese.setEnd_day(31);
			 MenuDelGiorno mgg = new MenuDelGiorno();
			 mgg.setDay(15);
			 List<Piatto> lp = new ArrayList<Piatto>();
			 lp.add(new Piatto("test1","0"));
			 lp.add(new Piatto("test2","100"));
			 mgg.setPiattiDelGiorno(lp);
			 List<MenuDelGiorno> lmg = new ArrayList<MenuDelGiorno>();
			 lmg.add(mgg);
			 mmese.setMenuDelGg(lmg);
			 return mmese;
			 //return mgg;
			// MenuDelMese mdm = NewMenuXlsUtil.getMenuDelMese(workbook);

			//return mm;

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

			return null;//NewMenuXlsUtil.getAlternative(workbook);

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
	private void initXls() throws BiffException, IOException {

		InputStream stream = getClass().getResourceAsStream("/Dicembre.xls");

	//	MenuController.workbook = NewMenuXlsUtil.getWorkbook(stream);
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * INIZIALIZZO TUTTE LE TABELLE
	 */

	// @PostConstruct
	// private void inizializzaDatabase() throws BiffException, IOException {
	//
	// logger.info("Called: inizializzaDatabase()");
	// // piattoRepository.deleteAll();
	//
	// // ********************************************************************
	// // Only from november to december to uniformate all piatto's name. After
	// // this few lines can be commented beacause the NewMenuXlsUtils class
	// // will take care of formatting the names (the first charachter
	// // uppercase and the rest of the string lowercase). This is to avoid
	// // multiple piatti with the same name with only differnet case letters
	// // --------------------------------------------------------------------
	// // get all the piatti
	// // List<Piatto> listDbPiatti = piattoRepository.findAll();
	// // for (Piatto piatto : listDbPiatti) {
	// // // format the name
	// // piatto.setPiatto_nome(NewMenuXlsUtil.format(piatto.getPiatto_nome()));
	// // // update the database
	// // piattoRepository.saveAndFlush(piatto);
	// // }
	// // ********************************************************************
	//
	// // get the updated piatti and now do what you want
	// List<Piatto> listDbPiatti = piattoRepository.findAll();
	//
	// // get the new set of piatti
	// Workbook excel = NewMenuXlsUtil.getWorkbook(getClass()
	// .getResourceAsStream("/Dicembre.xls"));
	// MenuDelMese menuDelMese = NewMenuXlsUtil.getMenuDelMese(excel);
	//
	// Set<Piatto> setPiatti = new HashSet<Piatto>();
	//
	// for (MenuDellaSettimana mds : menuDelMese.getMenuDellaSettimana()) {
	// List<MenuDelGiorno> mdglist = mds.getMenuDelGiorno();
	// for (MenuDelGiorno mdg : mdglist) {
	// List<Piatto> piattiDelGiorno = mdg.getPiattiDelGiorno();
	// for (Piatto p : piattiDelGiorno) {
	// setPiatti.add(p);
	// }
	// }
	// }
	//
	// int counterNuoviPiatti = 0;
	// // for each new piatto in the new menu check if it's in the db
	// for (Piatto nuovoPiatto : setPiatti) {
	// if (!listDbPiatti.contains(nuovoPiatto)) {
	// // is not in the db so save it
	// piattoRepository.saveAndFlush(nuovoPiatto);
	// logger.info(++counterNuoviPiatti + ". Saved: "
	// + nuovoPiatto.getPiatto_nome());
	// }
	// }
	// }

	// @Scheduled(fixedDelay = 900000)
	// // 15min
	// public void updateMenuList() throws AACException, SecurityException,
	// UnidataServiceException {
	// log.debug("Update comment in local");
	// List<CanteenOpening> mense = studentInfoService.getOpening(tkm
	// .getClientSmartCampusToken());
	// List<Mensa> menseList = new ArrayList<Mensa>();
	// for (CanteenOpening canteen : mense) {
	// Mensa m = new Mensa();
	// m.setMensa_nome(canteen.getCanteen());
	// menseList.add(m);
	// // TODO: merging mense
	// }
	// // salvo/aggiorno db
	// mensaRepository.save(menseList);
	// }

}

// *************************************************************
// finche non ci sono aggiornamenti sui link delle webcam o nomi delle
// mense lasciare tutto commentato
// *************************************************************
// /*
// *
// * Inizializzo le mense
// */
// final String url_povo_mensa_offline =
// "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo2.jpg";
// final String url_povo_mensa_veloce_offline =
// "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo1.jpg";
// final String url_tommaso_gar_offline =
// "http://www.operauni.tn.it/upload/cms/456_x/gar-offline.jpg";
// final String url_zanella_offline =
// "http://www.operauni.tn.it/upload/cms/456_x/mensa-zanella.jpg";
// final String url_mesiano_1_offline =
// "http://www.operauni.tn.it/upload/cms/456_x/mesiano-offline.jpg";
// final String url_mesiano_2_offline =
// "http://www.operauni.tn.it/upload/cms/456_x/mesiano-web-2.jpg";
//
// final String url_povo_mensa_online =
// "http://www.operauni.tn.it/upload/Webcam/Povo02.jpg";
// final String url_povo_mensa_veloce_online =
// "http://www.operauni.tn.it/upload/Webcam/Povo01.jpg";
// final String url_tommaso_gar_online =
// "http://www.operauni.tn.it/upload/Webcam/MensaUni.jpg";
// final String url_zanella_online =
// "http://www.operauni.tn.it/upload/Webcam/mensa_zanella.jpg";
// final String url_mesiano_1_online =
// "http://www.operauni.tn.it/upload/Webcam/MensaMes01.jpg";
// final String url_mesiano_2_online =
// "http://www.operauni.tn.it/upload/Webcam/MensaMes02.jpg";
//
// final String name_povo_mensa = "Povo Mensa";
// final String name_povo_mensa_veloce = "Povo Mensa Veloce";
// final String name_tommaso_gar = "Tommaso Gar";
// final String name_zanella = "Zanella";
// final String name_mesiano_1 = "Mesiano 1";
// final String name_mesiano_2 = "Mesiano 2";
//
// Mensa povo_mensa = new Mensa(name_povo_mensa, url_povo_mensa_online,
// url_povo_mensa_offline);
// Mensa povo_mensa_veloce = new Mensa(name_povo_mensa_veloce,
// url_povo_mensa_veloce_online, url_povo_mensa_veloce_offline);
// Mensa tommaso_gar = new Mensa(name_tommaso_gar,
// url_tommaso_gar_online,
// url_tommaso_gar_offline);
// Mensa zanella = new Mensa(name_zanella, url_zanella_online,
// url_zanella_offline);
// Mensa mesiano_1 = new Mensa(name_mesiano_1, url_mesiano_1_online,
// url_mesiano_1_offline);
// Mensa mesiano_2 = new Mensa(name_mesiano_2, url_mesiano_2_online,
// url_mesiano_2_offline);
//
// mensaRepository.save(povo_mensa);
// mensaRepository.save(povo_mensa_veloce);
// mensaRepository.save(tommaso_gar);
// mensaRepository.save(zanella);
// mensaRepository.save(mesiano_1);
// mensaRepository.save(mesiano_2);