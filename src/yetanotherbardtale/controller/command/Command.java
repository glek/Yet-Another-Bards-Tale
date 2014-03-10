package yetanotherbardtale.controller.command;

/** A self-executing game command.
 *
 * Used to pass instrutions to main game play loop. */
public abstract class Command {
    /** One or two word plain English name for this command. */
    protected static String commonName;
    
    /** Descriptive sentence-form description of the <em>semantics</em> of the command. */
    protected static String description;

    /** Execute game logic. */
    public abstract boolean execute();
}
