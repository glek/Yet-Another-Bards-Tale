package yetanotherbardtale.controller.command.pure;

import yetanotherbardtale.controller.Game;
import yetanotherbardtale.controller.command.impure.ImpureCommand;

/** Redo the effects of the last undone successful impure command. */
public class UndoCommand extends PureCommand {
    private Game game;

    public UndoCommand(Game game) {
        this.game = game;
    }

    @Override
    public boolean execute() {
        if (game.hasPastCommands()) {
            ImpureCommand c = game.popPastCommand();
            game.pushFutureCommand(c);
            c.unExecute();
            return true;
        } else {
            return false;
        }
    }
}
