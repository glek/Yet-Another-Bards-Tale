package yetanotherbardtale.controller.command.pure;

import yetanotherbardtale.controller.Game;
import yetanotherbardtale.controller.command.impure.ImpureCommand;

/** Undo the effects of the last successfully executed impure command. */
public class RedoCommand extends PureCommand {
    private Game game;

    public RedoCommand(Game game) {
        this.game = game;
    }

    @Override
    public boolean execute() {
        if (game.hasFutureCommands()) {
            ImpureCommand c = game.popFutureCommand();
            game.pushPastCommand(c);
            c.execute();
            return true;
        } else {
            return false;
        }
    }
}
