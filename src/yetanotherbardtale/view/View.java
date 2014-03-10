package yetanotherbardtale.view;

import java.util.Observer;

/**
 * Anything that can display standard or error text messages.
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public interface View extends Observer {
    public void displayText(String msg);
    public void displayError(String msg);
    
}
