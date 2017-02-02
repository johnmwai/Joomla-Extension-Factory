package factory.codegen.app;

import factory.Application.NoTheme;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 * Class used to edit application control web graphically.
 *
 * @author John Mwai
 */
public class JEFCanvas extends JPanel implements NoTheme{
/**
 * List of Drawable's.
 */
    private final LinkedList<Drawable> drawables = new LinkedList<>();

    /**
     * Add a {@link Drawable} to the list of
     * <code>Drawable</code>'s.
     *
     * @param drawable The drawable to add.
     */
    public void addDrawable(Drawable drawable) {
        if (!drawables.contains(drawable)) {
            drawables.add(drawable);
        }
    }

    /**
     * Remove a drawable from the list of
     * <code>Drawable</code>'s.
     *
     * @param drawable The drawable to remove.
     */
    public void removeDrawable(Drawable drawable) {
        drawables.remove(drawable);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Drawable d:  drawables){
            d.draw((Graphics2D) g);
        }
    }

    

    /**
     * Interface for anything that would like to use the graphics of this
     * canvass to draw on this canvas.
     */
    public static interface Drawable {

        /**
         * Implement to draw on this canvas.
         *
         * @param g The graphics of this canvas.
         */
        public void draw(Graphics2D g);
    }
}
