package yetanotherbardtale.view.mapview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import yetanotherbardtale.graphics.Renderable;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class LinkRenderable implements Renderable {
    public final RoomRenderable source;
    public final RoomRenderable destination;
    public final String name;
    public final boolean lock;
    
    public LinkRenderable(RoomRenderable s, RoomRenderable d, String n, boolean locked) {
        source = s;
        destination = d;
        name = n;
        lock = locked;
    }

    @Override
    public void render(Graphics g) {
        if(lock) {
            g.setColor(Color.RED);
        }
        else if(source.hasMonster()) {
            g.setColor(Color.ORANGE);
        }
        else {
            g.setColor(Color.WHITE);
        }
        g.setFont(g.getFont().deriveFont(12.0f));
        g.drawLine(source.getX(), source.getY(), destination.getX(), destination.getY());
        Graphics2D graph = (Graphics2D)g;
        java.awt.geom.Rectangle2D rect = new java.awt.geom.Rectangle2D.Double(0, 0, graph.getFontMetrics().stringWidth(name), graph.getFontMetrics().getHeight());
        int distX = destination.getX() - source.getX();
        int distY = destination.getY() - source.getY();
        java.awt.geom.AffineTransform tran = graph.getTransform();
        graph.translate(source.getX(), source.getY());
        double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        graph.rotate(Math.atan((float)(distY) / (float)(distX)) + ((distX < 0)?Math.PI:0));
        graph.translate(distance / 2, 0.0);
        graph.translate(0.0 - rect.getWidth() / 2, 0.0);
        graph.translate(0.0, 0.0 - rect.getHeight() / 2);
        graph.rotate(0.0 - Math.atan((float)(distY) / (float)(distX)) + ((distX < 0)?Math.PI:0), rect.getCenterX(), rect.getCenterY());
        graph.setColor(Color.BLACK);
        graph.fillRect(0, 0, (int)rect.getWidth() + 10, (int)rect.getHeight() + 2);
        if(lock) {
            g.setColor(Color.RED);
        }
        else if(source.hasMonster()) {
            g.setColor(Color.ORANGE);
        }
        else {
            g.setColor(Color.WHITE);
        }
        graph.drawRect(0, 0, (int)rect.getWidth() + 10, (int)rect.getHeight() + 2);
        g.drawString(name, 5, g.getFontMetrics().getHeight());
        graph.setTransform(tran);
        destination.render(g);
    }

    @Override
    public boolean isClickInside(int x, int y) {
        //TO DO: Add click check logic
        //TO DO: Check for click in destination
        return false;
    }

    @Override
    public void handleClickedOn(int x, int y) {
        //TO DO: Add logic for handling click
        //TO DO: Check for click in destination
        //TO DO: Synthesize event
    }
}
