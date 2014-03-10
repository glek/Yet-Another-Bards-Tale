package yetanotherbardtale.controller.command.impure;

import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.entity.item.Inventory;
import yetanotherbardtale.event.Notification;
import yetanotherbardtale.event.ModelStateChange;

/** A command to pick up an item.
 *
 * Actually a generalized transfer from one inventory to another. */
public class PickupCommand extends ImpureCommand {
    static {
        commonName = "Pickup";
        description = "Pick up some item in somebody else's inventory.";
    }

    private Item item;
    private Inventory source, destination;
    private String itemName; // We only crawl the source inventory **upon execute()**.

    /** Set up for pick up by directly referring to the items in question. */
    public PickupCommand(Inventory source,
                         Inventory destination,
                         Item item) {
        if (source == null)
            throw new NullPointerException("Source inventory should not be null.");
        else if (destination == null)
            throw new NullPointerException("Destination inventory should not be null.");
        else if (item == null)
            throw new NullPointerException("Cannot pick up null item.");

        this.source = source;
        this.destination = destination;
        this.item = item;
        this.itemName = item.getName();
    }

    /** Set up for pickup by item name. If the item does not exist, it will
     * fail at <em>execution time</em>. */
    public PickupCommand(Inventory source,
                         Inventory destination,
                         String itemName) {
        if (source == null)
            throw new NullPointerException("Source inventory should not be null.");
        else if (destination == null)
            throw new NullPointerException("Destination inventory should not be null.");
        else if (itemName == null)
            throw new NullPointerException("Item name cannot be null.");

        this.source = source;
        this.destination = destination;
        this.item = null;
        this.itemName = itemName;
    }

    /** Transfer the item in question from the source inventory to the
     * destination inventory.
     *
     * @return false if no such item by name exists in the source inventory,
     * false otherwise */
    @Override
    public boolean execute() {
        if (item == null) {
            item = source.getItemByName(itemName);
            if (item == null) {
                source.notifyListeners(new Notification(this,
                                                        "No such item \"" +
                                                            itemName +
                                                            "\".",
                                                        Notification.Type.FAILURE));
                return false;
            }
        }

        source.removeItem(item);
        source.notifyListeners(new ModelStateChange(source));

        if (destination.canAdd(item)) {
            destination.addItem(item);
            destination.notifyListeners(new ModelStateChange(destination));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unExecute() {
        if (!destination.canRemove(item))
            throw new IllegalStateException("Could not undo picking up \"" +
                                            item.getName() +
                                            "\".");
        destination.removeItem(item);
        destination.notifyListeners(new ModelStateChange(destination));
        
        if (!source.canAdd(item))
            throw new IllegalStateException("Could not undo picking up \"" +
                    item.getName() +
                    "\".");
        source.addItem(item);
        source.notifyListeners(new ModelStateChange(destination));
    }
}
