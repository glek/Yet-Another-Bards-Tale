package yetanotherbardtale.entity.item;

import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.Creature;

/**
 * An item that can be used to harm other creatures.
 * @author G'lek
 * @version 0.1.1
 */
public class Weapon extends Item implements TargetingItem {
    private double damage;
    
    public Weapon(String name, String description, double damage, double weight) {
        super(name, description, weight);
        this.damage = damage;
                this.type = Item.DYNAMIC;
    }
    
    /** @return the damage the weapon can do to another creature */
    public double getDamage() {
        return damage;
    }
    
    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public boolean useOn(GameEntity user, GameEntity target) {
        if(target instanceof Creature) {
            Creature c = (Creature)target;
            c.hurt(damage);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void undoUseOn(GameEntity user, GameEntity target) {
        if(target instanceof Creature) {
            Creature c = (Creature)target;
            c.undoHurt(damage);
        }
    }
}
