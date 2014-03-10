package yetanotherbardtale.view.threed;

/**
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public class Location implements java.io.Serializable {
    
    private int xLoc, yLoc, xScale, yScale, face;
    
    public Location(Location l) {
        this(l.getX(), l.getY(), l.getXScale(), l.getYScale(), l.getFace());
    }
    
    public Location(int x, int y, int dimX, int dimY, int face) {
        xLoc = x;
        yLoc = y;
        xScale = dimX;
        yScale = dimY;
        this.face = face;
    }
    
    public void setPosition(int x, int y) {
        xLoc = x;
        yLoc = y;
    }
    
    public void setScale(int dimX, int dimY) {
        xScale = dimX;
        yScale = dimY;
    }
    
    public void setFace(int face) {
        this.face = face;
    }
    
    public int getX() {
        return xLoc;
    }
    
    public int getY() {
        return yLoc;
    }
    
    public int getXScale() {
        return xScale;
    }
    
    public int getYScale() {
        return yScale;
    }
    
    public int getFace() {
        return face;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Location))
            return false;
        Location l = (Location) o;
        return l.xLoc   == xLoc &&
               l.yLoc   == yLoc &&
               l.xScale == xScale &&
               l.yScale == yScale &&
               l.face   == face;
    }
}
