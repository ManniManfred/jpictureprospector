package jpp.components;

import java.util.EventObject;


/**
 *
 * @author Manfred Rosskamp
 */
public class ChangedValueEvent extends EventObject {
    
    /**
     * Holds value of property oldValue.
     */
    private Object oldValue;
    
    /**
     * Holds value of property newValue.
     */
    private Object newValue;

    /**
     * Erzeugt ein neues Object aus ChangedValueEvent
     * @param source Quelle, die dieses Event ausgelöst hat
     * @param oldValue vorheriger alter Wert
     * @param newValue neuer Wert
     */
    public ChangedValueEvent(Object source, Object oldValue, Object newValue) {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Getter for property oldValue.
     * @return Value of property oldValue.
     */
    public Object getOldValue() {
        return this.oldValue;
    }

    /**
     * Getter for property newValue.
     * @return Value of property newValue.
     */
    public Object getNewValue() {
        return this.newValue;
    }
    
}
