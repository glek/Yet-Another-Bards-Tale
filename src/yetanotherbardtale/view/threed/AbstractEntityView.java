package yetanotherbardtale.view.threed;

import java.awt.Graphics;
import java.awt.Image;

import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.event.GameEventObjectListener;
import yetanotherbardtale.graphics.Renderable;
import yetanotherbardtale.view.ThreeDView;

public abstract class AbstractEntityView implements Renderable, GameEventObjectListener {
    protected GameEntity entity;
    protected final ThreeDView parent;
    protected Image img;
    
    private final Location loc;
    
    public AbstractEntityView(GameEntity entity, ThreeDView view) {
        this.entity = entity;
        this.parent = view;
        loc = view.getLocationStore().getLocation(entity.getID());
        img = view.getImageLibrary().getImage(view.getMapping().getMapping(entity.getID()));
        
        entity.addListener(this);
    }
    
    public String getName() {
        return entity.getName();
    }
    
    public GameEntity getEntity() {
        return entity;
    }
    
    public void destroy() {
        entity.deleteListener(this);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, loc.getX(), loc.getY(),
                loc.getXScale(), loc.getYScale(), null);
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return ((x > loc.getX() && x < loc.getX() + loc.getXScale()) &&
                (y > loc.getY() && y < loc.getY() + loc.getYScale()));
    }

    @Override public void handleClickedOn(int x, int y) {}

    
    public String getID() {
        return entity.getID();
    }
    
    public abstract void handle(GameEventObject e);
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractEntityView) {
            return getID().equals(((AbstractEntityView)o).getID());
        }
        return false;
    }

}
