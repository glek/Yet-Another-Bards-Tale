package yetanotherbardtale.entity.item;

import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.Creature;
import yetanotherbardtale.event.ModelStateChange;

/**
 * An item that restores health.
 * @author Michael Damian Mulligan (G'lek)
 * @version 0.1.1
 */
public class Potion extends Item implements TargetingItem, UsableItem {
    private double healAmount;
    
    public Potion(String name, String description, double healAmmount, double weight) {
        super(name, description, weight);
        this.healAmount = healAmmount;
    }

    /** @return the amount which this potion heals a creature when used */
    public double getHealAmount() {
        return healAmount;
    }
    
    public void setHealAmount(double healing) {
        healAmount = healing;
    }
    
    public boolean use(GameEntity user) {
        if (!(user instanceof Creature))
            throw new IllegalArgumentException("Cannot heal a non-creature.");
        ((Creature) user).heal(healAmount);
        user.notifyListeners(new ModelStateChange(user));
        return true;
    }

    public void undoUse(GameEntity user) {
        if (!(user instanceof Creature))
            throw new IllegalArgumentException("Cannot undo healing a non-creature.");
        ((Creature) user).undoHeal(healAmount);
    }

    public boolean useOn(GameEntity user, GameEntity target) {
        if (!(target instanceof Creature))
            throw new IllegalArgumentException("Cannot heal a non-creature.");
        ((Creature) target).heal(healAmount);
        target.notifyListeners(new ModelStateChange(target));
        return true;
    }

    public void undoUseOn(GameEntity user, GameEntity target) {
        if (!(target instanceof Creature))
            throw new IllegalArgumentException("Cannot undo healing a non-creature.");
        ((Creature) target).undoHeal(healAmount);
    }
}
