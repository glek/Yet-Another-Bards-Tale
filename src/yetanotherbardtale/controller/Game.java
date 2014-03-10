package yetanotherbardtale.controller;

import java.util.Stack;

import yetanotherbardtale.controller.command.Command;
import yetanotherbardtale.controller.command.impure.ImpureCommand;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.view.View;

/**
 * A template Yet Another Bard Tale game.
 *
 * This main class creates and initializes all the others: it creates all
 * rooms, creates the parser and starts the game. It also evaluates and
 * executes the commands that the parser returns.
 *
 * This is the top-level controller for yetanotherbardtale, unless you want to
 * somehow bundle this within a menu or other.
 *
 * @author  Andrew O'Hara (zalpha314), Michael Damian Mulligan (G'lek)
 * @version 0.1.0 
 */
public abstract class Game {
    private boolean wantToQuit; // Synchronous exit flag
    private boolean modelHasChanged;
    protected final Level level;
    private Stack<ImpureCommand> pastCommands, futureCommands;
    protected View view;

    /**
     * Create the game and initialize its internal map.
     */
    public Game() {
        this(null);
    }

    public Game(Level level) {
        this.level = level;
        pastCommands = new Stack<>();
        futureCommands = new Stack<>();
    }

    /** Notify that the last command was impure.
     *
     * Use this if you are overriding handleCommand. */
    protected void setModelChanged(boolean hasChanged) {
        this.modelHasChanged = hasChanged;
    }

    /** Return whether the last executed command was impure. */
    protected boolean hasModelChanged() {
        return modelHasChanged;
    }

    /** Main play routine.
     *
     * Loops until user wants to exit */
    public void play() {
        runGameStartHook();

        while (!wantToQuit) {
            setModelChanged(false);
            handleCommand(getNextCommand());
        }

        runGameExitHook();
    }

    /** Signal main event loop to quit after it has finished processing the
     * current command (if it is currently processing one) or the next command
     * (if it is blocked waiting for a command). */
    public void quit() {
        wantToQuit = true;
    }
    
    // Don't need pre/post turn/command hooks. Override this.
    public void handleCommand(Command command) {
        if (command.execute() && command instanceof ImpureCommand) {
            futureCommands = new Stack<>();
            pastCommands.push((ImpureCommand) command);
        }
    }
    
    /**
     * Returns the current player of the game
     */
    public Player getPlayer() {
        return level.getPlayer();
    }

    /** Remove and return the last executed command. */
    public ImpureCommand popPastCommand() {
        return pastCommands.pop();
    }

    /** Add a command to the history of past commands. */
    public void pushPastCommand(ImpureCommand command) {
        pastCommands.push(command);
    }
    
    /** @return true if there is at least one command in the history. */
    public boolean hasPastCommands() {
        return !pastCommands.isEmpty();
    }

    /** Remove and return the most recently undone command. */
    public ImpureCommand popFutureCommand() {
        return futureCommands.pop();
    }

    /** Add a command to the list of commands undone. */
    public void pushFutureCommand(ImpureCommand command) {
        futureCommands.push(command);
    }
    
    /** @return true if there is at least one command that was undone and has
     * not yet been redone. */
    public boolean hasFutureCommands() {
        return !futureCommands.isEmpty();
    }

    /**
     * Block until a {@code GameEvent} is available and return it.
     *
     * An action can be a command typed on the command line, a mouse click,
     * etc. */
    public abstract Command getNextCommand();

    /** Override this method for code that you wish to be executed at the
     * beginning of play().
     *
     * Necessary for hooks that are to be run after instantiation but don't go
     * in the constructor. */
    public abstract void runGameStartHook();

    /** Override this method for code that you wish to be executed at the end
     * of play(). */
    public abstract void runGameExitHook();
}
