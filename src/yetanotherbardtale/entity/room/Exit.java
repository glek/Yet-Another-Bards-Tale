package yetanotherbardtale.entity.room;

import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;

/**
 * Represents an exit out of a room.
 * Each exit leads to another room and can be locked.
 * @author Caleb Simpson (100819001)
 * @version 0.1.0 
 */
public class Exit extends Item {

    private Room destination;
    private Exit converse;
    private boolean locked;
    private String code;
    
    /**
     * Create a new Exit.
     * @param room the Room that this exit should lead to
     * @param locked a boolean specifying if this exit starts as locked or not
     */
    public Exit(Room room) {
        this.destination = room;
        this.locked = false; // being locked is the exception, not the rule
        this.code = null; // codes must be manually set and are the exception, see above
        // TODO: Add notes on default in javadoc.
    }
    
    /**
     * Set lock state for this room.
     * @param locked true if this door should be locked, false otherwise
     */
    public void setLocked(boolean locked, String code) {
        setLocked(locked, true, code);
    }

    
    public void setLocked(boolean locked, boolean lockConverse, String code) {
        this.locked = locked;
        this.code = code;
        if (lockConverse)
            converse.setLocked(locked, false, code); // need false to avoid infinite loop

    }

    public boolean tryUnlock(String code) {
        if (this.code == code) {
            setLocked(false, code);
            this.notifyListeners(new ModelStateChange(this));
            this.notifyListeners(new Notification(this, "Door was unlocked!", Notification.Type.SUCCESS));
            return true;
        } else {
            this.notifyListeners(new Notification(this, "That key doesn't work here", Notification.Type.FAILURE));
            return false;
        }
    }
    
    public void setCode(String code) {
        setCode(code, true);
    }
    
    public void setCode(String code, boolean setConverse) {
        this.code = code;
        if (setConverse)
            converse.setCode(code, false); // need false to avoid infinite loop
    }
    
    /**
     * @return true if this exit is locked, false otherwise
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * @return the Room that this exit leads to
     */
    public Room getDestination() {
        return destination;
    }
    
    public void setRoom(Room newDestination) {
        destination = newDestination;
    }
    
    /**
     * @return A string representing what is visible through this exit. If it
     *         is locked, no detailed description can be given. Otherwise, the
     *         name of the linking room is given.
     */
    public String toString() {
        if (isLocked()) {
            return "The exit is locked. I cannot see past!";
        } else {
            return "You see the " + destination.getName();
        }
    }

    public boolean isOneWay() {
        return converse == null;
    }
    
    public Exit getConverse() {
        return converse;
    }
    
    public void setConverse(Exit e) {
        converse = e;
    }
    
    public String getCode() {
        return code;
    }
}
