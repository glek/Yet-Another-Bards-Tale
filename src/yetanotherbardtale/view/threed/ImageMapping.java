package yetanotherbardtale.view.threed;

import java.io.Serializable;
import java.util.HashMap;

import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.room.Room;

/**
 * A mapping from game entity IDs to the image path used to render them.
 *
 * This avoids poluting core model code with data that is specific to the view.
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class ImageMapping implements Serializable {
    
    private HashMap<String, String> mapping;
    
    public ImageMapping() {
        mapping = new HashMap<>();
    }
    
    public void addMapping(GameEntity entity, String img) {
        addMapping(entity.getID(), img);
    }
    
    public void addMapping(String id, String img) {
        mapping.put(id, img);
    }
    
    public void addRoomMapping(Room room, int direction, String img) {
        addRoomMapping(room.getID(), direction, img);
    }
    
    public void addRoomMapping(String id, int direction, String img) {
        addMapping(id + "-" + Integer.toString(direction), img);
    }
    
    public void addExitMapping(Exit exit, boolean locked, String img) {
        addExitMapping(exit.getID(), locked, img);
    }
    
    public void addExitMapping(String id, boolean locked, String img) {
        addMapping(id + "-" + ((locked)?("lock"):("unlock")), img);
    }
    
    public String getMapping(String id) {
        if(!mapping.containsKey(id)) {
            return "NULL";
        }
        return mapping.get(id);
    }
    
    public String getRoomMapping(String id, int direction) {
        String i = id + "-" + Integer.toString(direction);
        return getMapping(i);
    }
    
    public String getExitMapping(String id, boolean locked) {
        String i = id + "-" + ((locked)?("lock"):("unlock"));
        return getMapping(i);
    }
}
