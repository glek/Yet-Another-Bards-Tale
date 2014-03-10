package yetanotherbardtale.controller.text;

import yetanotherbardtale.controller.command.pure.PureCommand;
import yetanotherbardtale.event.Notification;

/** A command to print help text to the console.
 *
 * Currently prints out a list of available commands.
 *
 * Not in the  core command package as it is TextGame specific. */
public class HelpCommand extends PureCommand {
    private TextGame game;

    public HelpCommand(TextGame game) {
        this.game = game;
    }

    @Override
    public boolean execute() {
        game.handle(new Notification(this,
                                     CommandWord.getCommandNames(),
                                     Notification.Type.INFORMATIONAL));
        return true;
    }
}
