package jpp.components;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


/**
 * Diese Klasse erweiter die normale JComboBox um einen 
 * UserChangedValueListener.
 * @author Manfred Rosskamp
 */
public class MyComboBox extends JComboBox {
    
    /**
     * Utility field holding list of UserChangedValueListeners.
     */
    private transient java.util.ArrayList userChangedValueListenerList;
    
    
    private boolean popupCanceled;
    
    private int selectedIndex;
    
    
     /**
     * Creates a <code>JComboBox</code> that takes it's items from an
     * existing <code>ComboBoxModel</code>.  Since the
     * <code>ComboBoxModel</code> is provided, a combo box created using
     * this constructor does not create a default combo box model and
     * may impact how the insert, remove and add methods behave.
     *
     * @param aModel the <code>ComboBoxModel</code> that provides the 
     * 		displayed list of items
     * @see DefaultComboBoxModel
     */
    public MyComboBox(ComboBoxModel aModel) {
        super(aModel);
        init();
    }

    /** 
     * Creates a <code>JComboBox</code> that contains the elements
     * in the specified array.  By default the first item in the array
     * (and therefore the data model) becomes selected.
     *
     * @param items  an array of objects to insert into the combo box
     * @see DefaultComboBoxModel
     */
    public MyComboBox(final Object items[]) {
        super(items);
        init();
    }

    /**
     * Creates a <code>JComboBox</code> that contains the elements
     * in the specified Vector.  By default the first item in the vector
     * and therefore the data model) becomes selected.
     *
     * @param items  an array of vectors to insert into the combo box
     * @see DefaultComboBoxModel
     */
    public MyComboBox(Vector<?> items) {
        super(items);
        init();
    }

    /**
     * Erzeugt ein neues Object aus MyComboBox
     */
    public MyComboBox() {
        init();
    }
    
    /**
     * initialisiert dieses MyComboBox-Object
     */
    private void init() {
        popupCanceled = false;
        
        this.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent e) {
                canceled();
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                invisible();
            }
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                visible();
            }
        });
    }
    
    /**
     * Wird aufgerufen, wenn das popupMenuCanceled-Event eintritt.
     */
    private void canceled() {
        popupCanceled = true;
    }
    
    /**
     * Wird aufgerufen, wenn das popupMenuWillBecomeInvisible-Event eintritt.
     */
    private void invisible() {
        if (!popupCanceled && this.selectedIndex != getSelectedIndex()) {
            invokeUserChangedValueListener();
        } else {
            this.setSelectedIndex(this.selectedIndex);
        }
        popupCanceled = false;
    }
    
    /**
     * Wird aufgerufen, wenn das popupMenuWillBecomeVisible-Event eintritt.
     */
    private void visible() {
        selectedIndex = getSelectedIndex();
    }
    
    /**
     * Informiert alle Listener darüber, dass sich etwas
     * geändert hat.
     */
    private void invokeUserChangedValueListener() {
        Object oldValue = this.getItemAt(selectedIndex);
        Object newValue = this.getSelectedItem();
        fireUserChangedValueListenerUserChangedValue(
                new ChangedValueEvent(this, oldValue, newValue));
    }
    
    

    /**
     * Registers UserChangedValueListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addUserChangedValueListener(
            jpp.components.UserChangedValueListener listener) {
        if (userChangedValueListenerList == null ) {
            userChangedValueListenerList = new java.util.ArrayList ();
        }
        userChangedValueListenerList.add (listener);
    }

    /**
     * Removes UserChangedValueListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeUserChangedValueListener(
          jpp.components.UserChangedValueListener listener) {
        if (userChangedValueListenerList != null ) {
            userChangedValueListenerList.remove (listener);
        }
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireUserChangedValueListenerUserChangedValue(
            jpp.components.ChangedValueEvent event) {
        java.util.ArrayList list;
        synchronized (this) {
            if (userChangedValueListenerList == null) return;
            list = (java.util.ArrayList)userChangedValueListenerList.clone();
        }
        System.out.println(event.getOldValue());
        System.out.println(event.getNewValue());
        for (int i = 0; i < list.size (); i++) {
            ((jpp.components.UserChangedValueListener)list.get (i))
                .userChangedValue(event);
        }
    }



    
}
