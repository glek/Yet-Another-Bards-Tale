package yetanotherbardtale.imagelib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class ImageLibrary {
    
    private HashMap<String, Image> library;
    
    public ImageLibrary() {
        library = new HashMap<>();
    }
    
    public boolean addImage(String fileName, Image i) {
        File f = new File(fileName);
        String name = f.getName();
        //Strip file type
        name = name.replaceAll("\\..+$", "");
        if(name.isEmpty() || name == null) {
            return false;
        }
        library.put(name, i);
        return true;
    }
    
    public boolean contains(String name) {
        if(library.containsKey(name)) {
            return (library.get(name) != null);
        }
        return false;
    }
    
    public Image getImage(String name) {
        if(!contains(name)) {
            return ImageLibrary.generateDefaultImage();
        }
        return library.get(name);
    }
    
    public Set<String> getImageNames() {
        return library.keySet();
    }
    
    public static Image generateDefaultImage() {
        Image i = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics g = i.getGraphics();
        g.setColor(Color.PINK);
        g.fillRect(0, 0, 100, 100);
        g.dispose();
        return i;
    }
    
    public Image removeImage(String imageName) {
        return library.remove(imageName);
    }
}
