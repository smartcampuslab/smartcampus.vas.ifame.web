package eu.trentorise.smartcampus.vas.ifame.model.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.trentorise.smartcampus.vas.ifame.model.Piatto;
import eu.trentorise.smartcampus.vas.ifame.model.PiattoType;


public class PiattoInit {

	private static String[] piatto_name = { "Pasta Aglio olio e peperoncino",
			"Pasta al ragu", "Risotto ai funghi", "Scaloppine al limone",
			"Ossobuco alla romana", "Fagioli", "Patatine fritte", "Stinco",
			"Anatre all'arancia", "Insalata di stagione", "macedonia",
			"yoghurt trentino" };

	private static String[] piatto_description = {
			"pasta dal sapore pesante ma piacevole",
			"pasta molto delicata al gusto",
			"rissotto servito con funghi porcini e finferli",
			"carne di vitello cotta alla piastra con sugo di limone",
			"piacevole ossobuco servito su letto di pomodoro",
			"fagioli in brodo",
			"patate fatte in friggitrice",
			"stinco cotto al vapore",
			"anatre preparate con succo di arancie di sicilia e profumo di mare",
			"insalata fresca del trentino", "frutta fresca della val di sole",
			"per la descrizione vai al il sito www.yoghurt.it" };

	private static String[][] piatto_ingredients = {
			{ "pasta", "aglio", "olio", "peperoncino" },
			{ "pasta", "carne", "pomodoro" }, { "riso", "funghi" },
			{ "coscia di vitello", "limone" },
			{ "ossobuco", "pomodoro", "capperi" }, { "fagioli", "pepe" },
			{ "patate", "olio" }, { "stinco di maiale" },
			{ "anatre al vapore", "arancie di sicilia", "pepe", "sale" },
			{ "insalata trentina", "radicchio", "carote", "pomodori", "ceci" },
			{ "fragole", "lamponi", "ananas", "ciliegie" },
			{ "fermenti lattici", "aria del trentino", "tanta frutta" } };

	private static PiattoType[] piatto_type = { PiattoType.PRIMO,
			PiattoType.PRIMO, PiattoType.PRIMO, PiattoType.SECONDO,
			PiattoType.SECONDO, PiattoType.CONTORNO, PiattoType.CONTORNO,
			PiattoType.SECONDO, PiattoType.SECONDO, PiattoType.CONTORNO,
			PiattoType.DESSERT, PiattoType.DESSERT };

	public static Piatto createFakePiatto() {

		Random n = new Random();
		Piatto p = new Piatto();
		Integer i = n.nextInt(10);

		p.setPiatto_id(i);
		p.setPiatto_name(piatto_name[i]);
		p.setPiatto_type(piatto_type[i]);
		p.setPiatto_ingredients(piatto_ingredients[i]);
		p.setPiatto_description(piatto_description[i]);

		return p;
	}

	public static List<Piatto> getpiatti() {
		ArrayList<Piatto> list = new ArrayList<Piatto>();

		for (int i = 0; i < 12; i++) {
			// for (int i = 0; i < piatto_name.length; i++) {
			Piatto p = new Piatto();

			p.setPiatto_id(i);
			p.setPiatto_name(piatto_name[i]);
			p.setPiatto_type(piatto_type[i]);
			p.setPiatto_ingredients(piatto_ingredients[i]);
			p.setPiatto_description(piatto_description[i]);

			list.add(p);
		}

		return list;
	}

}
