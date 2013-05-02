package smartcampus.ifame.model.init;

import java.util.ArrayList;

import smartcampus.ifame.model.ListaMense;
import smartcampus.ifame.model.Mensa;

public class ListaMenseInit {

	public static ListaMense getMenseValues() {

		final String url_povo_0 = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo1.jpg";
		final String url_povo_1 = "http://www.operauni.tn.it/upload/cms/456_x/mensa-povo2.jpg";
		final String url_tommaso_gar = "http://www.operauni.tn.it/upload/cms/456_x/gar-offline.jpg";
		final String url_zanella = "http://www.operauni.tn.it/upload/cms/456_x/mensa-zanella.jpg";
		final String url_mesiano = "http://www.operauni.tn.it/upload/cms/456_x/mesiano-offline.jpg";

		final String name_povo_0 = "POVO_0";
		final String name_povo_1 = "POVO_1";
		final String name_tommaso_gar = "TOMMASO_GAR";
		final String name_zanella = "ZANELLA";
		final String name_mesiano = "MESIANO";

		Long[] id = { (long) 656356, (long) 344647, (long) 455365,
				(long) 356356, (long) 212112 };

		Mensa povo_0 = new Mensa(id[0], name_povo_0, url_povo_0);
		Mensa povo_1 = new Mensa(id[1], name_povo_1, url_povo_1);
		Mensa tommaso_gar = new Mensa(id[2], name_tommaso_gar, url_tommaso_gar);
		Mensa zanella = new Mensa(id[3], name_zanella, url_zanella);
		Mensa mesiano = new Mensa(id[4], name_mesiano, url_mesiano);

		ArrayList<Mensa> mense = new ArrayList<Mensa>();
		mense.add(povo_0);
		mense.add(povo_1);
		mense.add(tommaso_gar);
		mense.add(zanella);
		mense.add(mesiano);

		ListaMense lm = new ListaMense();
		lm.setLast_update_milis(System.currentTimeMillis());
		lm.setList(mense);

		return lm;
	}

}
