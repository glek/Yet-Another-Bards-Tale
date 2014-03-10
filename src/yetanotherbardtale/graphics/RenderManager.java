package yetanotherbardtale.graphics;

import java.awt.event.ComponentEvent;
import javax.swing.JOptionPane;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

import yetanotherbardtale.controller.input.InputAdapter;

import java.util.List;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

/**
 * The manager that handles actually rendering the graphics to the screen.
 *
 * The components are rendered in no specific order. It is the duty of
 * Renderable to draw things in the right order.
 *
 * It is recommended to only have one thing (the room) being rendered at any
 * given time to prevent things being rendered out of order. See RenderGroup
 * for more information.
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class RenderManager implements Observer {
    //Components to render
    private List<Renderable> components = new ArrayList<>();
    private FramerateTimer timer = null;
    private JFrame mainWindow = null;
    private Canvas renderArea = null;
    private BufferStrategy bufferStrat = null;
    private VolatileImage bufferImg = null;
    private boolean isFullScreen = false;
    private boolean buffersReady = false;
    private static final int RESIZE_WAIT = 500;
    private int resizeTimeLeft = 0;
    public boolean enabled = true;

    public RenderManager(FramerateTimer t) {
        this(new JFrame("DefaultFrame"), t);
    }
    
    public RenderManager(JFrame f, FramerateTimer t) {
        timer = t;
        timer.addObserver(this);
        mainWindow = f;
        renderArea = new Canvas();
        renderArea.setIgnoreRepaint(true);
        mainWindow.add(renderArea);
        renderArea.setSize(500, 500);
        mainWindow.validate();
        mainWindow.setVisible(true);
        java.awt.Insets inset = mainWindow.getInsets();
        mainWindow.setSize(500 + (inset.left + inset.right), 500 + (inset.bottom + inset.top));
        mainWindow.validate();
        mainWindow.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainWindow.addComponentListener(new java.awt.event.ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                resizeTimeLeft = RESIZE_WAIT;
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                
            }

            @Override
            public void componentShown(ComponentEvent e) {
                
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                
            }
        });
        mainWindow.addWindowListener(new WindowListener() {

            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(
                        null,
                        "Do you really want to close the game?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                        mainWindow.dispose();
                        System.exit(0);
                }
            }
            
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
            
        });
        generateBuffer();
    }
    
    private void generateBuffer() {
        buffersReady = false;
        GraphicsEnvironment environ = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environ.getDefaultScreenDevice();
        bufferStrat = null;
        bufferImg = null;
        renderArea.createBufferStrategy(2);
        bufferStrat = renderArea.getBufferStrategy();
        bufferImg = device.getDefaultConfiguration().createCompatibleVolatileImage(500, 500);
        buffersReady = true;
    }
    
    public void setTitle(String s) {
        mainWindow.setTitle(s);
    }
    
    public void addRenderable(Renderable r) {
        components.add(r);
    }
    
    public void removeRenderable(Renderable r) {
        components.remove(r);
    }
    
    public void clearManager() {
        components.clear();
    }
    
    public boolean canFullScreen() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported();
    }
    
    public boolean isFullScreen() {
        return isFullScreen;
    }
    
    public void setFullScreen(boolean arg) {
        GraphicsEnvironment environ = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environ.getDefaultScreenDevice();
        if(arg == isFullScreen) {
            return;
        }
        if(!isFullScreen) {
            device.setFullScreenWindow(null);
            renderArea.setSize(640, 480);
            mainWindow.validate();
            generateBuffer();
        }
        else {
            try {
                renderArea.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
                mainWindow.validate();
                generateBuffer();
                device.setFullScreenWindow(mainWindow);
            }
            catch(Exception e) {
                device.setFullScreenWindow(null);
                renderArea.setSize(640, 480);
                mainWindow.validate();
                generateBuffer();
            }
        }
        isFullScreen = arg;
    }
    
    @Override
    public void update(Observable source, Object arg) {
        if (!enabled) return;
        
        boolean lost = false;
        boolean restored = false;
        Graphics g;
        do {
            do {
                /*
                 * Make sure we have fresh buffers
                 * This is a bit expensive on resources, but
                 * it saves headaches that result from
                 * resizing the window.
                 */
                generateBuffer();
                g = bufferImg.createGraphics();
                for(Renderable r : components) {
                    r.render(g);
                }
                if(bufferStrat == null) {
                    lost = bufferImg.contentsLost();
                }
                else {
                    lost = bufferStrat.contentsRestored();
                }
            } while(lost);
            if(bufferStrat != null) {
                restored = bufferStrat.contentsLost();
            }
        } while(restored);
        g.dispose();
        if(resizeTimeLeft > 0) {
            resizeTimeLeft -= 1000 / timer.getFramerate();
            return;
        }
        if(bufferStrat == null) {
            drawVolatileImage();
        }
        else {
            drawBufferStrategy();
        }
    }
    
    private void drawVolatileImage() {
        do {
            Graphics g2 = renderArea.getGraphics();
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, renderArea.getWidth(), renderArea.getHeight());
            int w, h;
            if(renderArea.getHeight() - bufferImg.getHeight() > renderArea.getWidth() - bufferImg.getWidth()) {
                w = h = renderArea.getHeight();
            }
            else {
                w = h = renderArea.getWidth();
            }
            renderArea.setSize(w, h);
            mainWindow.pack();
            g2.drawImage(bufferImg, 0, 0, w, h, null);
            g2.dispose();
        } while(bufferImg.contentsLost());
    }
    
    private void drawBufferStrategy() {
        do {
            do {
                Graphics g2 = bufferStrat.getDrawGraphics();
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, renderArea.getWidth(), renderArea.getHeight());
                int w, h;
                if(renderArea.getHeight() - bufferImg.getHeight() > renderArea.getWidth() - bufferImg.getWidth()) {
                    w = h = renderArea.getHeight();
                }
                else {
                    w = h = renderArea.getWidth();
                }
                renderArea.setSize(w, h);
                mainWindow.pack();
                g2.drawImage(bufferImg, 0, 0, w, h, null);
                g2.dispose();
            } while(bufferStrat.contentsRestored());
            bufferStrat.show();
        } while(bufferStrat.contentsLost());
    }
    
    public void addInputListener(InputAdapter listener) {
        renderArea.addMouseListener(listener);
        renderArea.addMouseMotionListener(listener);
        renderArea.addKeyListener(listener);
    }
    
    public Canvas getRenderArea() {
        return renderArea;
    }
    
    public void setVisible(boolean visible) {
        mainWindow.setVisible(visible);
    }
}
