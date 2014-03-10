package yetanotherbardtale.entity.item;

import yetanotherbardtale.entity.GameEntity;

/**
 * Item class.
 * An Item has a name, description, type, and a ID. The ID is a string that is
 * globally unique to this Item.
 * @author Michael Damian Mulligan (G'lek)
 * @version 0.1.0 
 */
public abstract class Item extends GameEntity {
    public enum Pickupability {
        STATIC, DYNAMIC, DOOR // TODO: convert to this later
    }
    public static final int STATIC = 1;
    public static final int DYNAMIC = 2;
    public static final int DOOR = 4;
    
    /** The type of the Item (static/dynamic).  */
    // FIXME: Rid of.
    protected int type;
    
    protected double weight;
    
    public Item(String name, String description, Double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /** Create a new Item with all attributes set to {@code null}/0. */
    public Item() {
        this(null,null,0.0);
    }
    
    /** Get the type of the item. */
    public int getType() {
        return type;
    }
    
    /**
     * Set the type (for how it can be picked up) of this Item.
     * If the type is not valid, nothing will be set.
     * @param t new type of the item
     */
    // FIXME: Use exceptions instead!
    public void setType(int t) {
        if (t == STATIC || t == DYNAMIC)
            type = t;
    }
    
    public void setWeight(double d) {
        weight = d;
    }
    
    public double getWeight() {
        return weight;
    }
    
    /**
     * Get the Item as a String in the form: NAME COLON SPACE DESCRIPTION.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getName())
         .append(": ")
         .append(getDescription());
        return b.toString();
    }
}
