package factory.design;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * Allows an edge to be shown on the screen. It can use many line segments to
 * represent the edge. It provides the Edge with an interface to communicate
 * with the user and other displayable entities. Connections also allow the user
 * to organize the edges by grouping the line segments of different connections
 * so that they can be manipulated as one.
 *
 * @author John Mwai
 */
public class Connection implements Displayable {
    /**
     * The pipes that this connection traverses.
     */
    private LinkedList<Pipe> pipes; 
    /**
     * The point where the Connection originates.
     */
    private Point origin;
    /**
     * The Point where the Connection ends.
     */
    private Point end;

    @Override
    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBounds(Rectangle bounds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the pipes
     */
    public LinkedList<Pipe> getPipes() {
        return pipes;
    }

    /**
     * @param pipes the pipes to set
     */
    public void setPipes(
            LinkedList<Pipe> pipes) {
        this.pipes = pipes;
    }

    /**
     * @return the origin
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    /**
     * @return the end
     */
    public Point getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    @Override
    public void draw(Graphics2D g) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * An arrow head marks the end of a Connection. <img src="doc-files/Drawing1.bmp"/>
     */
    public static class ArrowHead{
        /**
         * Width.
         */
        private int width;
        /**
         * Height
         */
        private int height;
        /**
         * Depression.
         */
        private int depression;

        /**
         * @return the width
         */
        public int getWidth() {
            return width;
        }
        /**
         * The tip of the ArrowHead
         */
        private Point tip;
        /**
         * the back of the ArrowHead
         */
        private Point back;

        /**
         * @param width the width to set
         */
        public void setWidth(int width) {
            this.width = width;
        }

        /**
         * @return the height
         */
        public int getHeight() {
            return height;
        }

        /**
         * @param height the height to set
         */
        public void setHeight(int height) {
            this.height = height;
        }

        /**
         * @return the depression
         */
        public int getDepression() {
            return depression;
        }

        /**
         * @param depression the depression to set
         */
        public void setDepression(int depression) {
            this.depression = depression;
        }
        
    }
}
