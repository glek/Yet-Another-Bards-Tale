package yetanotherbardtale.entity;

import yetanotherbardtale.event.Notification;
import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.room.Room;

/**
 * Represents an enemy that the player will have to fight in order to proceed.
 * @author Caleb Simpson (100819001)
 * @version 0.1.0 
 */
public class Enemy extends Creature {
    /**
     * Static method to create a Ghost monster
     * @param startingRoom The room to create the ghost in.
     * @return an Enemy with set up to look like a ghost.
     */
    public static Enemy createGhost(Room startingRoom) {
        Enemy e = new Enemy(startingRoom, "Ghost", "A big white ghost!", 100);
        startingRoom.setEnemy(e);
        return e;
    }
    
    /**
     * Create new Enemy with the given parameters...
     * @param startingRoom The room for the enemy to start in.
     * @param name The name of this enemy.
     * @param description The full description of this enemy.
     * @param health The maximum health for this enemy (Enemy will start at max health).
     */
    public Enemy(Room startingRoom, String name, String description, int health) {
        super(startingRoom, health);
        
        this.name = name;
        this.description = description;
    }
    
    /* TODO: Implement path finding algorithm. I suggest we assemble all rooms
     * in a Map, which would just a graph with just a few extra properties, as
     * well as path finding methods. */

    /**
     * Makes a move in a random direction.
     * The monster will not move if the player is in the same room as them.
     */
    public void randomMove() {
        String direction = currentRoom.getRandomUnlockedDirection();
        if (direction != null) {
            move(direction);
        }
    }
    
    /**
     * Checks if the given name is equal to this enemy's name using a
     * case-insensitive check.
     * @param name The name to compare to.
     */
    public boolean nameIs(String name) {
        return name.toLowerCase().equals(this.name.toLowerCase());
    }
    
    /**
     * To be used instead of the move method inherited by Creature.
     * Moves the monster in the given direction if possible.
     * @param direction The direction to move in.
     */
    public void move(String direction) {
        Exit exit = currentRoom.getExit(direction);
        if (exit != null
              && !exit.isLocked()
              && !exit.getDestination().hasEnemy())
            changeRoom(exit.getDestination());
    }
    
    /**
     * Change to the given room.
     * @param room The room to move to.
     */
    private void changeRoom(Room room) {
        currentRoom.setEnemy(null);
        currentRoom = room;
        currentRoom.setEnemy((Enemy)this);
    }
    
    /**
      * @return a string of the format SPACE ( HEALTH / MAXHEALTH SPACE HP
      *         NEWLINE DESCRIPTION.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(name)
              .append(" (")
              .append(health)
              .append("/")
              .append(maxHealth)
              .append(" HP)\n")
              .append(description);
        return result.toString();
    }
    
    /**
     * Remove oneself from the current room.
     *
     * This game has no corpses. Upon death, monsters just 'disappear'.
     */
    @Override
    public void triggerDeath() {
        this.notifyListeners(new Notification(this, this.name + " was killed!", Notification.Type.INFORMATIONAL));
        //currentRoom.setEnemy(null);
        //currentRoom = null;
    }
}
