package yetanotherbardtale.controller.text;

import yetanotherbardtale.controller.command.pure.PureCommand;
import yetanotherbardtale.event.Notification;

/** A command to print out that the last command entered on the TextGame
 * command line was not recognized. */
public class UnrecognizedCommand extends PureCommand {
    private TextGame game;

    public UnrecognizedCommand(TextGame game) {
        this.game = game;
    }

    @Override
    public boolean execute() {
        game.handle(new Notification(this,
                                     "Unrecognized command.",
                                     Notification.Type.FAILURE));
        return true;
    }
}
