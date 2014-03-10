package yetanotherbardtale.controller.text;

import yetanotherbardtale.controller.command.pure.PureCommand;
import yetanotherbardtale.event.Notification;

/** A command to print help text for a particular command to the console.
 *
 * Not in the  core command package as it is TextGame specific. */
public class HelpWithCommand extends PureCommand {
    private TextGame game;
    private String commandLine;
    private CommandWord word;

    public HelpWithCommand(TextGame game, String commandLine) {
        this.game = game;
        this.commandLine = commandLine;
        try {
            this.word = CommandWord.valueOf(commandLine.toUpperCase()
                                                       .trim()
                                                       .replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            this.word = null;
        }
    }

    @Override
    public boolean execute() {
        if (game == null) {
            game.handle(new Notification(this,
                                         "No such command \"" +
                                           commandLine +
                                           "\".",
                                         Notification.Type.FAILURE));
            return false;
        } else {
            game.handle(new Notification(this,
                                         word.getDescription(),
                                         Notification.Type.INFORMATIONAL));
            return true;
        }
    }
}
