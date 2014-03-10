package yetanotherbardtale.entity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import yetanotherbardtale.event.GameEventObject;
import yetanotherbardtale.event.GameEventObjectListener;

/**
 * Anything that is going to be put into the game.
 * @author Michael Damian Mulligan (G'lek), Andrew O'Hara (zalpha314)
 * @version 0.2.0
 */
public abstract class GameEntity implements Serializable {
    private static int nextID = 1;
    private String id; /* not final because it needs to be set outside of the
                        * constructor */
    protected String name, description;
    
    private List<GameEventObjectListener> listeners;
    
    /**
     * Create a GameEntity with the given information
     * @param name a human-readable name for this entity, such as "red ghost"
     * @param description a human-readable description for this entity
     * @param id a globally-unique identifier for a particular entity
     */
    public GameEntity(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    /**
     * Create a blank GameEntity
     */
    protected GameEntity() {
        listeners = new LinkedList<>();
        id = nextId();
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(id);
        out.writeObject(name);
        out.writeObject(description);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (listeners == null)
            listeners =  new LinkedList<GameEventObjectListener>(); // hacky
        id          = (String) in.readObject();
        name        = (String) in.readObject();
        description = (String) in.readObject();

        nextID++; // TODO: Remove me
    }

    /** Add GameEventListener to the set of listeners.
     *
     * @param l GameEventListener to add to the set */
    public void addListener(GameEventObjectListener l) {
        listeners.add(l);
    }
    
    /** Remove GameEventListener to the set of listeners.
     *
     * @param l GameEventListener to remove from the set */
    public void deleteListener(GameEventObjectListener l) {
        listeners.remove(l);
    }

    /** Notify all GameEventListeners of a GameEvent.
     *
     * @param e GameEvent to send to all listeners. */
    public void notifyListeners(GameEventObject e) {
        for (GameEventObjectListener l : listeners)
            l.handle(e);
    }
    
    private String nextId() {
        String id = String.format("%03d", nextID);
        nextID++;
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String d) {
        description = d;
    }
    
    public String getID() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof GameEntity) {
            return ((GameEntity)o).getID().equals(getID());
        }
        return false;
    }
}
