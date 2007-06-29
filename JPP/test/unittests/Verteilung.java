package unittests;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author Manfred Rosskamp
 */
public class Verteilung {
	
	/** Name dieser Verteilung, z.B "Schicht 3" Verteilung. */
	private String name;
	
	/** 
	 * Zuordung, von einer Auspraegung, z.B TCP, UDP oder ICMP, zu der
	 * Haeufigkeit der Auspraegung.
	 */
	private Map<String, Integer> counter;
	
	
	/** Summe ueber alle Auspraegungen. */
	private int summe = 0;
	
	/**
	 * Erzeugt eine neue Verteilung
	 * @param name
	 */
	public Verteilung(String name) {
		this.name = name;
		counter = new HashMap<String, Integer>();
	}

	
	/**
	 * Erhoeht den Zaehler der uebergebnen Auspraegung, wie z.B. von TCP
	 * @param auspraegung
	 */
	public void erhoehe(String auspraegung) {
		/* Aktuelle Zaehler zur Auspraegung holen */
    	Integer count = counter.get(auspraegung);
    	if (count == null) {
    		count = 0; 
    	}
    	
    	/* und um einen erhoeht setzten */
    	counter.put(auspraegung, count + 1);
    	
    	/* Gesamtzahl bzw. Summe erhoehen. */
    	summe++;
	}
	
  public void setze(String auspraegung, int wert) {
    /* Aktuelle Zaehler zur Auspraegung holen */
    Integer count = counter.get(auspraegung);
    if (count == null) {
      count = 0; 
    }
    summe -= count;
    
    /* und um einen erhoeht setzten */
    counter.put(auspraegung, wert);
    
    /* Gesamtzahl bzw. Summe erhoehen. */
    summe += wert;
  }
  
	/**
	 * Setzt alle Zaehler wieder auf 0 und beginnt somit von vorne.
	 */
	public void reset() {
		counter = new HashMap<String, Integer>();
		summe = 0;
	}
	
	public String toString() {
		/* Allgemeine Ausgaben zur Verteilung */
		String ergebnis = " ---- " + name
				+ " (" + summe + ") -----\n";
		
		ergebnis += "                         ms          Verteilung\n";
		
		/* Einzelne Verteilung der Protokolle */
		for (String protokollName : counter.keySet()) {
			int anzahl = counter.get(protokollName);
			ergebnis += String.format("%1$22s: %2$10d %3$10d " , 
    				protokollName,
    				anzahl,
    				anzahl * 100 / summe);
    		ergebnis += "\u0025\n";
    	}
		
		return ergebnis;
	}
	
}
