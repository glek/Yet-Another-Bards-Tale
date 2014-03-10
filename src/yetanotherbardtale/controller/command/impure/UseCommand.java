package yetanotherbardtale.controller.command.impure;

import yetanotherbardtale.entity.Player;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.entity.item.UsableItem;
import yetanotherbardtale.event.Notification;
import yetanotherbardtale.event.ModelStateChange;

/** A command to use a UsableItem. */
public class UseCommand extends ImpureCommand {
    static {
        commonName = "Use";
        description = "Use an item."; // doesn't need a target
    }

    private Player user;
    private UsableItem target;
    private String targetName;

    /**
     * @param user the player using the item
     * @param target the item to use */
    public UseCommand(Player user, UsableItem target) {
        if (user == null)
            throw new NullPointerException("Cannot invoke item usage on a null user.");
        else if (target == null)
            throw new NullPointerException("Cannot invoke item usage on null target.");
        this.user = user;
        this.target = target;
        this.targetName = ((Item) target).getName();
    }

    /**
     * @param user the player using the item
     * @param targetName the name of an item to search for to use */
    public UseCommand(Player user, String targetName) {
        if (user == null)
            throw new NullPointerException("Cannot invoke item usage on a null user.");
        else if (targetName == null)
            throw new NullPointerException("Cannot invoke item usage on null target name.");
        this.user = user;
        this.target = null;
        this.targetName = targetName;
    }

    /** Use the item in question.
     *
     * @return false if a name rather than reference was provided to the
     * constructor and no such item exists by name in the player's inventory,
     * true otherwise. */
    @Override
    public boolean execute() {
        if (target == null)
            target = (UsableItem) user.getInventory().getItemByName(targetName);

        if (target == null) {
            user.notifyListeners(new Notification(this,
                                                    "No such item in inventory \"" + targetName + "\".",
                                                    Notification.Type.FAILURE));
            return false;
        } else {
            user.notifyListeners(new ModelStateChange(user));
            return target.use(user);
        }
    }

    @Override
    public void unExecute() {
        target.undoUse(user);
        user.notifyListeners(new ModelStateChange(user));
    }
}
