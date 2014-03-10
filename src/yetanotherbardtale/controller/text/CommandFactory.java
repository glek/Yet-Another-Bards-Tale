package yetanotherbardtale.controller.text;

import yetanotherbardtale.controller.command.Command;
import yetanotherbardtale.controller.command.pure.*;
import yetanotherbardtale.controller.command.impure.*;

/**
 * Constructs events from Commands
 * @author Michael Damian Mulligan (G'lek), Andrew O'Hara (zalpha314)
 * @version 0.1.1
 */
public class CommandFactory {
    protected TextGame game;

    /**
     * Construct the GameEvent by parsing tokens of the [game] commandLine (not
     * the JVM command line).
     *
     * @param commandLine the commandLine to parse into the GameEvent
     * @return the constructed GameEvent, null if it can't be constructed.
     */

    /* An event factory is a glorified parser that must be associated with a
     * particular game. Meaningless outside of that context. */
    public CommandFactory(TextGame game) {
        this.game = game;
    }

    public Command parseLine(String commandLine) {
        CommandWord command = CommandWord.parseLine(commandLine); // Parse the CommandWord
        if (command == null)
            return new UnrecognizedCommand(game);

        // Split the args from the command
        String[] args = command.getSplitRegex()
                               .split(commandLine.toLowerCase());
        
        /* TODO: Build static mapping of command words to their event at
         * runtime rather than hardcoding it here. Will need reflection. Not
         * done yet as Java lazily loads class definitions. */
        // Parse command and args into GameEvent
        try {
            switch(command) {
                case EXAMINE:
                    return new ExamineCommand(game.getPlayer(), args[1].trim());
                    // TODO: Replace with unified interact
                case MOVE:
                    return new MoveCommand(game.getPlayer(), args[1].trim());
                case PICKUP:
                    return new PickupCommand(game.getPlayer().getCurrentRoom().getInventory(),
                                             game.getPlayer().getInventory(),
                                             args[1].trim());
                case DROP:
                    return new PickupCommand(game.getPlayer().getInventory(),
                                             game.getPlayer().getCurrentRoom().getInventory(),
                                             args[1].trim());
                case USE:
                    return new UseCommand(game.getPlayer(), args[1].trim());
                case USE_ON:
                    return new UseOnCommand(game.getPlayer(),
                                            args[1].trim(),
                                            args[2].trim());
                case UNDO:
                    return new UndoCommand(game);
                case REDO:
                    return new RedoCommand(game);
                case QUIT:
                    return new QuitCommand(game);
                case HELP:
                    return new HelpCommand(game);
                case HELP_: 
                    return new HelpWithCommand(game, args[1].trim());
                case STATUS:
                    return new StatusCommand(game);
                default:
                    throw new UnsupportedOperationException("Haven't implemented \"" + command + "\" yet.");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Error in CommandFactory.parseEvent() - Parsed " +
                               "Command, and expected arguments, but found " +
                               "none!!  Likely error in split regex.");
        }
        return null;
    
    }
}
