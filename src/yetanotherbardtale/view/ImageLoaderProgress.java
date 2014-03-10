package yetanotherbardtale.view;

import yetanotherbardtale.graphics.Renderable;
import yetanotherbardtale.imagelib.AsyncImageLoader;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A progress bar for the image loader class.
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class ImageLoaderProgress implements Renderable {
    private AsyncImageLoader loader;
    
    public ImageLoaderProgress(AsyncImageLoader l) {
        loader = l;
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 500);
        g.setColor(Color.BLUE);
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform origin = g2.getTransform();
        g2.translate(250, 250);
        g2.translate(-200, -25);
        int percent = loader.getPercentLoaded();
        g2.fillRect(0, 0, (int)(400 * (percent / 100.0)), 50);
        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, 400, 50);
        g2.setTransform(origin);
        g2.translate(250, 250);
        g2.translate(0-g2.getFontMetrics().stringWidth(Integer.toString(percent) + "%") / 2, g2.getFontMetrics().getHeight() / 2);
        g2.drawString(Integer.toString(percent) + "%", 0, 0);
        g2.setTransform(origin);
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return false;
    }

    @Override
    public void handleClickedOn(int x, int y) { }
}
