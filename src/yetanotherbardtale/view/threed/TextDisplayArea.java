package yetanotherbardtale.view.threed;

import yetanotherbardtale.graphics.Renderable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.text.BreakIterator;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class TextDisplayArea implements Renderable {

    private int x, y, dimX, dimY;
    private String text;
    private int frameRate;
    public static final int DISPLAY_TIME = 5;
    public static final double BLANK_TIME = 0.10;
    private int displayRemaining;
    private LineBreakMeasurer line;
    private TextLayout l;
    private boolean isBlanked = false;
    
    public TextDisplayArea(int x, int y, int scaleX, int scaleY, int frames) {
        frameRate = frames;
        this.x = x;
        this.y = y;
        dimX = scaleX;
        dimY = scaleY;
        text = "";
    }
    
    public void setFrameRate(int i) {
        frameRate = i;
    }
    
    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setText(String s) {
        text = s;
        displayRemaining = (int)(BLANK_TIME * 1000);
        isBlanked = true;
        line = null;
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, dimX, dimX);
        g.setColor(Color.WHITE);
        if(line == null) {
            if(!text.isEmpty()) {
                line = new LineBreakMeasurer((new AttributedString(text).getIterator()), BreakIterator.getWordInstance(), g.getFontMetrics(g.getFont().deriveFont(12.0f)).getFontRenderContext());
                l = line.nextLayout(dimX);
                text = "";
            }
            else {
                return;
            }
        }
        if(displayRemaining < 0) {
            if(isBlanked) {
                isBlanked = false;
                displayRemaining = DISPLAY_TIME * 1000;
                return;
            }
            l = line.nextLayout(dimX);
            displayRemaining = DISPLAY_TIME * 1000;
        }
        if(isBlanked) {
            displayRemaining -= 1000 / frameRate;
            return;
        }
        if(l == null) {
            line = null;
        }
        else {
            l.draw((Graphics2D)g, x, y + dimY);
            displayRemaining -= 1000 / frameRate;
        }
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return false;
    }

    @Override
    public void handleClickedOn(int x, int y) { }
}
