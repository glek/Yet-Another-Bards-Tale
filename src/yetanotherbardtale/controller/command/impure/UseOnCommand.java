package yetanotherbardtale.controller.command.impure;

import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.entity.item.TargetingItem;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;

/** A command to use a TargetingItem. */
public class UseOnCommand extends ImpureCommand {
    static {
        commonName = "Use on";
        description = "Use an item on a game entity."; // doesn't need a target
    }

    private Player user;
    private TargetingItem item;
    private GameEntity target;
    private String itemName, targetName;

    public UseOnCommand(Player user, TargetingItem item, GameEntity target) {
        this.user   = user;
        this.item   = item;
        this.itemName = ((Item) item).getName();
        this.target = target;
        this.targetName = ((GameEntity) target).getName();
    }

    public UseOnCommand(Player user, String item, String targetName) {
        this.user = user;
        this.item = null;
        this.itemName = item;
        this.target = null;
        this.targetName = targetName;
    }

    /** Use the targeting item.
     *
     * When a target name was passed and at least two conflicting names exist,
     * enemies, exits, and items in the current room are preferred in that
     * order.
     *
     * Any listeners for the player (likely the UI, if any) are notified that
     * the item was used successfully or unsuccessfully.
     *
     * @return false if a target or item name was passed rather than a
     * reference and no such entity exists by name in the player's current room
     * or inventory respectively, true otherwise. */
    @Override
    public boolean execute() {
        if (item == null)
            item = (TargetingItem) user.getInventory().getItemByName(itemName);
        if (item == null) {
            user.notifyListeners(new Notification(this,
                                                    "No item in inventory named \"" +
                                                        itemName +
                                                        "\".",
                                                    Notification.Type.FAILURE));
            return false;
        }

        if (target == null) {
            Room r = user.getCurrentRoom();
            if (targetName.toLowerCase().endsWith("exit")) {
                target = r.getExit(targetName.toLowerCase().split("\\sexit$")[0]);
                if (target == null) {
                    user.notifyListeners(new Notification(this,
                                                            "No " + targetName.toLowerCase() + ".",
                                                            Notification.Type.FAILURE));
                    return false;
                }
            } else if (r.getEnemy() != null &&
                       r.getEnemy()
                        .getName()
                        .toLowerCase()
                        .equals(targetName.toLowerCase())) {
                target = r.getEnemy();
            } else if (r.containsExit(targetName)) {
                target = r.getExit(targetName);
            } else if (r.containsName(targetName)) {
                target = r.findItemByName(targetName);
            } else {
                switch (targetName) {
                    case "monster":
                        target = r.getEnemy();
                        break;
                    case "inventory":
                        target = user.getInventory();
                        break;
                    case "room":
                        target = r;
                        break;
                    case "self":
                        target = user;
                        break;
                }
            }
        }
        if (target == null) {
            user.notifyListeners(new Notification(this,
                                                  "Nobody or nothing named \"" +
                                                      targetName +
                                                      "\" to use this item on.",
                                                  Notification.Type.FAILURE));
            return false;
        }

        user.notifyListeners(new Notification(this,
                                              "Used the " + itemName + " on " + targetName + ".",
                                              Notification.Type.SUCCESS));
        user.notifyListeners(new ModelStateChange(user));
        return item.useOn(user, target);
    }

    @Override
    public void unExecute() {
        item.undoUseOn(user, target);
        user.notifyListeners(new ModelStateChange(user));
    }
}
