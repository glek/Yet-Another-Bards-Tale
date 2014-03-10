package yetanotherbardtale.entity.item;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;

/**
 * The inventory is a ordered collection of Items that the player holds.
 *
 * Only Items with the right type can be added to the Inventory. Each Item is
 * considered unique (no stacking of Items of the same type).
 * @author Michael Damian Mulligan (G'lek)
 * @version 0.1.0 
 */
public class Inventory extends GameEntity {
    public enum ItemCompatibility {
        STATIC_ONLY, DYNAMIC_ONLY, ALL; // TODO: Convert all to this.
    }
    /** Flags a GameEntity that can only hold "STATIC" items, i.e., those that
     * can't normally be picked up. */
    public static final int STATIC_ONLY = 1;
    /** Flags a GameEntity that can only hold "DYNAMIC" items, i.e., those that
     * can normally be picked up. */
    public static final int DYNAMIC_ONLY = 2;
    /** Flags a GameEntity that can pick up both "STATIC" and "DYNAMIC" items. */
    public static final int STATIC_DYNAMIC = 3;
    
    /** bit mask of acceptable item types */
    // TODO: Convert to proper type; should be inner enum.
    private int acceptedItems;
    // Inventory list
    private List<Item> inv;
    /** Flags an an inventory that has no weight limit. */
    private static final int NO_MAX_WEIGHT = -1;
    /** maximum aggregate weight of the inventory */
    private double maxWeight = NO_MAX_WEIGHT;
    /** current aggregate weight of the inventory */
    private double weight = 0;
     
     /**
      * Creates a new inventory with the specified restrictions on items that
      * can be picked up.
      * @param acceptedItems bit mask of required items as an integer
      */
     public Inventory(int acceptedItems) {
         inv = new ArrayList<>();
         this.acceptedItems = acceptedItems;
     }
     
     /**
      * Add an Item to the inventory.
      * @param i The Item to add.
      */
     public void addItem(Item i) {
         inv.add(i);
         weight += i.getWeight();
         this.notifyListeners(new ModelStateChange(this));
         this.notifyListeners(new Notification(this, "Picked up the " + i.getName(), Notification.Type.SUCCESS));
     }
     
     public boolean canAdd(Item i) {
         return maxWeight == -1
             || ((i.getType() & acceptedItems) == i.getType()
              && !inv.contains(i)
              && ((weight + i.getWeight()) <= maxWeight));
             
     }

     public int size() {
         return inv.size();
     }
     
     /** @return a reference to the first item in the inventory with the name
      * {@code s} or {@code null} if no such item. */
     public Item getItemByName(String s) {
         if(!containsName(s))
             return null;

         for(Item i : inv) {
             if(i.getName().equalsIgnoreCase(s)) {
                 return i;
             }
         }
         return null;
     }
     
     /**
      * Remove an Item from the Inventory.
      * The inventory will decrease in weight appropriately.
      * @param i the item to attempt to remove from the inventory.
      */
     public void removeItem(Item i) {
         inv.remove(i);
         weight -= i.getWeight();
         this.notifyListeners(new ModelStateChange(this));
         this.notifyListeners(new Notification(this, "Dropped the " + i.getName(), Notification.Type.SUCCESS));
     }
     
     /** @return true if the specified item can be removed from the inventory;
      * currently only whether that item is actually present there. */
     public boolean canRemove(Item i) {
         return inv.contains(i);
     }
     
     /** Remove the first Item in the inventory with name {@code s}. */
     public void removeItemByName(String s) {
         for(Item i : inv) {
             if(i.getName().equalsIgnoreCase(s)) {
                 removeItem(i);
             }
         }
     }
     
     /** @return true if an item with the specified name can be removed from
      * the inventory; currently only whether such an item is actually present
      * there. */
     public boolean canRemoveItemByName(String s) {
         for(Item i : inv) {
             if(i.getName().equalsIgnoreCase(s)) {
                 return canRemove(i);
             }
         }
         return false;
     }
     
     /** Clear the Inventory of all items. */
     public void resetInventory() {
         inv.clear();
         weight = 0;
     }
     
     public double getWeight() {
         return weight;
     }
     
     public double getMaxWeight() {
         return maxWeight;
     }
     
     public void setMaxWeight(double d) {
         maxWeight = d;
     }
     
     /**
      * Check whether or not an Item is in the inventory.
      * @param i the Item to search for
      * @return true if {@code i} is present in the inventory.
      */
     public boolean contains(Item i) {
         return inv.contains(i);
     }
     
     /**
      * Check whether or not an Item with the specified name is in the
      * inventory.
      * @param s the name of the Item to search for
      * @return true if an Item with name {@code s} is present in the
      *         inventory.
      */
     public boolean containsName(String s) {
         for(Item i : inv) {
             if(i.getName().equalsIgnoreCase(s))
                 return true;
         }
         return false;
     }
     
     /** Get the array representation of this Inventory. */
     public Item[] getInventory() {
         return inv.toArray(new Item[inv.size()]);
     }

    /** Prints out a newline-separated list of all in the inventory. */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        Iterator<Item> it = inv.iterator();
        while(it.hasNext()) {
            Item i = it.next();
            b.append(i.toString());
            if(it.hasNext()) {
                b.append("\n");
            }
        }
        return b.toString();
    }
}
