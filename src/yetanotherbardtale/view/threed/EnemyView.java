package yetanotherbardtale.view.threed;

import java.awt.Color;
import java.awt.Graphics;

import yetanotherbardtale.entity.Enemy;
import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;
import yetanotherbardtale.view.ThreeDView;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class EnemyView extends AbstractEntityView {
    private boolean alive = true;
    
    public EnemyView(Enemy e, ThreeDView view) {
        super(e, view);
        alive = !e.isDead();
    }

    @Override
    public void render(Graphics g) {
        if (alive) {
            super.render(g);
            Location l = super.parent.getLocationStore().getLocation(super.getID());
            Enemy e = (Enemy)super.entity;
            int width = l.getXScale();
            int height = 10;
            int fill = (int)((e.getHealth() / e.getMaxHealth()) * width);
            g.setColor(Color.RED);
            g.fillRect(l.getX(), l.getY() - height, fill, height);
            g.setColor(Color.WHITE);
            g.drawRect(l.getX(), l.getY() - height, width, height);
        }
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return alive ? super.isClickInside(x, y) : false;
    }

    @Override
    public void handleClickedOn(int x, int y) {
        //Do nothing, handled by parent
    }

    @Override
    public void handle(GameEventObject e) {
        if(e instanceof ModelStateChange) {
            Enemy en = (Enemy)e.getSource();
            this.alive = !en.isDead();
        }
        else if(e instanceof Notification) {
            Notification n = (Notification)e;
            parent.displayText(n.getMessage());
        }
    }
}
