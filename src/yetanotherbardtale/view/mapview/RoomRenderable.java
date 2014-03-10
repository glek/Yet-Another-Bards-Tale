package yetanotherbardtale.view.mapview;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.graphics.Renderable;
import yetanotherbardtale.graphics.DoublePoint;
import java.awt.Color;

/**
 * A simple room.
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class RoomRenderable implements Renderable {
    //Should be divisible by 2
    private static final int DIM = 98;
    private static final int ADD_DISTANCE = 100;
    private List<LinkRenderable> links = new ArrayList<>();
    private DoublePoint location;
    private String name;
    private Room room;
    
    public RoomRenderable(Room r, DoublePoint p, boolean currentRoom) {
        name = r.getName();
        room = r;
        location = p;
        if(currentRoom) {
            double rot = 360.0 / r.getExits().length;
            double currentRot = 0.0;
            for(Exit e : r.getExits()) {
                DoublePoint l = new DoublePoint(p.x + (DIM + ADD_DISTANCE) * Math.cos(Math.toRadians(currentRot)), p.y + (DIM + ADD_DISTANCE) * Math.sin(Math.toRadians(currentRot)));
                links.add(new LinkRenderable(this, new RoomRenderable(e.getDestination(), l, false), e.getName(), e.isLocked()));
                currentRot += rot;
            }
        }
    }
    
    public boolean hasMonster() {
        return room.hasEnemy();
    }
    
    public int getX() {
        return Double.valueOf(location.x).intValue();
    }
    
    public int getY() {
        return Double.valueOf(location.y).intValue();
    }

    @Override
    public void render(Graphics g) {
        if(room.hasEnemy()) {
            g.setColor(Color.ORANGE);
        }
        else {
            g.setColor(Color.WHITE);
        }
        for(LinkRenderable l : links) {
            l.render(g);
        }
        g.setColor(Color.BLACK);
        g.fillRect(getX() - DIM / 2, getY() - DIM / 2, DIM, DIM);
        if(room.hasEnemy()) {
            g.setColor(Color.ORANGE);
        }
        else {
            g.setColor(Color.WHITE);
        }
        g.drawRect(getX() - DIM / 2, getY() - DIM / 2, DIM, DIM);
        java.awt.font.FontRenderContext read = g.getFontMetrics().getFontRenderContext();
        java.text.AttributedCharacterIterator it = (new java.text.AttributedString(name)).getIterator();
        java.text.BreakIterator bre = java.text.BreakIterator.getWordInstance();
        java.awt.font.LineBreakMeasurer line = new java.awt.font.LineBreakMeasurer(it, bre, read);
        for(int i = 0; i < 10; i++) {
            java.awt.font.TextLayout lay = line.nextLayout(DIM);
            if(lay == null) {
                break;
            }
            lay.draw((Graphics2D)g, getX() - DIM / 2 + 5, getY() + i * g.getFontMetrics().getHeight());
        }
    }

    @Override
    public boolean isClickInside(int x, int y) {
        if((x > location.x && x < location.x + DIM) &&
                (y > location.y && y < location.y + DIM)) {
            return true;
        }
        return false;
    }

    @Override
    public void handleClickedOn(int x, int y) {
        //Do nothing, this is handled by the LinkRenderable
    }
}
