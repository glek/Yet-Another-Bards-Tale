package yetanotherbardtale.controller.input;

import yetanotherbardtale.view.ThreeDView;

import java.awt.event.MouseEvent;
import java.awt.Component;

/**
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class ThreeDController extends InputAdapter {
    
    private ThreeDView view;
    
    public ThreeDController(ThreeDView v) {
        view = v;
    }
    
    /** Get the coordinates of a mouse click and have the view handle the
     * remainder. */
    // Plain old callbacks would be preferrable, esp. if we had curried
    // functions but that is not the Java way. */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            int sizeX, sizeY;
            sizeX = ((Component) e.getSource()).getWidth();
            sizeY = ((Component) e.getSource()).getHeight();
            double x, y;
            x = ((double) e.getX() / (double) sizeX) * 500.0;
            y = ((double) e.getY() / (double) sizeY) * 500.0;
            view.handleClickedOn((int) x, (int) y);
        }
    }
}
