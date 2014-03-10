package yetanotherbardtale.graphics;

import java.awt.Graphics;

/**
 * Something that can be rendered to the canvas.
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public interface Renderable {
    
    public void render(Graphics g);
    public boolean isClickInside(int x, int y);
    public void handleClickedOn(int x, int y);
}
