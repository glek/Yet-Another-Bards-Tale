package yetanotherbardtale.view;

import java.util.Observable;

/**
 * A simple text view for the game.
 *
 * Prints everything to stdout.
 *
 * @author G'lek
 * @version 0.1.0 
 */
public class TextView implements View {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg.toString());        
    }

    @Override
    public void displayText(String msg) {
        System.out.println(msg);
    }

    @Override
    public void displayError(String msg) {
        System.err.println(msg);
    }
}
