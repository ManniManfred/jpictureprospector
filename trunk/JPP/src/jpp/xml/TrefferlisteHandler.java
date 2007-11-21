package jpp.xml;

import jpp.client.ClientBildDokument;
import jpp.client.ClientTrefferliste;
import jpp.core.Trefferliste;
import jpp.core.exceptions.LeseMerkmalAusException;
import jpp.merkmale.Merkmal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TrefferlisteHandler extends DefaultHandler {

  private ClientTrefferliste liste;

  private ClientBildDokument currentDok;

  private Merkmal currentMerkmal;

  private String currentData;
  
  
  public TrefferlisteHandler() {
  }

  public Trefferliste getTrefferliste() {
    return liste;
  }


  @Override
  public void startElement(String namespaceURI, String localName, String qName,
      Attributes atts) throws SAXException {
    if (qName.equals("Trefferliste")) {
      liste = new ClientTrefferliste();
      liste.setGesamtAnzahlTreffer(Integer.parseInt(atts.getValue("Anzahl")));
    } else if (qName.equals("BildDokument")) {

      currentDok = new ClientBildDokument();

      if (liste == null) {
        throw new SAXException("Die XML ist nicht gültig.");
      }

      // @TODO score muss noch aus der XML geholt werden
      liste.addBildDokument(currentDok, 1);

    } else {
      currentData = "";
      Class klasse;
      try {
        klasse = Class.forName(qName);
  
  
        if (klasse == null) {
          System.out.println("Konnte Klasse " + qName + " nicht finden");
        } else {
          currentMerkmal = (Merkmal) klasse.newInstance();
        }
  
        if (currentDok == null) {
          throw new SAXException("Die XML ist nicht gültig.");
        }
        currentDok.addMerkmal(currentMerkmal);
        
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InstantiationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    }

  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) 
    throws SAXException {
    if (!qName.equals("Trefferliste") && !qName.equals("BildDokument")) {
      try {
        currentMerkmal.leseMerkmalAusString(currentData);
      } catch (LeseMerkmalAusException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      currentData = null;
    }
  }
  
  @Override
  public void characters(char[] ch, int start, int length) {
    String data = new String(ch, start, length);
    
    if (!data.equals("") && !data.equals("\n")) {
      currentData += data;
    }
    
  }


}
