package yetanotherbardtale.entity.room;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import yetanotherbardtale.entity.Creature;
import yetanotherbardtale.entity.Enemy;
import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.item.Inventory;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.helpers.StringHelper;

/**
 * Represents a room in the game.
 * Each room has both a name and full description.
 * It can also have an arbitrary number of items, and an exit in each
 * direction. 
 * @author Caleb Simpson (100819001)
 * @version 0.1.0 
 */
public class Room extends GameEntity {
    private List<Exit> exits;
    private Enemy enemy;
    private Inventory inventory;

    /**
     * Construct an empty room with no name or description.
     */
    public Room() { this("", ""); }
    
    /**
     * Construct a room with a given name and description
     * @param name the name of the new room
     * @param description the full description of the new room
     */
    public Room(String name, String description) {
        super(name, description);
        inventory = new Inventory(Inventory.STATIC_DYNAMIC);
        exits = new ArrayList<Exit>();
    }
    
    /**
     * @return the current name of the room
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the enemy that is currently in this room.
     * @param enemy The enemy to set.
     */
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
    
    /**
     * @return the enemy currently in this room
     */
    public Creature getEnemy() {
        return enemy;
    }
    
    /**
     * Checks if there is currently an enemy in this room or not.
     * @return true if there is an enemy in this room, and that enemy is not
     * dead, false otherwise
     */
    public boolean hasEnemy() {
        return enemy != null && !enemy.isDead();
    }
    
    /**
     * Add an exit to this room in the specified direction.
     * These exits can then be used by Creatures to move around the world.
     * @param direction the direction of the exit
     * @param room the room to which this exit is linked
     * @param locked whether exit should start as locked or not
     */
    public Exit setExit(String direction, Room room) {
        Exit exit = new Exit(room);
        exit.setName(direction.toLowerCase());
        exits.add(exit);
        return exit;
    }
    
    /**
     * Gets the Room that is linked to the exit in the specified direction.
     * @param direction the direction of exit to search for
     * @return the room linked to the given direction or null if there is no exit there
     */
    public Exit getExit(String direction) {
        for (Exit exit : exits) {
            if (exit.getName().equalsIgnoreCase(direction)) {
                return exit;
            }
        }
        return null;
    }
    
    public Exit[] getExits() {
        return exits.toArray(new Exit[exits.size()]);
    }
    
    /**
     * Checks if this room contains an exit in the given direction.
     * @param dir the direction to check in
     * @return true if the exit exists, false otherwise
     */
    public boolean containsExit(String dir) {
        return getExit(dir) != null;
    }
    
    public void removeExit(String dir) {
        Exit exit = getExit(dir);
        if (exit != null) {
            exits.remove(exit);
        }
    }
    
    /**
     * Chooses a random direction that has an unlocked exit.
     * @return the chosen direction
     */
    public String getRandomUnlockedDirection() {
        Set<String> options = new HashSet<>();
        for (Exit exit : exits) {
            if (!exit.isLocked())
                options.add(exit.getName());
        }
        if(options.isEmpty()) {
            return null;
        }
        Random r = new Random();
        return (String) options.toArray()[r.nextInt(options.size())];
    }
    
    private List<String> getExitNames() {
        List<String> exitNames = new ArrayList<String>();
        for (Exit exit : exits) {
            exitNames.add(exit.getName());
        }
        return exitNames;
    }
    
    /**
     * Adds an item to this room.
     * @param item the item to add
     */
    public void addItem(Item item) {
        inventory.addItem(item);
    }
    
    /**
     * Removes an item from this room.
     * @param item the item to add
     */
    public void removeItem(Item item) {
        inventory.removeItem(item);
    }
    
    /**
     * Checks if there is an item with a given name currently in this room.
     * @param name the name to search for
     * @return true if the item was found, false if it was not
     */
    public boolean containsName(String name) {
        // Do not put in exits as that is **non orthogonal**
        return inventory.containsName(name);
    }
    
    /**
     * Searches for an item in this room with a specified name.
     * @param itemName the name to look for
     * @return the Item with itemName if it is in this room, null otherwise
     */
    public Item findItemByName(String itemName) {
        return inventory.getItemByName(itemName);
    }
    
    public Item[] getItems() {
        return inventory.getInventory();
    }

    /**
     * @return a nicely formatted string representing this room.
     *         Includes the name, full description, exits, and items.
     */
    @Override
    public String toString() {
        String NL = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        result.append(name)
              .append(NL)
              .append(description)
              .append(NL);
        if (hasEnemy()) {
            result.append("In front of you stands a ")
                  .append(enemy.toString())
                  .append(NL);
        }
        result.append("There are exits in the directions: ")
              .append(StringHelper.join(getExitNames(), ", "));
        if (inventory.size() != 0)
              result.append(NL)
                    .append("Items in this room:")
                    .append(NL)
                    .append(inventory.toString());
        return result.toString();
    }

    // Probably will be docked marks for 'breaking' Demeter's law.
    public Inventory getInventory() {
        return inventory;
    }
}
