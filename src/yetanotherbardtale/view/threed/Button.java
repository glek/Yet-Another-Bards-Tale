package yetanotherbardtale.view.threed;

import yetanotherbardtale.imagelib.ImageLibrary;
import yetanotherbardtale.graphics.Renderable;

import java.awt.Image;
import java.awt.Graphics;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class Button implements Renderable {
    
    private Image img;
    private int x, y, scaleX, scaleY;
    
    public Button(String img, int x, int y, int scaleX, int scaleY, ImageLibrary lib) {
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        if(img != null && !img.isEmpty()) {
            if(lib.contains(img)) {
                this.img = lib.getImage(img);
            }
            else {
                this.img = ImageLibrary.generateDefaultImage();
            }
        }
        else {
            this.img = ImageLibrary.generateDefaultImage();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, x, y, scaleX, scaleY, null);
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return ((x > this.x && x < this.x + scaleX) &&
                (y > this.y && y < this.y + scaleY));
    }

    @Override
    public void handleClickedOn(int x, int y) {
        //Do nothing, this is handled by the parent
    }
}
