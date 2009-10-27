package jpp.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ErzeugeBildDokumentException;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.DateipfadMerkmal;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.SAXException;

/**
 * Ein Objekt dieser Klasse stellt die Hauptaufgaben dieser JPictureProspector
 * Anwendung bereit. Diese Klasse ist ein Singelton.
 * 
 * @author Manfred Rosskamp
 */
public class LuceneJPPCore extends AbstractJPPCore {

	private static final String ALBUMDATEINAME = "/alben.xml";

	/** Logger, der alle Fehler loggt. */
	Logger logger = Logger.getLogger("jpp.core.LuceneJPPCore");

	/** Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene. */
	private String indexDir;

	/** Enthaelt den Parser, der alle Such-Anfragen in eine Query parst. */
	private MultiFieldQueryParser parser;

	/**
	 * Enthaelt eine Zuordnung von der Gruppe zu einer Menge an dazugeörigen
	 * Alben.
	 */
	private Map<String, Set<String>> alben;

	/** Enthält alle Alben. */
	private Set<String> alleAlben = new HashSet<String>();

	/**
	 * Erstellt ein neues JPPCore-Objekt.
	 * 
	 * @param indexDir
	 *            Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene
	 * @throws ErzeugeException
	 *             wird geworfen, falls beim Erzeugen dieses Objektes ein Fehler
	 *             auftritt. Z.B wenn die Merkmalsdatei nicht existiert, oder
	 *             die Klassen zu den Merkmals-Klassennamen nicht gefunden
	 *             wurden.
	 */
	public LuceneJPPCore(String indexDir) throws ErzeugeException {
		super();

		this.indexDir = indexDir;

		/* Namen aller moeglichen Merkmale herausfinden */
		String[] namen = null;
		try {
			namen = getMerkmalsnamen();
		} catch (Exception e) {
			throw new ErzeugeException("Konnte die Merkmale nicht erzeugen.", e);
		}

		/*
		 * MultiFieldQueryParser erzeugen, der über alle Felder sucht und den
		 * GermanAnalyzer verwendet.
		 */
		parser = new MultiFieldQueryParser(namen, new GermanAnalyzer());

		logger.log(Level.INFO, "Lese alle Alben aus der Albendatei: "
				+ indexDir + ALBUMDATEINAME);
		readAlben();

		// this.alben = getAlbenFromLuceneDocuments(null);
		// readAlbenFromLuceneDocuments();
	}

	private void readAlbenFromLuceneDocuments() {
		this.alben = new HashMap<String, Set<String>>();
		try {
			/* Enthaelt alle Dokumente des Index */
			List<BildDokument> doks = gibAlleDokumente();

			for (BildDokument dok : doks) {
				String album = dok.getMerkmal("Album").getWert().toString();
				String gruppe = dok.getMerkmal("Gruppe").getWert().toString();

				this.addAlbumZurGruppe(album, gruppe);
			}

		} catch (IOException e) {
			logger.log(Level.WARNING, "Konnte den Index nicht \u00d6ffnen.", e);
		} catch (ErzeugeBildDokumentException e) {
			logger.log(Level.WARNING,
					"Die Trefferliste konnte nicht erzeugt werden.", e);
		}

	}

	private void addAlbumZurGruppe(String album, String gruppe) {
		Set<String> albenZurGruppe = this.alben.get(gruppe);
		if (albenZurGruppe == null) {
			albenZurGruppe = new HashSet<String>();
			this.alben.put(gruppe, albenZurGruppe);
		}

		alleAlben.add(album);
		albenZurGruppe.add(album);
		this.writeAlben();
	}

	private org.w3c.dom.Document parseXmlFile(String file) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		org.w3c.dom.Document result = null;
		String errorMessage = "Albumdatei konnte nicht geparst werden.";
		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			result = db.parse(file);

		} catch (ParserConfigurationException pce) {
			logger.log(Level.WARNING, errorMessage, pce);
		} catch (SAXException se) {
			logger.log(Level.WARNING, errorMessage, se);
		} catch (IOException ioe) {
			logger.log(Level.WARNING, errorMessage, ioe);
		}
		return result;
	}

	/**
	 * Using JAXP in implementation independent manner create a document object
	 * using which we create a xml tree in memory
	 */
	private org.w3c.dom.Document createDocument() {
		org.w3c.dom.Document dom;
		// get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// create an instance of DOM
			dom = db.newDocument();

		} catch (ParserConfigurationException pce) {
			// dump it
			logger.log(Level.WARNING,
					"Error while trying to instantiate DocumentBuilder " + pce);
			dom = null;
		}

		return dom;
	}

	/**
	 * This method uses Xerces specific classes prints the XML document to file.
	 */
	private void printToFile(org.w3c.dom.Document dom, String file) {

		try {
			// print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			// to generate output to console use this serializer
			// XMLSerializer serializer = new XMLSerializer(System.out, format);

			// to generate a file output use fileoutputstream instead of
			// system.out
			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
					new File(file)), format);

			serializer.serialize(dom);

		} catch (IOException ie) {
			logger.log(Level.WARNING,
					"Ablumdatei konnte nicht erstellt werden.", ie);
		}
	}

	private void writeAlben() {
		org.w3c.dom.Document dom = createDocument();

		// create the root element
		org.w3c.dom.Element rootEle = dom.createElement("Document");
		dom.appendChild(rootEle);

		for (Map.Entry<String, Set<String>> e : alben.entrySet()) {
			// System.out.println(e.getKey() + ": " + e.getValue());
			org.w3c.dom.Element groupNode = dom.createElement("Gruppe");
			groupNode.setAttribute("Name", e.getKey());
			rootEle.appendChild(groupNode);

			for (String album : e.getValue()) {
				org.w3c.dom.Element albumNode = dom.createElement("Album");
				albumNode.setAttribute("Name", album);

				groupNode.appendChild(albumNode);
			}
		}

		printToFile(dom, indexDir + ALBUMDATEINAME);
	}

	/**
	 * Lade alle Alben aus der Albendatei.
	 */
	private void readAlben() {

		alleAlben = new HashSet<String>();
		alben = new HashMap<String, Set<String>>();
		org.w3c.dom.Document dom = parseXmlFile(indexDir + ALBUMDATEINAME);

		if (dom != null) {
			// get the root element
			org.w3c.dom.Element docEle = dom.getDocumentElement();

			// read alben
			org.w3c.dom.NodeList nl = docEle.getElementsByTagName("Gruppe");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {

					// get the group element
					org.w3c.dom.Element el = (org.w3c.dom.Element) nl.item(i);

					// get the name of group
					String groupName = el.getAttribute("Name");
					HashSet<String> albenSet = new HashSet<String>();

					// get all alben from group node
					org.w3c.dom.NodeList albumNodeList = el
							.getElementsByTagName("Album");
					if (albumNodeList != null && albumNodeList.getLength() > 0) {

						for (int j = 0; j < albumNodeList.getLength(); j++) {
							// get the album element
							org.w3c.dom.Element albumNode = (org.w3c.dom.Element) albumNodeList
									.item(j);

							String albumName = albumNode.getAttribute("Name");
							alleAlben.add(albumName);
							albenSet.add(albumName);
						}
					}

					alben.put(groupName, albenSet);
				}
			}
		}

		// try {
		// BufferedReader reader = new BufferedReader(
		// new FileReader(indexDir + ALBUMDATEINAME));
		//      
		//      
		// this.alben = new HashSet<String>();
		//
		// org.
		// String line;
		// while (null != (line = reader.readLine())) {
		// this.alben.put(key, value)
		// }

		// this.alben = new HashSet<String>(Arrays.asList(line.split(";")));

		// TODO: read file

		// reader.close();
		// } catch (FileNotFoundException e) {
		// logger.log(Level.INFO, "Albendatei \"" + indexDir + ALBUMDATEINAME
		// + "\" wurde nicht gefunden.");
		// alben = new HashMap<String, Set<String>>();
		// } catch (IOException e) {
		// // TODO: weitere Fehlerbehandlung (fehlerausgabe oder ähnliches)
		// logger.log(Level.INFO, "Albendatei \"" + indexDir + ALBUMDATEINAME
		// + "\" wurde nicht gefunden.");
		// alben = new HashMap<String, Set<String>>();
		// }
	}

	/**
	 * Speichert alle Alben in der Albendatei.
	 */
	private void saveAlben() {
		/*
		 * try { FileWriter writer = new FileWriter(indexDir + ALBUMDATEINAME);
		 * 
		 * Iterator<String> iter = alben.iterator();
		 * 
		 * while (iter.hasNext()) { String album = iter.next();
		 * writer.write(album);
		 * 
		 * if (iter.hasNext()) { writer.write(";"); } }
		 * 
		 * writer.close(); } catch (IOException e) { // TODO: weitere
		 * Fehlerbehandlung (fehlerausgabe oder ähnliches) }
		 */
	}

	/**
	 * Importiert eine Bilddatei in diese Anwendung.
	 * 
	 * @param datei
	 *            Bilddatei, die importiert werden soll
	 * @return die importierte Bilddatei als BildDokument
	 * @throws ImportException
	 *             wird geworfen, wenn das Importieren der Bilddatei nicht
	 *             funktioniert, z.B. wenn das Bild schon importiert wurde.
	 */
	public void importiere(URL datei) throws ImportException {

		/*
		 * ueberpruefen, ob das Bild bereits im Index vorhanden ist. Wenn ja,
		 * dann eine ImportException werfen.
		 */
		if (istDateiImIndex(datei)) {
			throw new ImportException("Die Datei \"" + datei
					+ "\" wurde bereits importiert.");
		}

		/* Lasse das BildDokument aus der Datei erzeugen */
		BildDokument dokument;
		try {
			dokument = BildDokument.erzeugeAusDatei(datei);
		} catch (ErzeugeBildDokumentException e1) {
			throw new ImportException(
					"Konnte kein BildDokument aus der Datei \"" + datei
							+ "\" erzeugen.", e1);
		}

		importInLucene(dokument);
	}

	/**
	 * Fuegt das uebergebenen BildDokument dem Luceneindex hinzu.
	 * 
	 * @param dokument
	 *            BildDokument, welches in den Lucene-Index aufgenommen werden
	 *            soll
	 * @throws ImportException
	 *             wird geworfen, wenn das Bild nicht in den Lucene -Index
	 *             aufgenommen werden konnte
	 */
	private void importInLucene(BildDokument dokument) throws ImportException {

		/*
		 * Wird verwendet, um Text vorzubearbeiten. Z.B werden alle Woerter
		 * (Tokens) in klein Buchstaben umgewandelt, oder es werden triviale
		 * Woerter weggelassen.
		 */
		Analyzer analyzer = new GermanAnalyzer();

		try {
			/* Erzeuge einen IndexWriter, der den bestehenden Index verwendet. */
			IndexWriter writer = new IndexWriter(indexDir, analyzer);

			/* BildDokument dem Lucene-Index hinzufuegen */
			Document doc = dokument.erzeugeLuceneDocument();
			writer.addDocument(doc);

			/* IndexWriter schliessen */
			writer.close();
		} catch (IOException e) {
			throw new ImportException(
					"Es konnte der Lucene-Index nicht erstellt " + "werden", e);
		}

		/*
		 * Alben liste updaten. TODO: Damit nicht mehr vorhandene Alben auch
		 * ohne Neustart aus dem Cache entfernet werden, könnte man einen Zähler
		 * für jedes Album hinzufügen, der angibt wieviele Dokumente in dem
		 * Album sind.
		 */
		String album = dokument.getMerkmal("Album").getWert().toString();
		String gruppe = dokument.getMerkmal("Gruppe").getWert().toString();

		this.addAlbumZurGruppe(album, gruppe);
	}

	/**
	 * Gibt eine Liste mit allen BildDokumenten zurueck, die im Index dieser
	 * Anwendung vorhanden sind.
	 * 
	 * @return Liste mit allen BildDokumenten, die im Index dieser Anwendung
	 *         vorhanden sind.
	 * @throws IOException
	 *             wird geworfen, wenn der Index nicht geoeffnet oder die
	 *             Documents nicht gelesen werden konnten
	 * @throws ErzeugeBildDokumentException
	 *             wird geworfen, wenn aus dem Lucene-Document kein BildDokument
	 *             erzeugt werden konnte
	 */
	private List<BildDokument> gibAlleDokumente()
			throws ErzeugeBildDokumentException, IOException {
		List<BildDokument> ergebnis;
		IndexReader reader = IndexReader.open(indexDir);

		int anzahl = reader.maxDoc();
		ergebnis = new ArrayList<BildDokument>(anzahl);

		for (int i = 0; i < anzahl; i++) {
			/*
			 * Die Documente, die als geloescht markiert sind nicht mehr
			 * auslesen.
			 */
			if (!reader.isDeleted(i)) {
				ergebnis.add(BildDokument.erzeugeAusLucene(reader.document(i)));
			}
		}

		reader.close();
		return ergebnis;
	}

	/**
	 * Gibt das BildDokument zu der entsprechenden URL der Bilddatei zurueck.
	 * 
	 * @param datei
	 *            URL zu der das BildDokument zurueckgegeben werden soll
	 * @return BildDokument zu der entsprechenden URL der Bilddatei oder null,
	 *         falls dieses BildDokument keinmal oder mehr als einmal im Index
	 *         befindet.
	 * @throws ErzeugeBildDokumentException
	 */
	public BildDokument getBildDokument(URL datei)
			throws ErzeugeBildDokumentException {
		try {
			Searcher sucher = new IndexSearcher(indexDir);

			/* Nach dem Dateipfad suchen */
			Hits treffer = sucher.search(new TermQuery(new Term(
					DateipfadMerkmal.FELDNAME, datei.toString())));

			/*
			 * Falls mindestens ein Treffer erzielt wurde, befindet sich die
			 * Datei bereits im Index.
			 */
			if (treffer.length() == 1) {
				return BildDokument.erzeugeAusLucene(treffer.doc(0));
			}

			sucher.close();

		} catch (IOException e) {
			/*
			 * Wenn z.B. der Index noch nicht vorhanden ist, befindet sich die
			 * Datei auch noch nicht im Index. Deshalb wird hier false
			 * zurueckgegeben.
			 */
		}
		return null;
	}

	/**
	 * Gibt zurueck, ob sich die uebergebene Datei bereits im Index befindet.
	 * 
	 * @param datei
	 *            Datei, die ueberprueft wird
	 * @return true, falls die Datei im Index vorhanden ist
	 */
	private boolean istDateiImIndex(URL datei) {

		try {
			Searcher sucher = new IndexSearcher(indexDir);

			/* Nach dem Dateipfad suchen */
			Hits treffer = sucher.search(new TermQuery(new Term(
					DateipfadMerkmal.FELDNAME, datei.toString())));
			sucher.close();

			/*
			 * Falls mindestens ein Treffer erzielt wurde, befindet sich die
			 * Datei bereits im Index.
			 */
			return treffer.length() > 0;

		} catch (IOException e) {
			/*
			 * Wenn z.B. der Index noch nicht vorhanden ist, befindet sich die
			 * Datei auch noch nicht im Index. Deshalb wird hier false
			 * zurueckgegeben.
			 */
			return false;
		}
	}

	/**
	 * Suche in allen importierten Bilder nach dem Suchtext und gibt eine
	 * entsprechende Trefferliste mit den Suchergebnissen zurueck.
	 * 
	 * @param suchtext
	 *            Suchtext, nach dem gesucht wird
	 * @param offset
	 *            Nummer des Bilddokumentes aller Treffer, ab der die Treffer in
	 *            der Trefferliste aufgenommen werden sollen
	 * @param maxanzahl
	 *            Anzahl der Bilddokumente, die maximal in der Trefferliste
	 *            aufgenommen werden sollen
	 * @return Trefferliste mit den Suchergebnissen
	 * @throws SucheException
	 *             wird geworfen, wenn die Suche nicht erfolgreich mit einer
	 *             erzeugten Trefferliste beendet werden kann
	 */
	public Trefferliste suche(String suchtext, int offset, int maxanzahl)
			throws SucheException {

		Trefferliste treffer;
		try {
			/* wenn das Schluesselwort zur Anzeige aller Bilder angegeben wurde, */
			if (suchtext.equalsIgnoreCase(coreSettings.ALLEBILDER_SCHLUESSEL)) {

				/* dann alle BildDokumente der Trefferliste uebergeben. */
				treffer = gibAlleDokumentAdv(offset, maxanzahl);
				// List<BildDokument> doks = gibAlleDokumente();
				// treffer = new Trefferliste(doks, offset, maxanzahl);
			} else {

				/* Anfrage aufbauen */
				Query anfrage = parser.parse(suchtext);

				/* Suche durchfuehren */
				Searcher sucher = new IndexSearcher(indexDir);
				treffer = new Trefferliste(sucher.search(anfrage), offset,
						maxanzahl);
				sucher.close();
			}

		} catch (ParseException e) {
			/* Bei einer ParseException ein leere Trefferliste zurueckgeben */
			// TODO Aussage verbessern
			throw new SucheException("Die Anfrage ist nicht korrekt.", e);
		} catch (IOException e) {
			throw new SucheException("Konnte den Index nicht \u00d6ffnen.", e);
		} catch (ErzeugeException e) {
			throw new SucheException(
					"Die Trefferliste konnte nicht erzeugt werden.", e);
		} catch (ErzeugeBildDokumentException e) {
			throw new SucheException(
					"Das BildDokument konnte nicht erzeugt werden.", e);
		}

		/* Trefferliste aus dem Lucene SuchErgebnis erzeugen und zurueckgeben */
		return treffer;
	}

	/**
	 * Uebernimmt die Aenderungen, die an dem BildDokument gemacht wurden, wie
	 * z.B. das Hinzufuegen von Schluesselwoerter.
	 * 
	 * @param bild
	 *            Bilddokument, von dem die Aenderungen uebernommen werden
	 * @throws AendereException
	 *             wird geworfen, wenn das Aendern nicht funktioniert hat
	 */
	public void aendere(BildDokument bild) throws AendereException {
		/* BildDokument durch entfernen und neu hinzufuegen aendern */
		try {
			entferne(bild, false);
			importInLucene(bild);
		} catch (EntferneException e) {
			throw new AendereException(
					"\u00c4ndern hat nicht funktioniert da beim "
							+ "Entfernen ein Fehler auftrat.", e);
		} catch (ImportException e) {
			throw new AendereException("\u00c4ndern hat nicht funktioniert.", e);
		}
	}

	/**
	 * Entfernt die entsprechende Datei aus dem Index
	 * 
	 * @param datei
	 *            Datei, welches entfernt werden soll
	 * @param auchVonFestplatte
	 *            gibt an, ob das Bild auch von der Festplatte entfernt werden
	 *            soll
	 * @throws EntferneException
	 *             wird geworfen, wenn das Entfernen des BildDokuments
	 *             fehlgeschlagen ist
	 */
	public void entferne(URL datei, boolean auchVonFestplatte)
			throws EntferneException {

		/* BildDokument aus dem Index entfernen */
		IndexReader reader;
		try {
			reader = IndexReader.open(indexDir);
			reader.deleteDocuments(new Term(DateipfadMerkmal.FELDNAME, datei
					.toString()));
			reader.close();
		} catch (IOException e) {
			throw new EntferneException(
					"Konnte das BildDokument nicht entfernen.");
		}

		/*
		 * Wenn das Flag auchVonFestplatte gesetzt ist, dann entferne die
		 * Bilddatei von der Festplatte.
		 */
		if (auchVonFestplatte && datei.getProtocol().equalsIgnoreCase("file")) {
			File zuEntfernen = new File(datei.getPath());
			if (!zuEntfernen.delete()) {
				/* Fehlerbehandlung, falls das Loeschen misslang */
				throw new EntferneException(
						"Das Entfernen von der Festplatte misslang.");
			}
		}
	}

	/**
	 * Gibt eine Liste aller im Index vorhandenen Alben zurück.
	 * 
	 * @return eine Liste aller Alben
	 */
	public List<String> getAlben(String gruppe) {

		Set<String> albenZurGruppe;
		if (gruppe == null) {
			albenZurGruppe = this.alleAlben;
		} else {
			albenZurGruppe = this.alben.get(gruppe);
		}

		if (albenZurGruppe == null) {
			logger.log(Level.INFO, "Alben zur Gruppe \"" + gruppe
					+ "\" konnten nicht gefunden werden.");
			return new ArrayList<String>();
		}

		return Arrays.asList(albenZurGruppe.toArray(new String[0]));
	}

	public void rotate(BildDokument dok, double degree) {
		// @TODO: implement
	}

	/**
	 * List die Alben aus allen Lucene-Dokumenten.
	 * 
	 * @param gruppe
	 * @return
	 */
	private Set<String> getAlbenFromLuceneDocuments(String gruppe) {
		List<BildDokument> doks;
		Set<String> ergebnis;

		try {
			if (gruppe == null) {
				/* Enthaelt alle Dokumente des Index */
				doks = gibAlleDokumente();
			} else {
				Trefferliste treffer = suche("Gruppe:\"" + gruppe + "\"", 0,
						Integer.MAX_VALUE);
				doks = treffer.bildDokumente;
			}

			ergebnis = new HashSet<String>();
			for (BildDokument dok : doks) {
				ergebnis.add(dok.getMerkmal("Album").getWert().toString());
			}

		} catch (IOException e) {
			logger.log(Level.WARNING, "Konnte den Index nicht \u00d6ffnen.", e);
			ergebnis = new HashSet<String>();
		} catch (ErzeugeBildDokumentException e) {
			logger.log(Level.WARNING,
					"Die Trefferliste konnte nicht erzeugt werden.", e);
			ergebnis = new HashSet<String>();
		} catch (SucheException e) {
			logger.log(Level.WARNING,
					"Fehler bei der Suche nach allen Dokumenten"
							+ " der Gruppe " + gruppe + ".", e);
			ergebnis = new HashSet<String>();
		}

		return ergebnis;
	}

	/**
	 * Loescht alle Dokumente, die nicht mehr auf den Festplatte vorhanden sind
	 * aus dem Index.
	 * 
	 * @return Meldungen, welche Dokumente aus dem Index entfernt wurden
	 */
	public String clearUpIndex() throws SucheException {
		String meldung = "";

		try {
			/* Enthaelt alle Dokumente des Index */
			List<BildDokument> alleDoks = gibAlleDokumente();

			for (BildDokument dok : alleDoks) {

				/* Enthaelt den kompletten Dateipfad des BildDokumentes */
				URL pfad = (URL) dok.getMerkmal(DateipfadMerkmal.FELDNAME)
						.getWert();

				/*
				 * Wenn die Datei nicht existiert wird Sie aus dem Index
				 * entfernt
				 */
				if (!urlExists(pfad)) {
					try {
						entferne(dok, false);
						meldung += "BildDokument " + pfad
								+ " wurde entfernt.\n";
					} catch (EntferneException e) {
						meldung += "BildDokument " + pfad
								+ " konnte nicht entfernt werden." + e + "\n";
					}
				}
			}
		} catch (IOException e) {
			throw new SucheException("Konnte den Index nicht \u00d6ffnen.", e);
		} catch (ErzeugeBildDokumentException e) {
			throw new SucheException(
					"Die Trefferliste konnte nicht erzeugt werden.", e);
		}
		return meldung;
	}

	/**
	 * Prueft, ob eine angegeben URL-Datei noch existiert.
	 * 
	 * @param url
	 *            Url die geprueft wird
	 * @return true, wenn diese URL noch existiert
	 */
	private static boolean urlExists(URL url) {
		try {
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false)
			return url.getContent() != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private Trefferliste gibAlleDokumentAdv(int offset, int maxanzahl)
			throws IOException, ErzeugeBildDokumentException {

		List<BildDokument> ergebnis;
		IndexReader reader = IndexReader.open(indexDir);

		int anzahl = reader.maxDoc();
		ergebnis = new ArrayList<BildDokument>(anzahl);

		int anzahlBilder = 0;

		for (int i = 0; i < anzahl && (anzahlBilder < offset + maxanzahl); i++) {
			/*
			 * Die Documente, die als geloescht markiert sind nicht mehr
			 * auslesen.
			 */
			if (!reader.isDeleted(i)) {
				if (anzahlBilder >= offset) {
					ergebnis.add(BildDokument.erzeugeAusLucene(reader
							.document(i)));
				}
				anzahlBilder++;
			}
		}
		reader.close();
		return new Trefferliste(ergebnis, anzahl);
	}
}
