package yetanotherbardtale.controller.threed;

import java.util.LinkedList;
import javax.swing.JOptionPane;

import yetanotherbardtale.controller.Game;
import yetanotherbardtale.controller.Level;
import yetanotherbardtale.controller.command.Command;
import yetanotherbardtale.controller.input.ThreeDController;
import yetanotherbardtale.graphics.FramerateTimer;
import yetanotherbardtale.graphics.RenderManager;
import yetanotherbardtale.imagelib.AsyncImageLoader;
import yetanotherbardtale.imagelib.ImageLibrary;
import yetanotherbardtale.view.ThreeDView;
import yetanotherbardtale.view.View;

/**
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class ThreedGame extends Game {
    private RenderManager manage;
    private FramerateTimer timer;
    private ThreeDController control;
    private ImageLibrary lib;
    private LinkedList<Command> stack;
    protected View view;
    
    /** Play a '3D' game on the example level. */
    public ThreedGame() {
        this(Level.loadExampleLevel());
    }

    /** @param level the level on which to play a '3D' game. */
    public ThreedGame(Level level) {
        super(level);
        lib = new ImageLibrary();
        timer = new FramerateTimer();
        stack = new LinkedList<>();
    }
    
    /** Add a command to the stack of commands to execute.
     *
     * Intended to be called by view code. */
    public void passEventFromView(Command c) {
        stack.addLast(c);
    }
    
    /** Pause the game, handle the command, and then unpause it. */
    @Override
    public void handleCommand(Command c) {
        timer.setPaused(true);
        super.handleCommand(c);
        timer.setPaused(false);
    }
    
    @Override
    public Command getNextCommand() {
        while(stack.isEmpty()); // FIXME: Busy waits.
        return stack.pop();
    }

    @Override
    public void runGameStartHook() {
        manage = new RenderManager(timer);
        Thread t = new Thread(timer);
        t.setName("FramerateTimer");
        t.setDaemon(false);
        t.start();
        
        AsyncImageLoader.loadImageLibrary(lib, manage, "images.zip");        
        
        view = new ThreeDView(this, lib, level.getLocationStore(), level.getImageMapping());
        control = new ThreeDController((ThreeDView)view);
        manage.addRenderable((ThreeDView)view);
        manage.addInputListener(control);
    }

    /** Upon exit, pause the game. */
    @Override
    public void runGameExitHook() {
        timer.setPaused(true);
    }
    
    /** Start a new '3D' game.
     *
     * Prompts the user for a custom level file. If non provided, an example is
     * used. */
    public static void main(String[] args) {
        int result = JOptionPane.showConfirmDialog(null, "Do you wish to use a custom level file?");
        if (result == JOptionPane.YES_OPTION) {
            boolean failed;
            Level l = null;
            do {
                failed = false;
                try {
                    l = Level.chooseLevel();
                } catch (Exception e) {
                    failed = true;
                    JOptionPane.showMessageDialog(null, e.toString());
                }
                if (l == null && !failed)
                    System.exit(0);
            } while (failed);

            new ThreedGame(l).play();
        } else if (result == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        } else { // NO_OPTION
            new ThreedGame().play();
        }
    }
}
