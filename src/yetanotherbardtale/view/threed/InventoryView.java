package yetanotherbardtale.view.threed;

import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.graphics.Renderable;
import yetanotherbardtale.entity.item.Inventory;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.view.ThreeDView;
import yetanotherbardtale.event.GameEventObjectListener;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Color;

/**
 * A graphical component rendering the current inventory.
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class InventoryView implements Renderable, GameEventObjectListener {
    private ArrayList<ItemView> items;
    private int x, y, scaleX, scaleY;
    private Image background;
    private ThreeDView parent;
    private ItemView selected;
    private boolean updating;
    
    public InventoryView(Player p, int x, int y, int scaleX, int scaleY, ThreeDView v) {
        items = new ArrayList<>();
        parent = v;
        p.getInventory().addListener(this);
        for(Item i : p.getInventory().getInventory())
            items.add(new ItemView(i, parent));
        background = parent.getImageLibrary().getImage("InvBackground");
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    private void update(Inventory inv) {
        updating = true;
        if(inv.getInventory().length == 0) {
            for(ItemView v : items) {
                v.destroy();
            }
            items.clear();
            updating = false;
            return;
        }
        int scale = scaleX / inv.getInventory().length;
        if(scale > scaleY)
            scale = scaleY;
        int xLoc = 0;
        for(ItemView v : items)
            v.destroy();
        items.clear();
        for(Item i : inv.getInventory()) {
            items.add(new ItemView(i, parent));
            parent.getLocationStore().getLocation(i.getID()).setScale(scale, scaleY);
            parent.getLocationStore().getLocation(i.getID()).setPosition(scale * xLoc, y);
            xLoc++;
        }
        updating = false;
    }
    
    public void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void render(Graphics g) {
        if(updating)
            return;

        g.drawImage(background, x, y, scaleX, scaleY, null);
        if(items.isEmpty())
            return;

        int scale = scaleX / items.size();
        if(scale > scaleY)
            scale = scaleY;

        int xLoc = 0;
        for(ItemView i : items) {
            parent.getLocationStore().getLocation(i.getID()).setPosition(scale * xLoc, y);
            i.render(g);
            if(parent.getSelectedItem() == null) {
                xLoc++;
                continue;
            }
            if(parent.getSelectedItem() == i) {
                Graphics2D g2 = (Graphics2D)g;
                Composite origin = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                g2.setColor(new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 128));
                g2.fillRect(scale * xLoc, y, scale, scaleY);
                g2.setComposite(origin);
                g2.setColor(Color.BLACK);
            }
            xLoc++;
        }
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return ((x > this.x && x < this.x + scaleX) &&
                (y > this.y && y < this.y + scaleY));
    }

    @Override
    public void handleClickedOn(int x, int y) {
        for(ItemView i : items) {
            if(i.isClickInside(x, y)) {
                if(parent.getSelectedItem() == i) {
                    parent.setSelectedItem(null);
                    selected = null;
                }
                else {
                    parent.setSelectedItem(i);
                    selected = i;
                }
            }
        }
    }

    @Override
    public void handle(GameEventObject e) {
        if(e instanceof ModelStateChange) {
            ModelStateChange ch = (ModelStateChange)e;
            if(ch.getSource() instanceof Inventory) {
                this.update((Inventory)ch.getSource());
            }
        }
        if(e instanceof Notification) {
            Notification n = (Notification)e;
            parent.displayText(n.getMessage());
        }
    }
}
