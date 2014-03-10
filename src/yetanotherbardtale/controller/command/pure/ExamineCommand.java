package yetanotherbardtale.controller.command.pure;

import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.event.Notification;

/** A command to send a textual notification from the player to its listeners
 * containing information about a specific examinable entity in the player's
 * current room. */
public class ExamineCommand extends PureCommand {
    static {
        commonName = "Examine";
        description = "Examine a game entity.";
    }

    private GameEntity target;
    private String     targetName;
    private Player     player; // The player, not the game examines something. TODO: Is this correct?

    public ExamineCommand(Player player, GameEntity target) {
        if (player == null)
            throw new NullPointerException("Cannot examine using a null player.");
        else if (target == null)
            throw new NullPointerException("Cannot examine a null target.");

        this.player = player;
        this.target = target;
        this.targetName = target.getName();
    }

    public ExamineCommand(Player player, String targetName) {
        if (player == null)
            throw new NullPointerException("Cannot examine using a null player.");
        else if (targetName == null)
            throw new NullPointerException("Cannot examine a null target.");

        this.player = player;
        this.target = null;
        this.targetName = targetName;
    }

    @Override
    public boolean execute() {
        if (target == null) {
            Room r = player.getCurrentRoom();
            // I see a legitimate use of goto here
            if (player.getInventory().containsName(targetName)) {
                target = player.getInventory().getItemByName(targetName);
            } else if (r.getEnemy() != null &&
                       r.getEnemy().getName().toLowerCase().equals(targetName.toLowerCase())) {
                target = r.getEnemy();
            } else if (r.containsExit(targetName)) {
                target = r.getExit(targetName);
            } else if (r.findItemByName(targetName) != null) {
                target = r.findItemByName(targetName);
            } else { // keywords **after** in case something here actuall has that name
                switch (targetName.toLowerCase()) {
                    case "inventory":
                        target = player.getInventory();
                        break;
                    case "monster":
                        target = r.getEnemy();
                        break;
                    case "room":
                        target = r;
                        break;
                }
            }

        }

        if (target != null) {
            player.notifyListeners(new Notification(this,
                                                    target.getDescription(),
                                                    Notification.Type.INFORMATIONAL));
            return true;
        } else {
            player.notifyListeners(new Notification(this,
                                                    "No such entity \"" +
                                                        targetName +
                                                        "\".", Notification.Type.FAILURE));
            return false;
        }
    }
}
