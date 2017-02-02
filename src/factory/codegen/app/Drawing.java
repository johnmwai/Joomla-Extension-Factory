package factory.codegen.app;

import factory.design.ApplicationControlGraph;
import java.util.LinkedList;

/**
 * A class of Objects that manage the
 *
 * @author John Mwai
 */
public class Drawing {

    private String name;
    /**
     * List of drawing listeners
     */
    private LinkedList<DrawingListener> listeners = new LinkedList<>();
    /**
     * Whether this Drawing is open in the IDE
     */
    private boolean open;
    /**
     * Whether this Drawing is saved.
     */
    private boolean saved;
    /**
     * The canvas to draw on.
     */
    private JEFCanvas canvas;
    /**
     * The graph to draw.
     */
    private ApplicationControlGraph graph;

    /**
     * Add a {@link DrawingListener} to the list of DrawingListener's.
     *
     * @param listener The listener to add.
     */
    public void addDrawingListener(DrawingListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a {@link DrawingListener} from the list of DrawingListener's.
     *
     * @param listener The listener to remove.
     */
    public void removeDrawingListener(DrawingListener listener) {
        listeners.remove(listener);
    }

    /**
     * @return the open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * @param open whether this drawing is open in the IDE.
     */
    public void setOpen(boolean open) {
        boolean old = this.open;
        this.open = open;
        if (old != open) {
            DrawingPropertyChangeEvent dpce = new DrawingPropertyChangeEvent(
                    this, DrawingProperty.OPEN, old, open);
            fire(dpce);
        }
    }

    /**
     * @return the saved
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * @param saved whether this drawing has unsaved changes.
     */
    public void setSaved(boolean saved) {
        boolean old = saved;
        this.saved = saved;
        if (old != saved) {
            DrawingPropertyChangeEvent dpce = new DrawingPropertyChangeEvent(
                    this, DrawingProperty.SAVED, old, saved);
            fire(dpce);
        }
    }

    /**
     * Notify Drawing listeners installed in this Drawing of some property
     * change.
     *
     * @param event the event to fire.
     */
    private void fire(DrawingPropertyChangeEvent event) {
        for (DrawingListener listener : listeners) {
            if (listener != null) {
                listener.drawingPropertyChanged(event);
            }
        }
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
    public void setCanvas(JEFCanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * @return the graph
     */
    public ApplicationControlGraph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(ApplicationControlGraph graph) {
        this.graph = graph;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        if (name == null ? old != null : !name.equals(old)) {
            DrawingPropertyChangeEvent dpce = new DrawingPropertyChangeEvent(
                    this, DrawingProperty.NAME, old, name);
            fire(dpce);
        }
    }

    /**
     * Interface for things interested to know when the properties of a drawing
     * change.
     */
    public static interface DrawingListener {

        /**
         * Method to get notified when a property of this drawing changes.
         *
         * @param event
         */
        public void drawingPropertyChanged(DrawingPropertyChangeEvent event);
    }

    public static class DrawingPropertyChangeEvent {

        private Drawing drawing;
        private DrawingProperty property;
        private Object oldValue;
        private Object newValue;

        public DrawingPropertyChangeEvent(Drawing drawing,
                DrawingProperty property, Object oldValue, Object newValue) {
            this.drawing = drawing;
            this.property = property;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public DrawingPropertyChangeEvent() {
        }

        /**
         * @return the drawing
         */
        public Drawing getDrawing() {
            return drawing;
        }

        /**
         * @param drawing the drawing to set
         */
        public void setDrawing(Drawing drawing) {
            this.drawing = drawing;
        }

        /**
         * @return the property type
         */
        public DrawingProperty getPropertyType() {
            return property;
        }

        /**
         * @param newProperty the property type to set
         */
        public void setPropertyType(
                DrawingProperty newProperty) {
            this.property = newProperty;
        }

        /**
         * @return the oldValue
         */
        public Object getOldValue() {
            return oldValue;
        }

        /**
         * @param oldValue the oldValue to set
         */
        public void setOldValue(Object oldValue) {
            this.oldValue = oldValue;
        }

        /**
         * @return the newValue
         */
        public Object getNewValue() {
            return newValue;
        }

        /**
         * @param newValue the newValue to set
         */
        public void setNewValue(Object newValue) {
            this.newValue = newValue;
        }
    }

    public static enum DrawingProperty {

        /**
         * Drawing open property.
         */
        OPEN,
        /**
         * Drawing saved property.
         */
        SAVED,
        /**
         * Drawing name property.
         */
        NAME
    }
}
