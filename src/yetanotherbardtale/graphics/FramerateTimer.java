package yetanotherbardtale.graphics;

import java.util.Observable;

/**
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class FramerateTimer extends Observable implements Runnable {

    private int framerate;
    private boolean paused;
    
    public FramerateTimer() {
        //Default framerate.
        framerate = 10;
        paused = false;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            
            @Override
            public void run() {
                setPaused(true);
            }
        }));
    }
    
    public void setPaused(boolean arg) {
        paused = arg;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public void setFramerate(int arg) {
        framerate = arg;
    }
    
    public int getFramerate() {
        return framerate;
    }
    
    @Override
    public void run() {
        while(true) {
            if(isPaused()) {
                try {
                    Thread.sleep(1000 / framerate);
                    continue;
                }
                catch(Exception e) {
                    continue;
                }
            }
            long start = System.currentTimeMillis();
            setChanged();
            try {
                notifyObservers();
            }catch (java.util.ConcurrentModificationException | IllegalStateException ex) {
                // Ignored.  These are due to Timer invoking update at the same time as the user
            }
            long end = System.currentTimeMillis();
            long diff = end - start;
            long sleep = 1000 / framerate; //1000 milliseconds per second
            sleep -= diff;
            if(sleep <= 0) {
                continue;
            }
            else {
                try {
                    Thread.sleep(sleep);
                }
                catch(Exception e) {
                    
                }
            }
        }
    }
}
