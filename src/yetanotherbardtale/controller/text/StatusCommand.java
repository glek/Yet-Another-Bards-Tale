package yetanotherbardtale.controller.text;

import yetanotherbardtale.controller.command.pure.PureCommand;
import yetanotherbardtale.event.Notification;

/** A command to get the current player status (inventory, etc., as determined
 * by Player.toString()) and room status (same, from toString()). */
public class StatusCommand extends PureCommand {
    private TextGame game;

    public StatusCommand(TextGame game) {
        this.game = game;
    }

    @Override
    public boolean execute() {
        game.handle(new Notification(this,
                                     game.getPlayer().toString(),
                                     Notification.Type.INFORMATIONAL));
        game.handle(new Notification(this,
                                     "--",
                                     Notification.Type.INFORMATIONAL));
        game.handle(new Notification(this,
                                     "Room: " + game.getPlayer().getCurrentRoom().toString(),
                                     Notification.Type.INFORMATIONAL));
        return true;
    }
}
