package yetanotherbardtale.view.threed;

import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.event.Notification;
import yetanotherbardtale.view.ThreeDView;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class ItemView extends AbstractEntityView {
    
    public ItemView(Item i, ThreeDView view) {
        super(i, view);
    }
    
    @Override
    public void handleClickedOn(int x, int y) {
        //Do nothing, handled by parent
    }

    @Override
    public void handle(GameEventObject e) {
        if(e instanceof Notification) {
            Notification n = (Notification)e;
            parent.displayText(n.getMessage());
        }
    }
}
