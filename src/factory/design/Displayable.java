package factory.design;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Interface for anything that wants to be put on the screen.
 *
 * @author John Mwai
 */
public interface Displayable {

    /**
     * Get the extents of this thing on the drawing canvas.
     *
     * @return The rectangle in the coordinates system of the canvas that
     * represents the bounds of this Displayable.
     */
    public Rectangle getBounds();

    /**
     * Set the bounds of this displayable.
     *
     * @param bounds The bounds to set.
     */
    public void setBounds(Rectangle bounds);
    /**
     * Draw this Displayable in the graphics context of a Component. 
     * @param g the Graphics.
     */
    public void draw(Graphics2D g);
}
