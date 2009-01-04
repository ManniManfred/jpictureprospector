package jpp.merkmale;

import jpp.core.GeoeffnetesBild;
import jpp.core.exceptions.LeseMerkmalAusException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Ein Objekt der Klasse repraesentiert ein Album innerhalb des Lucene-Index
 * dar. Der Benutzer soll ueber Alben bequem innerhalb seiner Bilder
 * browsen koennen.
 */
public class GruppeMerkmal extends Merkmal {

  /** Name des Lucene-Feldes fuer dieses Merkmal. */
  public static final String FELDNAME = "Gruppe";
  
  /** Erstell ein neues Objekt der Klasse. */
  public GruppeMerkmal() {
    super(FELDNAME);
  }

  @Override
  public Field erzeugeLuceneField() {
    return new Field(FELDNAME, this.getWert().toString(), Field.Store.YES, 
        Field.Index.TOKENIZED);
  }

  @Override
  public boolean istEditierbar() {
    return true;
  }

  @Override
  public void leseMerkmalAus(GeoeffnetesBild bild)
      throws LeseMerkmalAusException {
    this.wert = "Main";
  }

  @Override
  public void leseMerkmalAusLuceneDocument(Document doc)
      throws LeseMerkmalAusException {
    this.wert = doc.getField(FELDNAME).stringValue();
  }

}
