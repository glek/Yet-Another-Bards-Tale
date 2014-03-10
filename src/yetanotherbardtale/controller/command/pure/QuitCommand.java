package yetanotherbardtale.controller.command.pure;

import yetanotherbardtale.controller.Game;

/** The proper way to quit the game, since the game only quits after the next
 * event has been processed. */
public class QuitCommand extends PureCommand {
    private Game game;

    public QuitCommand(Game game) {
        this.game = game;
    }

    @Override
    public boolean execute() {
        /* Doesn't quit immediately; just sets a flag though we don't need to
         * know that. */
        game.quit();
        return true;
    }
}
