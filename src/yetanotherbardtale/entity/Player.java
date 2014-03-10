package yetanotherbardtale.entity;

import yetanotherbardtale.entity.item.Inventory;
import yetanotherbardtale.event.Notification;

/**
 * The player is the representation of the game's player, including what room
 * they are in, what direction they are facing (if applicable), and their
 * inventory.
 * @author Michael Damian Mulligan (G'lek)
 * @version 0.1.0 
 */
public class Player extends Creature {
    private Inventory inventory;
    
    //FIX ME: Should be an enum, decides which room face to render
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    
    private int direction = 0; // FIXME: Should have been in 3D view in the first place.
    
    /** Create a new Player with an empty inventory and able to only pick up
     * 'dynamic' items.
     *
     * @see yetanotherbardtale.entity.Creature#Creature */
    public Player() {
        inventory = new Inventory(Inventory.DYNAMIC_ONLY);
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    /** Get the direction in which the player is facing. */
    public int getDirection() {
        return direction;
    }
    
    public void setDirection(int dir) {
        direction = dir;
    }

    /**
     * Delete all items in the player's inventory.
     */
    public void clearInventory() {
        inventory.resetInventory();
    }
    
    @Override
    public void move(String direction) {
        if(currentRoom.hasEnemy()) {
            this.notifyListeners(new Notification(this, "Cannot move, there is "
                    + "an enemy in the room", Notification.Type.FAILURE));
        } else {
            super.move(direction);
        }
    }
    
    /** Do nothing, for the player can in now way receive damage at this point. */
    @Override
    public void triggerDeath() {
        // TODO: Need to change this to real game over
    }

    /** Print player name, health, maximum health, and inventory (the latter
     * only if it is not empty). */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getName() == null ? "Player" : getName())
           .append(": HP=")
           .append(getHealth())
           .append("/")
           .append(getMaxHealth());
        if (inventory.size() != 0)
            buf.append(System.getProperty("line.separator"))
               .append("Inventory: ")
               .append(System.getProperty("line.separator"))
               .append(inventory.toString());

        return buf.toString();
    }

}
