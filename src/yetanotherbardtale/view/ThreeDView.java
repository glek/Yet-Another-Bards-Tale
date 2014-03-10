package yetanotherbardtale.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.util.Observable;

import yetanotherbardtale.controller.Game;
import yetanotherbardtale.controller.command.Command;
import yetanotherbardtale.controller.command.pure.RedoCommand;
import yetanotherbardtale.controller.command.pure.UndoCommand;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.graphics.Renderable;
import yetanotherbardtale.imagelib.ImageLibrary;
import yetanotherbardtale.view.threed.Button;
import yetanotherbardtale.view.threed.ImageMapping;
import yetanotherbardtale.view.threed.InventoryView;
import yetanotherbardtale.view.threed.ItemView;
import yetanotherbardtale.view.threed.LocationStore;
import yetanotherbardtale.view.threed.MiniMap;
import yetanotherbardtale.view.threed.RoomView;
import yetanotherbardtale.view.threed.TextDisplayArea;


/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.2
 */
public class ThreeDView implements View, Renderable {
    private Game game;
    protected final Player player;
    protected RoomView currentRoom;
    private ImageLibrary lib;
    private MiniMap miniMap;
    private Button miniMapButton;
    private Button undoButton;
    private Button redoButton;
    private Button inventoryButton;
    protected Button lookRight;
    protected Button lookLeft;
    private TextDisplayArea text;
    private InventoryView inventory;
    private LocationStore store;
    public static final int MINIMAP_DROP_SPEED = 30;
    private boolean isMiniMapDropped = false;
    private boolean isInventoryRaised = false;
    private int inventoryTranslate = 0;
    private int miniMapTranslate = 0;
    private ItemView selectedItem;
    private ImageMapping mapping;
    
    public ThreeDView(Player player, ImageLibrary lib, LocationStore store, ImageMapping map) {
        this.player = player;
        mapping = map;
        this.lib = lib;
        this.store = store;
        lookRight = new Button("LookRight", 480, 0, 20, 500, lib);
        lookLeft = new Button("LookLeft", 0, 0, 20, 500, lib);
        currentRoom = new RoomView(player, this);
        text = new TextDisplayArea(50, 475, 450, 25, 10);
        miniMap = new MiniMap(player);
        miniMapButton = new Button("Minimap", 450, 0, 50, 25, lib);
        undoButton = new Button("Undo", 0, 0, 50, 25, lib);
        redoButton = new Button("Redo", 50, 0, 50, 25, lib);
        inventoryButton = new Button("Inventory", 0, 475, 50, 25, lib);
        inventory = new InventoryView(player, 0, 500, 500, 100, this);
    }
    
    public ThreeDView(Game game, ImageLibrary lib, LocationStore store, ImageMapping map) {
        this(game.getPlayer(), lib, store, map);
        this.game = game;
    }
    
    public void passEventToGame(Command e) {
        if (game == null) {
            System.err.println("ThreeDView.passEventToGame() - null game");
        } else {
            game.handleCommand(e);
        }
    }
    
    public void setSelectedItem(ItemView v) {
        selectedItem = v;
    }
    
    public ItemView getSelectedItem() {
        return selectedItem;
    }
    
    public LocationStore getLocationStore() {
        return store;
    }
    
    public ImageLibrary getImageLibrary() {
        return lib;
    }
    
    public ImageMapping getMapping() {
        return mapping;
    }
    
    @Override
    public void render(Graphics g) {
        currentRoom.render(g);
        VolatileImage image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleVolatileImage(500, 500);
        Graphics g2 = image.createGraphics();
        Graphics2D g2d = (Graphics2D)g;
        lookRight.render(g);
        lookLeft.render(g);
        if(isInventoryRaised && inventoryTranslate < 100) {
            inventoryTranslate += MINIMAP_DROP_SPEED;
            if(inventoryTranslate > 100) {
                inventoryTranslate = 100;
            }
        }
        else if(!isInventoryRaised && inventoryTranslate > 0) {
            inventoryTranslate -= MINIMAP_DROP_SPEED;
            if(inventoryTranslate < 0) {
                inventoryTranslate = 0;
            }
        }
        inventoryButton = new Button("Inventory", 0, 475 - inventoryTranslate, 50, 25, lib);
        text.setPoint(50, 475 - inventoryTranslate);
        inventoryButton.render(g);
        text.render(g);
        inventory.translate(0, 500 - inventoryTranslate);
        inventory.render(g);
        undoButton.render(g);
        redoButton.render(g);
        miniMap.render(g2);
        AffineTransform origin = g2d.getTransform();
        g2d.translate(0, -500);
        if(isMiniMapDropped && miniMapTranslate < 500) {
            miniMapTranslate += MINIMAP_DROP_SPEED;
            if(miniMapTranslate > 500) {
                miniMapTranslate = 500;
            }
        }
        else if(!isMiniMapDropped && miniMapTranslate > 0) {
            miniMapTranslate -= MINIMAP_DROP_SPEED;
            if(miniMapTranslate < 0) {
                miniMapTranslate = 0;
            }
        }
        g2d.translate(0.0, miniMapTranslate);
        g2d.drawImage(image, 0, 0, 500, 500, null);
        g2d.setTransform(origin);
        miniMapButton = new Button("Minimap", 450, (miniMapTranslate > 475)?475:miniMapTranslate, 50, 25, lib);
        miniMapButton.render(g);
    }

    @Override
    public void displayText(String arg) {
        text.setText(arg);
    }

    @Override
    public boolean isClickInside(int x, int y) {
        return false;
    }

    @Override
    public void handleClickedOn(int x, int y) {
        //Check top-level for clicks
        //Minimap/Inventory/Undo/Redo buttons
        if(undoButton.isClickInside(x, y)) {
            //this.passEventToGame(new UndoCommand(this));
            if (!(new UndoCommand(game)).execute())
                displayText("Cannot undo last command. Was there a last command?");

        } else if(redoButton.isClickInside(x, y)) {
            if (!(new RedoCommand(game)).execute())
                displayText("Cannot redo last undone command. Was there an undone command?");

        } else if(miniMapButton.isClickInside(x, y)) {
            isMiniMapDropped = !isMiniMapDropped;
            
        } else if(inventoryButton.isClickInside(x, y)) {
            isInventoryRaised = !isInventoryRaised;

        } else if(lookRight.isClickInside(x, y)) {
            player.setDirection((player.getDirection() + 1 > 3)?(0):(player.getDirection() + 1));
            currentRoom.update(player);

        } else if(lookLeft.isClickInside(x, y)) {
            player.setDirection((player.getDirection() - 1 < 0)?(3):(player.getDirection() - 1));
            currentRoom.update(player);

        } else if(inventory.isClickInside(x, y)) {
            inventory.handleClickedOn(x, y);

        } else {
            currentRoom.handleClickedOn(x, y);
        }
    }

    @Override
    public void displayError(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        currentRoom.update(player);        
    }
}
