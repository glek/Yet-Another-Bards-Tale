package yetanotherbardtale.entity.item;

import yetanotherbardtale.entity.GameEntity;

/** An item that has no notion of a target or for which the target is implicit. */
public interface UsableItem {
    public boolean use(GameEntity user);
    public void undoUse(GameEntity user);
}
