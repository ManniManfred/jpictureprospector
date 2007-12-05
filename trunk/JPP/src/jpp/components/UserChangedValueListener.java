package jpp.components;


/**
 * Dieses Interface stellt ein Listener dar, der dann aufgerufen wird, wenn
 * der User eine Wert durch irgendwelche Interaktionen geändert hat.
 * @author Manfred Rosskamp
 */
public interface UserChangedValueListener extends java.util.EventListener {
    
    /**
     * Wird aufgerufen, wenn der Benutzter den Wert einer Componente durch
     * irgendwelche Interaktionen geändert hat.
     * @param e  ChangedValueEvent, das ausgelöst wurde
     */
    public void userChangedValue(ChangedValueEvent e);
}
