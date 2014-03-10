package yetanotherbardtale.view.threed;

import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.event.ModelStateChange;
import yetanotherbardtale.event.Notification;
import yetanotherbardtale.view.ThreeDView;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.2.1
 */
public class ExitView extends AbstractEntityView {
    
    public ExitView(Exit e, ThreeDView view) {
        super(e, view);
        img = view.getImageLibrary().getImage(view.getMapping().getExitMapping(e.getID(), e.isLocked()));
    }
    
    @Override
    public void handleClickedOn(int x, int y) {
        //Do nothing, handled by room
    }

    @Override
    public void handle(GameEventObject e) {
        if(e instanceof ModelStateChange) {
            Exit exit = (Exit) entity;
            img = parent.getImageLibrary().getImage(parent.getMapping().getExitMapping(exit.getID(), exit.isLocked()));
        }
        if(e instanceof Notification) {
            Notification n = (Notification)e;
            parent.displayText(n.getMessage());
        }
    }
}
