package yetanotherbardtale.controller.command.impure;

import yetanotherbardtale.controller.command.Command;

/** A command with side-effects.
 *
 * Subclasses of ImpureCommand are expected to notify their listeners of a
 * change in their state through a ModelStateChange event. */
public abstract class ImpureCommand extends Command {
    // unExecute shouldn't fail 'silently'; it should throw exceptions.
    public abstract void unExecute();
}
