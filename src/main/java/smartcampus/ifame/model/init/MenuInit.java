package smartcampus.ifame.model.init;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import smartcampus.ifame.model.Alternative;
import smartcampus.ifame.model.MenuDelGiorno;
import smartcampus.ifame.model.MenuDelMese;
import smartcampus.ifame.model.MenuDellaSettimana;
import smartcampus.ifame.model.PiattoKcal;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.read.biff.BiffException;

public class MenuInit {
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * ALTERNATIVE
	 */

	public static Alternative getAlternative(Workbook workbook) {

		Alternative alternative = new Alternative();
		List<PiattoKcal> listaPiatti = new ArrayList<PiattoKcal>();

		// le alternative sono uguali per tutte le settimane perciò leggiamo le
		// alternative sul foglio della prima settimana
		final int SHEET = 1;

		final int COLONNA_INIZIALE = 0;
		final int COLONNA_FINALE = 7;
		final int RIGA_INIZIALE = 15;
		final int RIGA_FINALE = 18;

		Sheet sheet = workbook.getSheet(SHEET);
		for (int colonna = COLONNA_INIZIALE; colonna <= COLONNA_FINALE; colonna = colonna + 2) {
			for (int riga = RIGA_INIZIALE; riga <= RIGA_FINALE; riga++) {
				Cell nomePiatto = sheet.getCell(colonna, riga);
				CellType type = nomePiatto.getType();
				// check se la casella contiene qualcosa
				if (type == CellType.LABEL) {
					Cell kcal = sheet.getCell(colonna + 1, riga);
					listaPiatti.add(new PiattoKcal(nomePiatto.getContents(),
							kcal.getContents()));
				}
			}
		}
		alternative.setAlternative(listaPiatti);

		return alternative;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * GET MENU DEL GIORNO
	 */

	public static MenuDelGiorno getMenuDelGiorno(Integer targetDay,
			Workbook workbook) {

		List<PiattoKcal> listaPiatti = new ArrayList<PiattoKcal>();
		Integer currentDay = 0;

		// ciclo sui fogli desiderati (evito la cena)
		for (int numSettimana = 0; numSettimana < 5; numSettimana++) {
			Sheet sheet = workbook.getSheet(numSettimana);

			// controllo ogni due colonne (evitando le kcal)
			for (int colonnaGiorno = 0; colonnaGiorno < sheet.getColumns(); colonnaGiorno = colonnaGiorno + 2) {
				Cell current_day = sheet.getCell(colonnaGiorno, 3);
				CellType day_type = current_day.getType(); // se

				// il giorno esiste aumento il contatore
				if (day_type == CellType.LABEL)
					currentDay++;

				// se il giorno ricercato è quello che corrente
				if (currentDay == targetDay) {
					for (int i = 4; i < 12; i++) {
						Cell cell = sheet.getCell(colonnaGiorno, i);
						CellType type = cell.getType();
						CellFormat cformat = cell.getCellFormat();
						if (type == CellType.LABEL) {
							Cell kcal = sheet.getCell(colonnaGiorno + 1, i);
							listaPiatti.add(new PiattoKcal(cell.getContents(),
									kcal.getContents()));
						}
					}
				}
			}
		}

		MenuDelGiorno mdg = new MenuDelGiorno();
		mdg.setPiattiDelGiorno(listaPiatti);
		mdg.setDay(targetDay);

		return mdg;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * GET MENU DELLA SETTIMANA
	 */

	public static MenuDellaSettimana getMenuDellaSettimana(Integer targetDay,
			Workbook workbook) {

		MenuDellaSettimana mds = new MenuDellaSettimana();
		List<MenuDelGiorno> mdglist = new ArrayList<MenuDelGiorno>();

		Integer currentDay = 0;
		// ciclo sui fogli desiderati (evito la cena)
		for (int numSettimana = 0; numSettimana < 5; numSettimana++) {
			// -1 è perchè in tutti sti for si fa un ciclo in piu
			int giorniDaInizioSettimanaCorrente = -1;
			Sheet sheet = workbook.getSheet(numSettimana);

			// controllo ogni due colonne (evitando le kcal)
			for (int colonnaGiorno = 0; colonnaGiorno < sheet.getColumns(); colonnaGiorno = colonnaGiorno + 2) {
				Cell current_day = sheet.getCell(colonnaGiorno, 3);
				CellType day_type = current_day.getType();

				// se il giorno esiste aumento il contatore
				if (day_type == CellType.LABEL) {
					currentDay++;
					giorniDaInizioSettimanaCorrente++;
				}

				// il giorno ricercato è quello che esamino
				if (currentDay == targetDay) {
					int primoGiornoSettimanaCorrente = currentDay
							- giorniDaInizioSettimanaCorrente;
					// set giorno di inizio di settimana in menu della
					// settimana
					mds.setStart_day(primoGiornoSettimanaCorrente);
					// ciclo per i giorni della settimana che mi interessa
					for (int colonnaMenuDelGiorno = 0; colonnaMenuDelGiorno < sheet
							.getColumns(); colonnaMenuDelGiorno = colonnaMenuDelGiorno + 2) {

						// ogni piatto li inserisco nella lista
						List<PiattoKcal> piattiDelGiornoList = new ArrayList<PiattoKcal>();
						for (int i = 4; i < 12; i++) {
							Cell cell = sheet.getCell(colonnaMenuDelGiorno, i);
							CellType type = cell.getType();
							if (type == CellType.LABEL) {
								Cell kcal = sheet.getCell(
										colonnaMenuDelGiorno + 1, i);
								piattiDelGiornoList.add(new PiattoKcal(cell
										.getContents(), kcal.getContents()));

							}
						}
						// ogni giorno ho un nuovo menu del giorno
						MenuDelGiorno mdg = new MenuDelGiorno();
						mdg.setDay(primoGiornoSettimanaCorrente);
						mdg.setPiattiDelGiorno(piattiDelGiornoList);
						mdglist.add(mdg);
						// incremento il giorno della settimana
						primoGiornoSettimanaCorrente++;
					}
				}
			}
		}

		mds.setMenuDelGiorno(mdglist);

		return mds;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * GET MENU DEL MESE
	 */

	public static MenuDelMese getMenuDelMese(Workbook workbook) {

		// variabile per tenere il giorno corrente tracciato
		int giornoDelMese = 0;
		// creo il menu del mese e una lista di menu della settimana
		MenuDelMese mdm = new MenuDelMese();
		ArrayList<MenuDellaSettimana> mdslist = new ArrayList<MenuDellaSettimana>();
		// ciclo sui fogli desiderati (evito la cena)
		for (int numSettimana = 0; numSettimana < 5; numSettimana++) {
			// ogni foglio ha una settimana
			Sheet sheet = workbook.getSheet(numSettimana);
			// variabile per tracciare qual'e' il primo giorno della settimana
			// corrente
			int giornoSettimanaCorrente = 0;
			// ogni settimana ho un menu della settimana e una lista di menu del
			// giorno
			MenuDellaSettimana mds = new MenuDellaSettimana();
			List<MenuDelGiorno> mdglist = new ArrayList<MenuDelGiorno>();
			// controllo per ogni giorno due colonne (evitando le kcal)
			for (int colonnaGiorno = 0; colonnaGiorno < sheet.getColumns(); colonnaGiorno = colonnaGiorno + 2) {
				Cell current_day = sheet.getCell(colonnaGiorno, 3);
				CellType day_type = current_day.getType();
				// se ho scritte nella label il giorno esiste e aumento il
				// contatore ho un giorno in piu
				if (day_type == CellType.LABEL) {
					// c'è il giorno incremento giorno corrente e giorno
					// settimana corrente
					giornoDelMese++;
					giornoSettimanaCorrente++;
					// controllo che non sia il primo della settimana altrimenti
					if (giornoSettimanaCorrente == 1) {
						// lo setto come startday
						mds.setStart_day(giornoDelMese);
					}
					// ogni giorno ho un nuovo menu del giorno e una lista di
					// piatti
					MenuDelGiorno mdg = new MenuDelGiorno();
					List<PiattoKcal> piattiDelGiornoList = new ArrayList<PiattoKcal>();
					// ciclo sulle right del giorno considerato
					for (int riga = 4; riga < 12; riga++) {
						Cell piattoNameCell = sheet
								.getCell(colonnaGiorno, riga);
						CellType piattoNameCellType = piattoNameCell.getType();
						// siccome dei giorni non hanno tutte le righe piene
						// controllo
						if (piattoNameCellType == CellType.LABEL) {
							Cell kcalCell = sheet.getCell(colonnaGiorno + 1,
									riga);
							piattiDelGiornoList.add(new PiattoKcal(
									piattoNameCell.getContents(), kcalCell
											.getContents()));
						}
					}
					// setto il giorno corrente al menu del giorno
					mdg.setDay(giornoDelMese);
					// aggiungo la lista al menu del giorno
					mdg.setPiattiDelGiorno(piattiDelGiornoList);
					// aggiungo il menu del giorno alla lista di menu del giorno
					// per la settimana corrente
					mdglist.add(mdg);
				}
			}
			// aggiungo la lista di menu del giorno al menu della settimana
			mds.setMenuDelGiorno(mdglist);
			// setto il giorno corrente come ultimo della settimana
			mds.setEnd_day(giornoDelMese);
			// aggiungo alla lista di menu della settimana il menu della
			// settimana corrente
			mdslist.add(mds);
		}
		// setto la lista di menu della settimana
		mdm.setMenuDellaSettimana(mdslist);
		// setto il primo e l'ultimo giorno del mese
		mdm.setStart_day(1);
		mdm.setEnd_day(giornoDelMese);

		return mdm;
	}
}