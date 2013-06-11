package eu.trentorise.smartcampus.vas.ifame.model.init;

import java.util.ArrayList;
import java.util.Calendar;

import eu.trentorise.smartcampus.vas.ifame.model.ListaMense;
import eu.trentorise.smartcampus.vas.ifame.model.Mensa;


public class ListaMenseInit {

	public static ListaMense getMenseValues() {

		final String url_povo_0_off = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo1.jpg";
		final String url_povo_1_off = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo2.jpg";
		final String url_tommaso_gar_off = "http://www.operauni.tn.it/upload/cms/456_x/gar-offline.jpg";
		final String url_zanella_off = "http://www.operauni.tn.it/upload/cms/456_x/mensa-zanella.jpg";
		final String url_mesiano_1_off = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-offline.jpg";
		final String url_mesiano_2_off = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-web-2.jpg";

		final String url_povo_0 = "http://www.operauni.tn.it/upload/Webcam/Povo01.jpg";
		final String url_povo_1 = "http://www.operauni.tn.it/upload/Webcam/Povo02.jpg";
		final String url_tommaso_gar = "http://www.operauni.tn.it/upload/Webcam/MensaUni.jpg";
		final String url_zanella = "http://www.operauni.tn.it/upload/Webcam/mensa_zanella.jpg";
		final String url_mesiano_1 = "http://www.operauni.tn.it/upload/Webcam/MensaMes01.jpg";
		final String url_mesiano_2 = "http://www.operauni.tn.it/upload/MensaMes02.jpg";

		final String name_povo_0 = "Povo Mensa";
		final String name_povo_1 = "Povo Mensa Veloce";
		final String name_tommaso_gar = "Tommaso Gar";
		final String name_zanella = "Zanella";
		final String name_mesiano_1 = "Mesiano 1";
		final String name_mesiano_2 = "Mesiano 2";

		Long[] id = { (long) 656356, (long) 344647, (long) 455365,
				(long) 356356, (long) 212112, (long) 455478 };

		Mensa povo_0;
		Mensa povo_1;
		Mensa tommaso_gar;
		Mensa zanella;
		Mensa mesiano_1;
		Mensa mesiano_2;

		Calendar date = Calendar.getInstance();
		int hour = date.get(Calendar.HOUR_OF_DAY);
		if (hour >= 12 && hour < 14) {
			// mense online
			povo_0 = new Mensa(id[0], name_povo_0, url_povo_1);
			povo_1 = new Mensa(id[1], name_povo_1, url_povo_0);
			tommaso_gar = new Mensa(id[2], name_tommaso_gar, url_tommaso_gar);
			zanella = new Mensa(id[3], name_zanella, url_zanella);
			mesiano_1 = new Mensa(id[4], name_mesiano_1, url_mesiano_1);
			mesiano_2 = new Mensa(id[5], name_mesiano_2, url_mesiano_2);

		} else {
			// mense offline
			povo_0 = new Mensa(id[0], name_povo_0, url_povo_1_off);
			povo_1 = new Mensa(id[1], name_povo_1, url_povo_0_off);
			tommaso_gar = new Mensa(id[2], name_tommaso_gar,
					url_tommaso_gar_off);
			zanella = new Mensa(id[3], name_zanella, url_zanella_off);
			mesiano_1 = new Mensa(id[4], name_mesiano_1, url_mesiano_1_off);
			mesiano_2 = new Mensa(id[5], name_mesiano_2, url_mesiano_2_off);

		}

		ArrayList<Mensa> mense = new ArrayList<Mensa>();
		mense.add(povo_0);
		mense.add(povo_1);
		mense.add(tommaso_gar);
		mense.add(zanella);
		mense.add(mesiano_1);
		mense.add(mesiano_2);

		ListaMense lm = new ListaMense();
		lm.setLast_update_milis(System.currentTimeMillis());
		lm.setList(mense);

		return lm;
	}

}
