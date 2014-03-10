package yetanotherbardtale.entity.item;

import yetanotherbardtale.entity.GameEntity;

/** An item that can act on a game entity. */
public interface TargetingItem {
    public boolean useOn(GameEntity user, GameEntity target);
    public void undoUseOn(GameEntity user, GameEntity target);
}
