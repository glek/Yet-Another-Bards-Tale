package yetanotherbardtale.controller.text;

import java.util.regex.Pattern;

/**
 * This enum encapsulates all of the valid commands that can 
 * be read into the game from the user.
 *
 * @author Michael Damian Mulligan (G'lek), Andrew O'Hara (zalpha314)
 * @version 0.1.1 
 */
public enum CommandWord {
    /* TODO: Clean up and specify case insensitivity where this is called. Java
     * has no construct to specify case insensitivity in the regex itself
     * without using character classes. */
    EXAMINE ("^examine.+$",
             "^examine",
             "Examine <entity/direction> - take a closer look at <entity> or the room in <direction>."),
    MOVE    ("^move.+$",
             "^move",
             "Move <direction> - try to move in the given direction"),
    PICKUP  ("^pickup.+$",
             "^pickup",
             "Pickup <item> - try to pick up the given item in the room"),
    DROP    ("^drop.+$",
             "^drop",
             "Drop <item> - attempt to drop the given item from your inventory"),
    USE     ("^use.+$",
             "^use",
             "Use <item> on <entity> - Try to use an item in your inventory on an entity in the room"),
    USE_ON  ("^use.+on.+$",
             "^((use)|(on))",
             "Use <item> - Try to use an item in the room or in your inventory"),
    UNDO    ("^undo$",
             "^undo",
             "Undo - Try to undo the last action performed"),
    REDO    ("^redo$",
             "^redo",
             "Redo - Try to redo the last action undone. Cannot redo if you undid, and then performed an action."),
    HELP_   ("^help.+$",
             "^help",
             "You're funny."),
    HELP    ("^help$",
             "",
             "Help - prints general help information"),
    STATUS  ("^status$",
             "",
             "Status - Shows your current status in the game"),
    QUIT    ("^quit$",
             "",
             "Quit - Quits the game");
    
    private String matchRegex, splitRegex, description;
    
    private CommandWord(String match, String split, String description) {
        this.matchRegex = match;
        this.splitRegex = split;
        this.description = description;
    }

    /**
     * Try to parse the commandString and return the CommandWord
     * Returns null if no match found
     * @param commandString the string to parse
     * @return the CommandWord within the commandString
     */
    public static CommandWord parseLine(String commandString) {
        for(CommandWord w : CommandWord.values()) { // For every commandWord...
            if (w.getMatchRegex()
                 .matcher(commandString)
                 .matches()) // If a CommandWord match has been found
                return w;
        }
        return null;
    }
    
    /**
     * Return Pattern instance to test whether a given string matches this
     * command word.
     */
    public Pattern getMatchRegex() {
        return Pattern.compile(matchRegex, Pattern.CASE_INSENSITIVE);
    }
    
    /**
     * Return Pattern instance to split a given command into the command word
     * and its arguments.
     */
    public Pattern getSplitRegex() {
        return Pattern.compile(splitRegex, Pattern.CASE_INSENSITIVE);
    }
    
    /**
     * Get instructions for the current command
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Return a String containing all of the available commands to give.
     */
    public static String getCommandNames() {
        StringBuffer buf = new StringBuffer();
        buf.append("< ");
        for(CommandWord command : CommandWord.values())
            buf.append(command.toString()
                              .replaceAll("_", " "))
               .append(", ");
        buf.deleteCharAt(buf.length() - 2);
        buf.append(">");
        return buf.toString();
    }
}
