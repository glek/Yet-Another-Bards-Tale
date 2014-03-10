package yetanotherbardtale.event;

import java.util.EventObject;

/**
 * A generic event in the Yet Another Bard Tale game.
 *
 * This otherwise useless subclass of EventObject is only here to uniformly
 * differentiate events under this hierarchy from those that are completely
 * unassociated with the game. */
public abstract class GameEventObject extends EventObject {
    public GameEventObject(Object source) {
        super(source);
    }
}
