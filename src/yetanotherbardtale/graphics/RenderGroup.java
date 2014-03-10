package yetanotherbardtale.graphics;

/**
 * A group of Renderables. Anything that implements
 * this interface is responsible for ensuring that it renders
 * its Renderables in the right order, if there is any.
 * NOTE: For this project, it is recommended to have a
 * "RoomView" which implements this interface and is responsible
 * for rendering the room. This "RoomView" is added to the
 * RenderManager.
 * @author Michael Damian Mulligan (G'lek)
 * @version glek 0.0.1
 */
public interface RenderGroup extends Renderable {
    public void addRenderable(Renderable r);
    public void removeRenderable(Renderable r);
    public void clearGroup();
}
