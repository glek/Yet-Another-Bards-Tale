package yetanotherbardtale.entity;

import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;

/**
 * A creature is something that is able to move between rooms.
 *
 * It also has health and can attack/be attacked.
 * @author Caleb Simpson (100819001)
 * @version 0.1.0 
 */
public abstract class Creature extends GameEntity {
    /**
     * The max health this creature can possess.
     */
    protected double maxHealth;
    /**
     * The creature's current health
     */
    protected double health;
    /**
     * The room the creature is in.
     */
    protected Room currentRoom;
    
    public Creature() {
        this(null, 0);
    }
    
    /**
     * Construct a new creature in the given room with a given amount of health.
     * @param startingRoom The room for this creature to start in.
     * @param maxHealth The maximum health for this creature.
     */
    public Creature(Room startingRoom, double maxHealth) {
        this.currentRoom = startingRoom;
        this.maxHealth = maxHealth;
        
        health = maxHealth;
    }
    
    /**
     * Move in a given direction.
     * Function assumes all checks have already been performed
     * before this command happens.
     * @param direction The direction to try to move.
     */
    public void move(String direction) {
        Exit exit = currentRoom.getExit(direction.toLowerCase());
        this.setRoom(exit.getDestination());
    }
    
    /**
     * Immediately set the room of this creature.
     * @param room The room to move to.
     */
    public void setRoom(Room room) {
        this.currentRoom = room;
    }
    
    /**
     * @return The room this Creature is currently in.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    /**
     * @return The current health of this Creature. 
     */
    public double getHealth() {
        if (health > maxHealth)
            return maxHealth;
        else
            return health;
    }
    
    /**
     * Set the creature's health
     * @param health The health to set the creature to. 
     */
    public void setHealth(double health) {
        this.health = health;
    }
    
    /**
     * Restore an amount of health to this Creature.
     *
     * The health is capped at {@link #maxHealth maxHealth}.
     *
     * @param restore the amount of health to restore
     */
    public void heal(double restore) {
        health += restore;
        this.notifyListeners(new ModelStateChange(this));
        this.notifyListeners(new Notification(this,
                                              this.name
                                                + " was healed by "
                                                + Double.toString(restore),
                                              Notification.Type.INFORMATIONAL));
    }
    
    public void undoHeal(double restore) {
        health -= restore;
        this.notifyListeners(new ModelStateChange(this));
    }
    
    /**
     * Causes an amount of damage to this Creature.
     *
     * If this causes the Creature to reach zero health, the die method will be
     * called.
     *
     * @param damage The amount of damage to deal.
     */
    public void hurt(double damage) {
        if (health > maxHealth)
            health = maxHealth;
        
        health -= damage;
        this.notifyListeners(new ModelStateChange(this));
        this.notifyListeners(new Notification(this,
                                              this.name
                                                + " was hurt by "
                                                + Double.toString(damage),
                                              Notification.Type.INFORMATIONAL));
        if(isDead()) {
            triggerDeath();
        }
    }
    
    public void undoHurt(double damage) {
        health += damage;
        this.notifyListeners(new ModelStateChange(this));
    }
    
    
    /**
     * Get the max health this creature can possess
     */
    public double getMaxHealth() {
        return maxHealth;
    }
    
    /**
     * Set this creature's max health
     * @param health The health value to set as max.
     */
    public void setMaxHealth(double health) {
        maxHealth = health;
    }
    
    /**
     * Check if this creature has no more health left
     * @return True if the creature is dead, false otherwise.
     */
    public boolean isDead() {
        return health < 0; // Why do you keep changing this back!?!??!
    }
    
    /**
     * Perform the action for this creature's death.
     * For a player, this could be go to game over.
     * For a monster, this could be to remove them from
     * the game and unlock the doors.
     */
    public abstract void triggerDeath();
}
