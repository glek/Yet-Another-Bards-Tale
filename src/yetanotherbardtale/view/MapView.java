package yetanotherbardtale.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Observable;

import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.graphics.DoublePoint;
import yetanotherbardtale.view.mapview.RoomRenderable;
import yetanotherbardtale.graphics.FramerateTimer;
import yetanotherbardtale.graphics.Renderable;

/**
 * Draws the current room and the rooms connected to them.
 *
 * Also the "2D" view.
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class MapView implements View, Renderable {

    private RoomRenderable render;
    private FramerateTimer timer;
    private String text;
    private long framesOfTextLeft = 0;
    private static final int TEXT_DISPLAY_TIME = 5;
    
    public MapView(Room r, FramerateTimer t) {
        render = new RoomRenderable(r, new DoublePoint(250, 250), true);
        timer = t;
    }
    
    public void setRoom(Room r) {
        render = new RoomRenderable(r, new DoublePoint(250, 250), true);
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(8.0f));
        g.fillRect(0, 0, 500, 500);
        render.render(g);
        if(framesOfTextLeft > 0) {
            g.setFont(g.getFont().deriveFont(50.0f));
            int h = g.getFontMetrics().getHeight();
            java.text.AttributedString str = new java.text.AttributedString(text);
            java.text.AttributedCharacterIterator ch = str.getIterator();
            java.text.BreakIterator it = java.text.BreakIterator.getWordInstance();
            java.awt.font.LineBreakMeasurer line = new java.awt.font.LineBreakMeasurer(ch, it, g.getFontMetrics().getFontRenderContext());
            java.awt.font.TextLayout lay;
            int i = 0;
            while((lay = line.nextLayout(500.0f)) != null) {
                i++;
            }
            line.setPosition(0);
            int y = 500 - h * i;
            for(int j = 0; j < i; j++) {
                lay = line.nextLayout(500.0f);
                if(lay == null) {
                    break;
                }
                lay.draw((Graphics2D)g, 0, y + h + j * h);
            }
            framesOfTextLeft--;
        }
    }

    @Override
    public void displayText(String arg) {
        framesOfTextLeft = timer.getFramerate() * TEXT_DISPLAY_TIME;
        text = arg;
    }

    @Override
    public void displayError(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClickInside(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleClickedOn(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Room) {
            this.setRoom((Room) arg);
        }        
    }
}
