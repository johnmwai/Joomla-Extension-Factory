package factory.design;

import factory.Application;
import factory.codegen.app.DrawingManager;
import factory.codegen.app.JEFCanvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class allows things to be displayed on the screen in form of a graph
 * with nodes and edges and also allows simulations.
 *
 * @author John Mwai
 */
public class ApplicationControlGraph extends Box {

    /**
     * The canvas on which this graph renders itself.
     */
    private JEFCanvas canvas;
    /**
     * The ControlWeb delegate
     */
    private ApplicationUnit applicationUnit = new ApplicationUnit();

    /**
     *
     * @return The ApplicationUnit
     */
    public ApplicationUnit getApplicationUnit() {
        return applicationUnit;
    }

    /**
     * Listens to the movements of the mouse
     */
    private class MyMouseListener extends MouseAdapter {

        private DrawingManager dm() {
            return Application.getInstance().getDrawingManager();
        }

        private void turnOnGlassPane() {
            dm().turnOnGlassPane();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            turnOnGlassPane();
            if (!getPerimeterBounds().contains(e.getPoint())) {
                dm().clearDropLocation();
                return;
            }
            /**
             * If the DrawingManager has something to drop accept it
             */
            if (dm().isWithNodeToDrop()) {
                ApplicationSimulatingNode node = dm().getSimulatingNodeToDrop();
                try {
                    applicationUnit.acceptNode(node);
                    testChildLocation(node.getBox(), e.getPoint());
                } catch (FuscardControlWebException ex) {
                    dm().write(ex.getMessage());
                    dm().clearDropLocation();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!getPerimeterBounds().contains(e.getPoint())) {
                return;
            }
            /**
             * If the DrawingManager has something to drop drop it here
             */
            if (dm().isWithNodeToDrop()) {
                ApplicationSimulatingNode node = dm().getSimulatingNodeToDrop();
                try {
                    applicationUnit.addNode(node);
                    dm().setWithNodeToDrop(false);
                    addChild(node.getBox(), e.getPoint());
                } catch (FuscardControlWebException ex) {
                    dm().write(ex.getMessage());
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            turnOnGlassPane();
            setMouseInside(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setMouseInside(false);
        }
    }
    /**
     * The mouse listener delegate
     */
    private MyMouseListener myMouseListener = new MyMouseListener();

    private static boolean hasMouseListener(JEFCanvas canvas,
            MyMouseListener mouseListener) {
        for (MouseListener ml : canvas.getMouseListeners()) {
            if (ml == mouseListener) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create an instance of an ApplicationControlGraph
     */
    public ApplicationControlGraph() {
        /*
         * Setting ApplicationControlGraph box properties
         */
        setDrawsBorder(false);

        addPropertyChangeListener(
                new PropertyChangeListener<BoxPropertyChangeEvent>() {
            JEFCanvas.Drawable drawable = new JEFCanvas.Drawable() {
                @Override
                public void draw(Graphics2D g) {
                    ApplicationControlGraph.this.draw(g);
                }
            };

            @Override
            public void propertyChanged(BoxPropertyChangeEvent event) {
                Box.BoxPropertyType type = event.getType();
                Box source = event.getSource();
                Object newValue = event.getNewValue();
                Object oldValue = event.getOldValue();
                if (type == BoxPropertyType.CANVAS) {
                    if (oldValue != null) {
                        JEFCanvas old = (JEFCanvas) oldValue;
                        old.removeDrawable(drawable);
                        old.removeMouseListener(myMouseListener);
                        old.removeMouseMotionListener(myMouseListener);
                        old.removeMouseWheelListener(myMouseListener);
                    }
                    if (newValue != null) {
                        JEFCanvas _new = (JEFCanvas) newValue;
                        _new.addDrawable(drawable);
                        if (!hasMouseListener(_new, myMouseListener)) {
                            _new.addMouseMotionListener(myMouseListener);
//                            _new.addMouseWheelListener(myMouseListener);
                            _new.addMouseListener(myMouseListener);
                        }
                    }
                }
                if (type == BoxPropertyType.X
                        || type == BoxPropertyType.Y
                        || type == BoxPropertyType.HEIGHT
                        || type == BoxPropertyType.WIDTH
                        || type == BoxPropertyType.FOREGROUND_COLOR
                        || type == BoxPropertyType.BACKGROUND_PAINT) {
                    JEFCanvas c = getCanvas();
                    if (c != null) {
                        c.repaint();
                    }
                }
                if (type == BoxPropertyType.MOUSE_INSIDE) {
                    JEFCanvas c = getCanvas();
                    if (c != null) {
                        c.repaint();
                    }
                }
                if (type == BoxPropertyType.X
                        || type == BoxPropertyType.Y
                        || type == BoxPropertyType.HEIGHT
                        || type == BoxPropertyType.WIDTH) {
                    revalidate();
                }
            }
        });
        /**
         * Add the mouse listener
         */
    }

    public void revalidate() {
        JEFCanvas c = getCanvas();
        if (c != null) {
            int h = getY() + getHeight() + getMarginTop() + getMarginBottom();
            int w = getMarginLeft() + getX() + getWidth() + getMarginRight();
            c.setPreferredSize(new Dimension(w, h));
            c.revalidate();
        }
    }

    /**
     * Create an instance of an ApplicationControlGraph
     *
     * @param canvas The canvas on which to draw the ApplicationControlGraph
     */
    public ApplicationControlGraph(JEFCanvas canvas) {
        this();
        setCanvas(canvas);
    }

    /**
     * @return the canvas
     */
    public JEFCanvas getCanvas() {
        return canvas;
    }

    /**
     * @param canvas the canvas to set
     */
    public final void setCanvas(JEFCanvas canvas) {
        JEFCanvas old = this.canvas;
        this.canvas = canvas;
        if (canvas != old) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.CANVAS, this, old, canvas));
        }
    }

    @Override
    public void draw(Graphics2D g) {
//        g.setClip(getPerimeterBounds());
        super.draw(g); //To change body of generated methods, choose Tools | Templates.
    }
}
