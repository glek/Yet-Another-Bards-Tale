package yetanotherbardtale.entity.item;

/**
 * A simple item that can be picked up.
 * This class is useful for dummy or placeholder items.
 * @author Michael Damian Mulligan (G'lek)
 * @version 0.1.0 
 */
public class BasicItem extends Item {
    /**
     * Create a new dummy item.
     * @param n name of the item
     * @param d description of the item
     * @param w weight of the item */
    public BasicItem(String n, String d, double w) {
        super(n, d, w);
        this.setType(Item.DYNAMIC);
    }
}
