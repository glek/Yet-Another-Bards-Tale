package yetanotherbardtale.view.threed;

import yetanotherbardtale.entity.item.Inventory;
import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.imagelib.ImageLibrary;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.Enemy;
import yetanotherbardtale.view.ThreeDView;
import yetanotherbardtale.controller.command.impure.*;
import yetanotherbardtale.controller.command.Command;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;

import java.awt.Image;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class RoomView extends AbstractEntityView {
    private Image img;
    protected ArrayList<ItemView> items;
    protected ArrayList<ExitView> exits;
    protected EnemyView enemy;
    protected ThreeDView parent;
    private int direction;
    private Player p;
    protected boolean updating = false;
    
    public RoomView(Player p, ThreeDView par) {
        super(p.getCurrentRoom(), par);
        direction = p.getDirection();
        this.p = p;
        p.addListener(this);
        p.getInventory().addListener(this);
        LocationStore store = par.getLocationStore();
        ImageLibrary lib = par.getImageLibrary();
        items = new ArrayList<>();
        exits = new ArrayList<>();
        parent = par;
        entity = p.getCurrentRoom();
        Room r = p.getCurrentRoom();
        r.getInventory().addListener(this);
        for(Item i : r.getItems()) {
            if(store.getLocation(i.getID()).getFace() == direction) {
                items.add(new ItemView(i, par));
            }
        }
        for(Exit e : r.getExits()) {
            if(store.getLocation(e.getID()).getFace() == direction) {
                exits.add(new ExitView(e, par));
            }
        }
        if(r.hasEnemy()) {
            enemy = new EnemyView((Enemy)r.getEnemy(), par);
        }
        img = lib.getImage(parent.getMapping().getRoomMapping(r.getID(), direction));
    }
    
    public void update(Player p) {
        updating = true;
        Room r = p.getCurrentRoom();
        if(r != entity) {
            entity.deleteListener(this);
            ((Room)entity).getInventory().deleteListener(this);
            r.addListener(this);
            r.getInventory().addListener(this);
            entity = r;
        }
        direction = p.getDirection();
        for(ItemView i : items) {
            i.destroy();
        }
        items.clear();
        for(ExitView e : exits) {
            e.destroy();
        }
        exits.clear();
        if(enemy != null) {
            enemy.destroy();
        }
        enemy = null;
        LocationStore store = parent.getLocationStore();
        ImageLibrary lib = parent.getImageLibrary();
        for(Item i : r.getItems()) {
            if(store.getLocation(i.getID() + "_OLD") == null) {
                store.addLocation(i.getID() + "_OLD", new Location(store.getLocation(i.getID())));
            }
            store.addLocation(i.getID(), new Location(store.getLocation(i.getID() + "_OLD")));
            if(store.getLocation(i.getID()).getFace() == direction) {
                items.add(new ItemView(i, parent));
            }
        }
        for(Exit e : r.getExits()) {
            if(store.getLocation(e.getID()).getFace() == direction) {
                exits.add(new ExitView(e, parent));
            }
        }
        if(r.hasEnemy()) {
            enemy = new EnemyView((Enemy)r.getEnemy(), parent);
        }
        img = lib.getImage(parent.getMapping().getRoomMapping(r.getID(), direction));
        updating = false;
    }

    @Override
    public void render(Graphics g) {
        if(updating) {
            return;
        }
        g.drawImage(img, 0, 0, 500, 500, null);
        for(ExitView v : exits) {
            v.render(g);
        }
        for(ItemView i : items) {
            i.render(g);
        }
        if(enemy != null) {
            enemy.render(g);
        }
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return false;
    }

    @Override
    public void handleClickedOn(int x, int y) {
        if(updating) {
            return;
        }
        LocationStore store = parent.getLocationStore();
        Command e = null;
        for(ItemView i : items) {
            if(i.isClickInside(x, y)) {
                if(parent.getSelectedItem() == null) {
                    if(p.getInventory().canAdd(((Room)entity).findItemByName(i.getName()))) {
                        store.addLocation(i.getID() + "_OLD", new Location(store.getLocation(i.getID())));
                        e = new PickupCommand(((Room)entity).getInventory(),
                                p.getInventory(), i.getName());
                    }
                    else {
                        e = new UseCommand(p, i.getName());
                    }
                }
                else {
                    e = new UseOnCommand(p, parent.getSelectedItem().getName(), i.getName());
                    parent.setSelectedItem(null);
                }
                if(e != null) {
                    parent.passEventToGame(e);
                    return;
                }
            }
        }
        for(ExitView ex : exits) {
            if(ex.isClickInside(x, y)) {
                if(parent.getSelectedItem() == null) {
                    e = new MoveCommand(p, ex.getName()); }
                else {
                    e = new UseOnCommand(p, parent.getSelectedItem().getName(), ex.getName());
                    parent.setSelectedItem(null);
                }
            }
            if(e != null) {
                parent.passEventToGame(e);
                return;
            }
        }
        if(enemy != null) {
            if(enemy.isClickInside(x, y)) {
                if(parent.getSelectedItem() == null) {
                    parent.displayText("Cannot attack a monster with no weapon!");
                }
                else {
                    e = new UseOnCommand(p, parent.getSelectedItem().getName(), enemy.getName());
                }
            }
            if(e != null) {
                parent.passEventToGame(e);
                return;
            }
        }
        if(parent.getSelectedItem() != null) {
            store.getLocation(parent.getSelectedItem().getID() + "_OLD").setPosition(x, y);
            store.getLocation(parent.getSelectedItem().getID() + "_OLD").setFace(direction);
            e = new PickupCommand(p.getInventory(), ((Room)entity).getInventory(), parent.getSelectedItem().getName());
            parent.setSelectedItem(null);
        }
        if(e != null)
            parent.passEventToGame(e);
    }
    
    public void notifyParent(GameEventObject e) {
        if(e instanceof Notification) {
            this.handle(e);
        }
    }

    @Override
    public void handle(GameEventObject e) {
        if(e instanceof ModelStateChange) {
            this.update(p);
        }
        else if(e instanceof Notification) {
            Notification n = (Notification)e;
            if(n.getSource() instanceof Inventory) {
                
            }
            else {
                parent.displayText(n.getMessage());
            }
        }
    }
}
