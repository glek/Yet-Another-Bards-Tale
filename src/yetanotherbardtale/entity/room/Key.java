package yetanotherbardtale.entity.room;


import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.entity.item.TargetingItem;
import yetanotherbardtale.event.Notification;
import yetanotherbardtale.event.ModelStateChange;

/**
 * A key unlocks an exit.
 * @author Michael Damian Mulligan (G'lek)
 * @version 0.1.0 
 */
public class Key extends Item implements TargetingItem {
    // FIXME: Shouldn't this be a reference to a room instead, and final?
    private String code;
    
    public Key(String name, String description, double weight, String code) {
        super(name, description, weight);
        this.code = code;
        this.type = DYNAMIC;
    }
    
    public String getCode() {
        return code;
    }

    public boolean useOn(GameEntity user, GameEntity target) {
        if (!(target instanceof Exit))
            throw new IllegalArgumentException("Cannot use a key on a non-exit.");

        Exit e = (Exit) target;
        if (e.tryUnlock(code)) {
            notifyListeners(new ModelStateChange(this));
            return true;
        } else {
            notifyListeners(new Notification(this,
                                             "Could not unlock "
                                                 + e.getName()
                                                 + ".",
                                             Notification.Type.FAILURE));
            return false;
        }
    }

    public void undoUseOn(GameEntity user, GameEntity target) {
        if (!(target instanceof Exit))
            throw new IllegalArgumentException("Cannot use a key on a non-exit.");

        ((Exit) target).setLocked(true, code);
    }
}
