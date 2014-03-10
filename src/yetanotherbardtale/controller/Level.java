package yetanotherbardtale.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JFileChooser;

import yetanotherbardtale.entity.Enemy;
import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.entity.item.BasicItem;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.entity.item.Weapon;
import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.room.Key;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.view.threed.ImageMapping;
import yetanotherbardtale.view.threed.Location;
import yetanotherbardtale.view.threed.LocationStore;

/**
 * Level keeps track of all of the entities within the game, and the player's position
 * It also saves and loads the level
 * @author Andrew O'Hara (zalpha314)
 * @version 0.3.3
 */
public class Level implements Serializable {

    private static final long serialVersionUID = 1598388967587101028L;
    protected final Player player;
    protected final List<Enemy> enemies;
    protected final List<Room> rooms;
    private final LocationStore store;
    private final ImageMapping mapping;
    
///////////////////////////////// Constructor /////////////////////////////////
    
    private Level() {
        player  = new Player();
        enemies = new ArrayList<Enemy>();
        rooms   = new ArrayList<Room>();
        store   = new LocationStore();
        mapping = new ImageMapping();
    }
    
///////////////////////////////// Editor Accessors ////////////////////////////
    
    public List<GameEntity> getGameEntities() {
        ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
        entities.add(player);
        entities.addAll(rooms);
        entities.addAll(enemies);
        return entities;
    }
    
    public LocationStore getLocationStore() {
        return store;
    }
    
    public ImageMapping getImageMapping() {
        return mapping;
    }
    
    public Room getCurrentRoom() {
        return player.getCurrentRoom();
    }
    
    public Room getRoom(String name) {
        for (Room r : rooms) {
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public List<Room> getRooms() {
        return rooms;
    }
    
///////////////////////////////// Editor Methods //////////////////////////////
    
    public void addItem(String roomName, Item item, Location loc, String imgName) {
        Room room = getRoom(roomName);
        if (room == null) {
            System.err.println("Error adding Item: room '" + roomName + "' does not exist");
        } else {
            store.addLocation(item, loc);
            mapping.addMapping(item, imgName);
            room.addItem(item);
        }
    }
    
    public void addEnemy(String roomName, Enemy enemy, Location loc, String imgName) {
        Room room = getRoom(roomName);
        if (room == null) {
            System.err.println("Error adding enemy: room '" + roomName + "' does not exist");
        } else {
            store.addLocation(enemy, loc);
            mapping.addMapping(enemy, imgName);
            room.setEnemy(enemy);
            enemies.add(enemy);
        }
    }
    
    public void addRoom(Room room, String northImage, String eastImage, String southImage, String westImage) {
        rooms.add(room);
        mapping.addRoomMapping(room, 0, northImage);
        mapping.addRoomMapping(room, 1, eastImage);
        mapping.addRoomMapping(room, 2, southImage);
        mapping.addRoomMapping(room, 3, westImage);
    }
    
    public Exit addExit(Location loc, String name, Room src, Room dest) {
        return addExit(loc, name, src, dest,"door_house","door_house_locked");
    }
    
    public Exit addExit(Location loc, String name, Room src, Room dest, String unlockedImage, String lockedImage) {
        Exit exit = src.setExit(name, dest);
        store.addLocation(exit, loc);
        mapping.addExitMapping(exit, false, unlockedImage);
        mapping.addExitMapping(exit, true, lockedImage);
        
        linkExits(src, exit); // Search for any exits that lead to each other, and link them together
        return exit;
    }
    
    public void linkExits(Room modifiedRoom, Exit modifiedExit) {
        for (Room r : rooms) {
            for (Exit e : r.getExits()) {
                if (e.getDestination() == modifiedRoom) {
                    e.setConverse(modifiedExit);
                    modifiedExit.setConverse(e);
                }
            }
        }
    }
    
    public void setPlayerStart(String roomName) {
        Room room = getRoom(roomName);
        if (room == null) {
            System.err.println("Error setting player start: room '" + roomName + "' does not exist");
        } else {
            player.setRoom(room);
        }
    }
    
    public void removeEntity(Room room, GameEntity entity) {
        if (entity instanceof Room) {
            System.err.println("Please use Level.removeRoom to remove rooms");
        } else if (entity instanceof Exit) {
            room.removeExit(entity.getName());
        } else if (entity instanceof Item) {
            room.removeItem((Item) entity);
        } else if (entity instanceof Enemy) {
            enemies.remove(entity);
            room.setEnemy(null);
        } else {
            System.err.println("Level.removeEntity() does not support removal of type " + entity.getClass().getName());
        }
    }
    
    public void removeRoom(Room room) {
        rooms.remove(room);
    }

////////////////////////////////// Save Methods ///////////////////////////////
    
    public void saveLevel(File file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(this);
        out.close();
    }
    
    public void saveLevel(String path) throws IOException {
        saveLevel(new File(path));
    }
    
    public void saveLevelAs() throws IOException {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            saveLevel(jfc.getSelectedFile());
        // TODO: Indicate failure
    }
    
///////////////////////////////// Load Methods ////////////////////////////////
    
    public static Level loadLevelFrom(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Level level = (Level) in.readObject();
        in.close();
        return level;
    }

    public static Level loadLevelFrom(String path) throws IOException, ClassNotFoundException {
        return loadLevelFrom(new File(path));
    }

    /* Should this even be here? It feels like pollution. */
    public static Level chooseLevel() throws IOException, ClassNotFoundException {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return loadLevelFrom(jfc.getSelectedFile());
        else
            return null;
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    public static Level loadExampleLevel() {
        Level level = new Level();
        
        ///////////////
        // Create rooms
        ///////////////
        Room outside, theater, pub, lab, office;        
        outside = new Room(
                "the main entrance of the university",
                "There's not much to see, just some buildings and roads");
        level.addRoom(outside, "outside3", "outside1", "lab4", "outside2");
        
        theater = new Room(
                "a lecture theater",
                "It's a big lecture theater meant for teaching science! And "
                    + "other things...");
        level.addRoom(theater, "theater2", "theater3", "theater4", "theater1");
        
        pub = new Room("the campus pub",
                       "The pub is cozy and has that 'well worn' feel to it");
        level.addRoom(pub, "pub4", "pub1", "pub2", "pub3");
        
        lab = new Room("a computing lab",
                       "This is where the real magic happens!");
        level.addRoom(lab, "lab2", "lab1", "lab3", "lab4");
        
        office = new Room(
                "the computing admin office",
                "There's a rule around here. Don't piss off the system admin.");
        level.addRoom(office, "lab1", "office1", "office2", "office1");
        
        
        /////////////////////////
        // Add items to the rooms
        /////////////////////////
        Weapon w = new Weapon("Sword", "A sharp sword, be carefuly!", 20, 0.1);
        level.addItem(outside.getName(), w, new Location(50, 50, 50, 50, 0), "sword_large");
        
        w = new Weapon("rusty nail", "don't cut yourself with it!", 20, 0.1);
        level.addItem(outside.getName(), w, new Location(110, 50, 50, 50, 0), "rusty_nail");
        
        Key k = new Key("office key", "The key to the lab office", 0.01, "1AB");
        level.addItem(outside.getName(), k, new Location(170, 50, 50, 50, 0), "key_small");
        
        BasicItem b = new BasicItem("keyboard", "It makes clickety sounds", 0.4);
        level.addItem(theater.getName(), b, new Location(230, 50, 50, 50, 0), "keyboard");
        
        b = new BasicItem("jPhone", "the new jPhone 9000", 0.3);
        level.addItem(pub.getName(), b, new Location(290, 50, 50, 50, 0), "jphone");
        
        b = new BasicItem("Empty Potion", "it has nothing in it :(", 0.1);
        level.addItem(lab.getName(), b, new Location(340, 50, 50, 50, 0), "potion_empty");
        
        b = new BasicItem("enigma piece", "a dark swirling globe", 9000.1);
        level.addItem(office.getName(), b, new Location(390, 50, 50, 50, 0), "enigma_piece");
        
        /////////////////////////
        // Add exits to the rooms
        /////////////////////////
        level.addExit(new Location(200, 250, 48, 64, 1), "East_Exit", outside,theater);
        level.addExit(new Location(250, 180, 48, 62, 2), "South_Exit", outside,lab);
        level.addExit(new Location(200, 180, 48, 64, 3), "West_Exit", outside,pub);
        level.addExit(new Location(200, 180, 48, 64, 3), "West_Exit", theater, outside);
        level.addExit(new Location(200, 250, 48, 64, 1), "East_Exit", pub, outside);
        level.addExit(new Location(250, 250, 48, 64, 3), "North_Exit", lab, outside);
        level.addExit(new Location(200, 180, 48, 64, 3), "West_Exit", office, lab);
        level.addExit(new Location(200, 250, 48, 64, 1), "East_Exit", lab, office);
        
        lab.getExit("East_Exit").setLocked(true, "1AB");

        //////////////
        // Add Enemies
        //////////////
        Enemy e = Enemy.createGhost(office);
        level.addEnemy(office.getName(), e, new Location(300, 200, 100, 100, 0), "Ghost");
        
        level.setPlayerStart(outside.getName()); // start game outside

        return level;
    }

    public static void main(String[] args) throws IOException {
        loadExampleLevel().saveLevelAs();
    }
}
