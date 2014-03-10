package yetanotherbardtale.view.threed;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

import yetanotherbardtale.entity.GameEntity;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class LocationStore implements Serializable {
    
    public HashMap<String, Location> loc;
    
    public LocationStore() {
        loc = new HashMap<>();
    }
    
    public void addLocation(GameEntity entity, Location loc) {
        addLocation(entity.getID(), loc);
    }
    
    public void addLocation(String id, Location loc) {
        this.loc.put(id, loc);
    }
    
    public Location getLocation(String id) {
        if(!loc.containsKey(id)) {
            return null;
        }
        return loc.get(id);
    }
    
    public static Location generateDefaultLocation() {
        return new Location(250, 250, 100, 100, 0);
    }
    
    public static void saveLocationStore(OutputStream stream, LocationStore store) {
        try(ObjectOutputStream st = new ObjectOutputStream(stream)) {
            st.writeObject(store);
        }
        catch(Exception e) {
            
        }
    }
    
    public static LocationStore loadStore(InputStream stream) {
        LocationStore result = null;
        try(ObjectInputStream in = new ObjectInputStream(stream)) {
            result = (LocationStore)in.readObject();
        }
        catch(Exception e) {
            
        }
        return result;
    }
}
