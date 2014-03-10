package yetanotherbardtale.imagelib;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import javax.imageio.ImageIO;

import yetanotherbardtale.graphics.RenderManager;
import yetanotherbardtale.view.ImageLoaderProgress;

import java.awt.Image;

/**
 * Class meant to load images in an asynchronized manner.
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class AsyncImageLoader implements Runnable {
    private ImageLibrary toLoad;
    private String fileSource;
    private boolean success = false;
    private boolean complete = false;
    private boolean running = false;
    private int percentLoaded = 0;
    
    public AsyncImageLoader(ImageLibrary lib, String file) {
        toLoad = lib;
        fileSource = file;
    }
    
    public boolean wasSuccessful() {
        return (success && complete);
    }
    
    public boolean isComplete() {
        return complete;
    }
    
    public int getPercentLoaded() {
        return percentLoaded;
    }
    
    public void halt() {
        //If not running, do nothing, else, set running to false
        running = (running)?false:running;
    }
    
    @Override
    public void run() {
        running = true;
        try(ZipFile zipIn = new ZipFile(fileSource)) {
            Enumeration<? extends ZipEntry> e = zipIn.entries();
            int total = zipIn.size();
            int completed = 0;
            while(e.hasMoreElements() && running) {
                percentLoaded = (int)(((double)completed / (double)total) * 100);
                ZipEntry ent = e.nextElement();
                if(ent.isDirectory()) {
                    completed++;
                    continue;
                }
                if(ent.getName().matches("(.+(png))|(.+(PNG))|(.+(jpg))|"
                        + "(.+(JPG))")) {
                    Image i = null;
                    try {
                        i = ImageIO.read(zipIn.getInputStream(ent));
                    }
                    catch(Exception ex) {
                        System.err.println("ImageLibrary.java:65:Failed to load image from ZIP, continuing...");
                    }
                    boolean successLoad = toLoad.addImage(ent.getName(), i);
                    if(!successLoad) {
                        System.err.println("ImageLibrary.java:69:Failed to add image to library, continuing...");
                    }
                }
                completed++;
            }
            //Did we finish or get halted?
            success = running;
        }
        catch(Exception e) {
            StringWriter str = new StringWriter();
            PrintWriter error = new PrintWriter(str);
            e.printStackTrace(error);
            System.err.println("ImageLibrary.java:80:Serious error encountered during image loading:");
            System.err.println(str.toString());
            success = false;
        }
        complete = true;
    }
    
    public static void loadImageLibrary(ImageLibrary lib, RenderManager manage, String fileLocation) {
        AsyncImageLoader imgLoad = new AsyncImageLoader(lib, fileLocation);
        ImageLoaderProgress p = new ImageLoaderProgress(imgLoad);
        manage.addRenderable(p);
        
        Thread img = new Thread(imgLoad);
        img.setName("ImageLoader");
        img.setDaemon(false);
        img.start();
        
        // Wait for loader to finish
        while(!imgLoad.isComplete()) {
            try { Thread.sleep(100); }
            catch (InterruptedException e)  { e.printStackTrace(); }
        }
        if(!imgLoad.wasSuccessful()) {
            System.exit(-1);
        }
        manage.removeRenderable(p);
    }
    
    public static ImageLibrary LoadImageLibrary(RenderManager manage, String fileLocation) {
        ImageLibrary lib = new ImageLibrary();
        loadImageLibrary(lib, manage, fileLocation);
        return lib;
    }
}
