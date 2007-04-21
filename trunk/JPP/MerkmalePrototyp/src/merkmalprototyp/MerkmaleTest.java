/*
 * MerkmaleTest.java
 *
 * Created on 20. April 2007, 13:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package merkmalprototyp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox.KeySelectionManager;

import merkmalprototyp.merkmale.BildbreiteMerkmal;
import merkmalprototyp.merkmale.BildhoeheMerkmal;
import merkmalprototyp.merkmale.DateigroesseMerkmal;
import merkmalprototyp.merkmale.DateinameMerkmal;
import merkmalprototyp.merkmale.DateitypMerkmal;
import merkmalprototyp.merkmale.LetzterZugriffMerkmal;
import merkmalprototyp.merkmale.ThumbnailMerkmal;

/**
 * 
 * @author Besitzer
 */
public class MerkmaleTest {

	/** Ordner in dem die Testbilder liegen. */
	private static final String VERZEICHNISPFAD =
	// "F:\\Studium\\4
	// Semester\\PPR\\JPP\\src\\merkmalprototyp\\zollverein.jpg";
	"/windows/daten/quatsch/";

	private Map<String, Double> wertung;
	
	private int anzahlMerkmale;
	
	/** Creates a new instance of MerkmaleTest */
	public MerkmaleTest() {
		wertung = new HashMap<String, Double>();
		
		Date vorher = new Date();
		importVerzeichnis(VERZEICHNISPFAD);
		Date nachher = new Date();

		/* Auswertung ausgeben. */
		long dauer = nachher.getTime() - vorher.getTime();
		System.out.println("Benoetigte Zeit: "
				+ dauer + " (in ms)");
		
		Set<String> mSet = wertung.keySet();
		for (String m : mSet) {
			System.out.println(m + ": " + (wertung.get(m) / dauer * 100) + " %");
		}
		
	}

	private void importVerzeichnis(String verzeichnis) {
		File dir = new File(verzeichnis);
		if (dir.isDirectory()) {

			/*
			 * Holt alle Dateien, die mit .jpg, .png oder .gif enden.
			 */
			File[] files = dir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getPath().endsWith(".jpg")
							|| pathname.getPath().endsWith(".png")
							|| pathname.getPath().endsWith(".gif");
				}

			});
			System.out.println("Es werden " + files.length
					+ " Bilder importiert.");
			
			for (int i = 0; i < files.length; i++) {
				importDateiA(files[i]);
			}

		} else {
			System.out.println("VERZEICHNISPFAD gibt kein Verzeichnis an.");
		}
	}

	/**
	 * Importiere eine Bilddatei.
	 * 
	 * @param bilddatei
	 */
	private void importDateiA(File bilddatei) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"merkmale"));
			anzahlMerkmale = 0;
			String merkmalsKlassenname;
			while ((merkmalsKlassenname = reader.readLine()) != null) {
				Merkmal m = (Merkmal) Class.forName(merkmalsKlassenname)
						.newInstance();
				Date vorher = new Date();
				m.liesMerkmal(bilddatei);
				Date nachher = new Date();
				
				if (wertung.get(m.getName()) == null) {
					wertung.put(m.getName(), (double) (nachher.getTime() - vorher.getTime()));
				} else {
					wertung.put(m.getName(), (double) (wertung.get(m.getName()) + nachher.getTime() - vorher.getTime()));
				}
					
				anzahlMerkmale++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void importDateiB(File bilddatei) {
		// Dateiname
		Merkmal dateiname = new DateinameMerkmal();
		dateiname.liesMerkmal(bilddatei);
		System.out.println("Name: " + dateiname.getName() + "\t" + "Wert: "
				+ dateiname.getWert());

		// Dateigr��e in kB
		Merkmal dateigroesse = new DateigroesseMerkmal();
		dateigroesse.liesMerkmal(bilddatei);
		System.out.println("Name: " + dateigroesse.getName() + "\t" + "Wert: "
				+ dateigroesse.getWert());

		// Letzter Zugriff
		Merkmal letzterZugriff = new LetzterZugriffMerkmal();
		letzterZugriff.liesMerkmal(bilddatei);
		System.out.println("Name: " + letzterZugriff.getName() + "\t"
				+ "Wert: " + letzterZugriff.getWert());

		// Dateityp
		Merkmal dateityp = new DateitypMerkmal();
		dateityp.liesMerkmal(bilddatei);
		System.out.println("Name: " + dateityp.getName() + "\t" + "Wert: "
				+ dateityp.getWert());

		// Bildh�he
		Merkmal hoehe = new BildhoeheMerkmal();
		hoehe.liesMerkmal(bilddatei);
		System.out.println("Name: " + hoehe.getName() + "\t" + "Wert: "
				+ hoehe.getWert());

		// Bildbreite
		Merkmal breite = new BildbreiteMerkmal();
		breite.liesMerkmal(bilddatei);
		System.out.println("Name: " + breite.getName() + "\t" + "Wert: "
				+ breite.getWert());

		// Thumbnail
		System.out.println("Thumbnail erzeugen...");
		Merkmal thumbnail = new ThumbnailMerkmal();
		thumbnail.liesMerkmal(bilddatei);
		System.out.println("Fertig.");
	}

	public static void main(String[] args) {
		new MerkmaleTest();
	}

}
