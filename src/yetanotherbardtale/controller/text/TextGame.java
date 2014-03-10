package yetanotherbardtale.controller.text;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import yetanotherbardtale.controller.Game;
import yetanotherbardtale.controller.Level;
import yetanotherbardtale.controller.command.Command;
import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.event.GameEventObjectListener;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;

/** A console-based game.
 *
 * REPL-driven. */
public class TextGame extends Game implements GameEventObjectListener {
    private final Scanner inputScanner; // Retrieves lines from the user to parse into commands
    private final String PROMPT = "(yet-another-bard-tale) ";
    private CommandFactory factory;

    public TextGame(Level level) {
        super(level);
        inputScanner = new Scanner(System.in);
        factory = new CommandFactory(this);
    }

    private TextGame() {
        this(null);
    }

    @Override
    public Command getNextCommand() {
        System.out.print(PROMPT);
        if (inputScanner.hasNextLine()) {
            String commandLine = inputScanner.nextLine();
            /* No way to get rid of or bypass the event factory for commands
             * that don't have side-effects (i.e., EXAMINE), so we just
             * encapsulate these within an event. Bloated, but the bloat stops
             * at Player. */
            return factory.parseLine(commandLine);
        } else {
            System.out.println("");
            return factory.parseLine("QUIT");
        }
    }

    @Override
    public void runGameStartHook() {
        String sep = System.getProperty("line.separator");
        System.out.println(
            sep
            + "Welcome to Yet Another Bard's Tale!" + sep
            + "Yet Another Bard's tale, is yet another tale of a bard." + sep
            + "Type 'help' if you're an inexperienced bard." + sep
            + sep);
        new StatusCommand(this).execute();
        
        getPlayer().addListener(this);
        getPlayer().getInventory().addListener(this);
        getPlayer().getCurrentRoom().addListener(this);
    }

    @Override
    public void runGameExitHook() { }

    @Override
    public void handle(GameEventObject e) {
        if (e instanceof Notification) {
            Notification n = (Notification) e;
            if (n.getType() == Notification.Type.FAILURE)
                System.err.println(n.getMessage());
            else
                System.out.println(n.getMessage());
        } else if (e instanceof ModelStateChange) {
            setModelChanged(true);
        }
    }

    @Override
    public void handleCommand(Command command) {
        super.handleCommand(command);
        if (hasModelChanged())
            new StatusCommand(this).execute();
    }

    public static void main(String[] args) {
        Level level = null;
        for (int i=0; i<args.length; i++) {
            switch (args[i]) {
                default:
                    try {
                        level = Level.loadLevelFrom(args[i]);
                    } catch (FileNotFoundException e) {
                        System.err.println("No such file \"" + args[i] + "\".");
                        System.exit(1);
                    } catch (IOException e) {
                        System.err.println("Could not load level from file \"" + args[i] + "\": " + e.toString());
                        System.exit(1);
                    } catch (ClassNotFoundException e) {
                        System.err.println("Level file is in an invalid format.");
                        System.exit(1);
                    }
            }
        }

        if (level == null)
            new TextGame(Level.loadExampleLevel()).play();
        else
            new TextGame(level).play();
    }
}
