package benutzermanager;

import java.util.HashMap;
import java.util.Map;

import benutzermanager.exceptions.RechtExistiertNichtException;



/**
 * Ein Objekt dieser Klasse stellt einen Benutzer dar.
 * 
 * @author Manfred Rosskamp
 */
public class Benutzer {

    
    private String loginName;
    private String passwort;
    
    
    private String nachname;
    private String vorname;
    private String email;
    
    private Map<String, Recht> rechte = new HashMap<String, Recht>();
    
    public Benutzer(String loginName) {
      this.loginName = loginName;
    }
    

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "loginName=" + loginName + ","
                + "Vorname=" + vorname + ","
                + "Nachname=" + nachname + ","
                + "email=" + email + ","
                + "passwort=" + passwort + ","
                + "rechte=" + rechte;
    }


    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginName != null ? loginName.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this Benutzer.  The result is 
     * <code>true</code> if and only if the argument is not null and is a Benutzer object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Benutzer)) {
            return false;
        }
        Benutzer other = (Benutzer)object;
        if (loginName != other.loginName && (this.loginName == null || !this.loginName.equals(other.loginName))) return false;
        return true;
    }

    /**
     * Raeumt diesem Benutzer ein Recht ein. Dieses Recht muss aber der
     * Applikation bekannt sein und zwar im RechteManager.
     * 
     * @param recht Recht, welches der Benutzer nach dieser Anweisung hat
     * @throws RechtExistiertNichtException wird geworfen, wenn das Recht nicht
     *  verfuegbar ist
     */
    public void addRecht(Recht recht) throws RechtExistiertNichtException {
      if (recht != null && RechteManager.existiert(recht)) {
        rechte.put(recht.getName(), recht);
      } else {
        throw new RechtExistiertNichtException(recht);
      }
    }
    
    /**
     * Nimmt diesem Benutzer ein Recht wieder weg.
     * 
     * @param recht Recht, welches dem Benutzer weggenommen wird
     */
    public void removeRecht(Recht recht) {
      rechte.remove(recht.getName());
    }
    
    /**
     * Ueberprueft, ob dieser Benutzer ein bestimmtes Recht hat.
     * 
     * @param recht Das Recht auf das dieser Benutzer geprueft wird
     * @return true, wenn dieser Benutzer das uebergebene Recht hat
     */
    public boolean hatRecht(Recht recht) {
      return rechte.containsKey(recht.getName());
    }
    
    
    
    
    
    /**
     * @return the passwort
     */
    public String getPasswort() {
      return passwort;
    }


    /**
     * @param passwort the passwort to set
     */
    public void setPasswort(String passwort) {
      this.passwort = passwort;
    }


    /**
     * @return the nachname
     */
    public String getNachname() {
      return nachname;
    }


    /**
     * @param nachname the nachname to set
     */
    public void setNachname(String nachname) {
      this.nachname = nachname;
    }


    /**
     * @return the vorname
     */
    public String getVorname() {
      return vorname;
    }


    /**
     * @param vorname the vorname to set
     */
    public void setVorname(String vorname) {
      this.vorname = vorname;
    }


    /**
     * @return the loginName
     */
    public String getLoginName() {
      return loginName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}
