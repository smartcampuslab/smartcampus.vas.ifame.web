package eu.trentorise.smartcampus.vas.ifame.utils;

import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

import eu.trentorise.smartcampus.vas.ifame.model.MenuDelGiorno;
import eu.trentorise.smartcampus.vas.ifame.model.MenuDelMese;
import eu.trentorise.smartcampus.vas.ifame.model.MenuDellaSettimana;
import eu.trentorise.smartcampus.vas.ifame.model.Piatto;

public class MenuXlsUtil {
	/*
	 * ALTERNATIVE
	 */

	public static List<Piatto> getAlternative(Workbook workbook) {

		List<Piatto> listaPiatti = new ArrayList<Piatto>();

		// le alternative sono uguali per tutte le settimane perciò leggiamo le
		// alternative sul foglio della prima settimana
		final int SHEET = 1;

		final int COLONNA_INIZIALE = 0;
		final int COLONNA_FINALE = 7;
		final int RIGA_INIZIALE = 16;
		final int RIGA_FINALE = 19;

		Sheet sheet = workbook.getSheet(SHEET);
		for (int colonna = COLONNA_INIZIALE; colonna <= COLONNA_FINALE; colonna = colonna + 2) {
			for (int riga = RIGA_INIZIALE; riga <= RIGA_FINALE; riga++) {
				Cell nomePiatto = sheet.getCell(colonna, riga);
				CellType type = nomePiatto.getType();
				// check se la casella contiene qualcosa
				if (type == CellType.LABEL) {
					Cell kcal = sheet.getCell(colonna + 1, riga);
					listaPiatti.add(new Piatto(nomePiatto.getContents(), kcal
							.getContents()));
				}
			}
		}

		return listaPiatti;
	}

	/*
	 * GET MENU DEL GIORNO
	 */

	public static MenuDelGiorno getMenuDelGiorno(Integer targetDay,
			Workbook workbook) {

		final int COLONNA_INIZIALE = 0;
		final int COLONNA_FINALE = 14; // shhet.getColumns();
		final int RIGA_INIZIALE = 5;
		final int RIGA_FINALE = 13;
		final int SHEET_INIZIALE = 0;
		final int SHEET_FINALE = 5;

		List<Piatto> listaPiatti = new ArrayList<Piatto>();
		Integer currentDay = 0;

		// ciclo sui fogli desiderati (evito la cena)
		for (int numSettimana = SHEET_INIZIALE; numSettimana < SHEET_FINALE; numSettimana++) {
			Sheet sheet = workbook.getSheet(numSettimana);

			// controllo ogni due colonne (evitando le kcal)
			for (int colonnaGiorno = COLONNA_INIZIALE; colonnaGiorno < COLONNA_FINALE; colonnaGiorno = colonnaGiorno + 2) {
				Cell current_day = sheet.getCell(colonnaGiorno, RIGA_INIZIALE);
				CellType day_type = current_day.getType(); // se

				// il giorno esiste aumento il contatore
				if (day_type == CellType.LABEL)
					currentDay++;

				// se il giorno ricercato è quello che corrente
				if (currentDay == targetDay) {
					for (int i = RIGA_INIZIALE; i < RIGA_FINALE; i++) {
						Cell cell = sheet.getCell(colonnaGiorno, i);
						CellType type = cell.getType();
						if (type == CellType.LABEL) {
							Cell kcal = sheet.getCell(colonnaGiorno + 1, i);
							listaPiatti.add(new Piatto(cell.getContents(), kcal
									.getContents()));
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
	 * GET MENU DEL MESE
	 */

	public static MenuDelMese getMenuDelMese(Workbook workbook) {

		final int COLONNA_INIZIALE = 0;
		final int COLONNA_FINALE = 14; // shhet.getColumns();
		final int RIGA_INIZIALE = 5;
		final int RIGA_FINALE = 13;
		final int SHEET_INIZIALE = 0;
		final int SHEET_FINALE = 5;

		// variabile per tenere il giorno corrente tracciato
		int giornoDelMese = 0;
		// creo il menu del mese e una lista di menu della settimana
		MenuDelMese mdm = new MenuDelMese();
		ArrayList<MenuDellaSettimana> mdslist = new ArrayList<MenuDellaSettimana>();
		// ciclo sui fogli desiderati (evito la cena)
		for (int numSettimana = SHEET_INIZIALE; numSettimana < SHEET_FINALE; numSettimana++) {
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
			for (int colonnaGiorno = COLONNA_INIZIALE; colonnaGiorno < COLONNA_FINALE; colonnaGiorno = colonnaGiorno + 2) {
				Cell current_day = sheet.getCell(colonnaGiorno, RIGA_INIZIALE);
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
					List<Piatto> piattiDelGiornoList = new ArrayList<Piatto>();
					// ciclo sulle right del giorno considerato
					for (int riga = RIGA_INIZIALE; riga < RIGA_FINALE; riga++) {

						Cell piattoNameCell = sheet
								.getCell(colonnaGiorno, riga);
						CellType piattoNameCellType = piattoNameCell.getType();
						// siccome dei giorni non hanno tutte le righe piene
						// controllo
						if (piattoNameCellType == CellType.LABEL) {
							Cell kcalCell = sheet.getCell(colonnaGiorno + 1,
									riga);
							piattiDelGiornoList.add(new Piatto(piattoNameCell
									.getContents(), kcalCell.getContents()));
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