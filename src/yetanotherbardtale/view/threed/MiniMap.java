package yetanotherbardtale.view.threed;

import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.view.mapview.RoomRenderable;
import yetanotherbardtale.graphics.DoublePoint;
import yetanotherbardtale.graphics.Renderable;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.event.GameEventObjectListener;
import yetanotherbardtale.event.ModelStateChange;

import java.awt.Graphics;
import java.awt.Color;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class MiniMap implements Renderable, GameEventObjectListener {
    
    private RoomRenderable render;
    
    public MiniMap(Player p) {
        Room r = p.getCurrentRoom();
        render = new RoomRenderable(r, new DoublePoint(250, 250), true);
        p.addListener(this);
    }
    
    public void setRoom(Room r) {
        render = new RoomRenderable(r, new DoublePoint(250, 250), true);
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 500);
        render.render(g);
    }

    @Override
    public boolean isClickInside(int x, int y) {
        //Do nothing
        return false;
    }

    @Override
    public void handleClickedOn(int x, int y) {
        //Do nothing
    }

    @Override
    public void handle(GameEventObject e) {
        if(e instanceof ModelStateChange) {
            ModelStateChange ch = (ModelStateChange) e;
            if (ch.getSource() instanceof Player)
                this.setRoom(((Player) ch.getSource()).getCurrentRoom());
        }
    }
}
