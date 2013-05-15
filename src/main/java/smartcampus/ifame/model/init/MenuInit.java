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

import smartcampus.ifame.model.MenuDelGiorno;
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

		mds.setMenuDellaSettimana(mdglist);

		return mds;
	}
}