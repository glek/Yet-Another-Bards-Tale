package yetanotherbardtale.event;

import java.util.EventListener;

// TODO: Rename to GameEventListener when we get rid of the old GameEvent crud.
public interface GameEventObjectListener extends EventListener {
    public void handle(GameEventObject e);
}
